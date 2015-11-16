package org.jboss.ide.eclipse.as.reddeer.server.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.ide.eclipse.as.reddeer.server.editor.WelcomeToServerEditor;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.editor.ServerEditor;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * Represents a JBoss server and contains state and operations specific to this kind of server. 
 * Note, however, that it is upon the user of this class to check if the server is really JBoss. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossServer extends Server {

	public static final String XML_LABEL_DECORATION_SEPARATOR = "   ";
	
	private static final Logger log = Logger.getLogger(JBossServer.class);

	protected JBossServer(TreeItem treeItem, ServersView view) {
		super(treeItem, view);
	}

	@Override
	public JBossServerEditor open() {
		ServerEditor editor = super.open();

		if (!(editor instanceof JBossServerEditor)){
			throw new IllegalStateException("Unexpected ServerEditor subtype. Expected: " + JBossServerEditor.class + " but was: " + editor.getClass());
		}
		return (JBossServerEditor) editor;
	}

	@Override
	public JBossServerModule getModule(String name) {
		ServerModule module = super.getModule(name);

		if (!(module instanceof JBossServerModule)){
			throw new IllegalStateException("Unexpected ServerModule subtype. Expected: " + JBossServerModule.class + " but was: " + module.getClass());
		}
		return (JBossServerModule) module;
	}

	public WelcomeToServerEditor openWebPage(){
		activate();
		new WaitUntil(new ContextMenuIsEnabled("Show In", "Web Browser"));
		new ContextMenu("Show In", "Web Browser").select();
		return new WelcomeToServerEditor();
	}

	@Override
	public void start() {
		checkServerAlreadyRunningDialog();
		try {
			super.start();
		} catch (WaitTimeoutExpiredException e){
			log.error("JBoss server failed to start");
			checkServerAlreadyRunningDialog();
			log.error("JBoss server's console dump:");
			ConsoleView view = new ConsoleView();
			view.open();
			log.error("\t" + view.getConsoleText());
			throw e;
		}
	}
	
	@Override
	public void restart() {
		try {
			super.restart();
		} catch (WaitTimeoutExpiredException e){
			log.error("JBoss server failed to restart");
			checkServerAlreadyRunningDialog();
			log.error("JBoss server's console dump:");
			ConsoleView view = new ConsoleView();
			view.open();
			log.error("\t" + view.getConsoleText());
			throw e;
		}
	}

	/**
	 * Retrieves the XML configuration items listed under the specified category. 
	 * 
	 * @param categoryName
	 * @return
	 */
	public List<XMLConfiguration> getXMLConfiguration(String categoryName){
		activate();
		TreeItem categoryItem = treeItem.getItem("XML Configuration").getItem(categoryName);
		List<TreeItem> configurationItems = categoryItem.getItems();
		
		// does not work on AS 4.0
		new WaitUntil(new TreeItemLabelDecorated(configurationItems.get(0)), TimePeriod.NORMAL, false);
		
		// does not work on AS 3.2
		new WaitUntil(new TreeItemLabelDecorated(configurationItems.get(configurationItems.size() - 1)), TimePeriod.NONE, false);

		List<XMLConfiguration> configurations = new ArrayList<XMLConfiguration>();
		for (final TreeItem item : configurationItems){
			String[] columns = item.getText().split(XML_LABEL_DECORATION_SEPARATOR);
			if (columns.length < 2){
				// it is nested node, we should process it recursively in the future
				// but for now not crucial, let's skip it
				continue;
			}
			configurations.add(new XMLConfiguration(columns[0].trim(), columns[1].trim()));
		}
		return configurations;
	}

	@Override
	protected ServerEditor createServerEditor(String title) {
		return new JBossServerEditor(title);
	}

	@Override
	protected ServerModule createServerModule(TreeItem item) {
		return new JBossServerModule(item, view);
	}

	private void checkServerAlreadyRunningDialog() {
		try {
			Shell shell = new DefaultShell("Server already running on localhost");
			shell.close();
			throw new ServerAlreadyStartedException();
		} catch (SWTLayerException e){
			// do nothing
		}
	}
	
	/**
	 * Checks if the tree item label is decorated. In case of server, the separator is "  ".
	 * 
	 * @author Lucia Jelinkova
	 *
	 */
	private static class TreeItemLabelDecorated extends AbstractWaitCondition {

		private TreeItem item;

		private TreeItemLabelDecorated(TreeItem item) {
			super();
			this.item = item;
		}

		@Override
		public boolean test() {
			return item.getText().contains(XML_LABEL_DECORATION_SEPARATOR);
		}

		@Override
		public String description() {
			return "Expected the tree item to be decorated with separator '" + XML_LABEL_DECORATION_SEPARATOR + "'";
		}
	}
	
	private static class ContextMenuIsEnabled extends AbstractWaitCondition {

		private String[] path;
		
		public ContextMenuIsEnabled(String... path) {
			this.path = path;
		}

		@Override
		public boolean test() {
			return new ContextMenu(path).isEnabled();
		}

		@Override
		public String description() {
			return "context menu item is enabled";
		}
		
	}

	class ServerAlreadyStartedException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		public ServerAlreadyStartedException() {
			super("Server already running on localhost");
		}
	}
}

package org.jboss.ide.eclipse.as.reddeer.server.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.reddeer.eclipse.wst.server.ui.editor.ServerEditor;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * Represents a JBoss server and contains state and operations specific to this kind of server. 
 * Note, however, that it is upon the user of this class to check if the server is really JBoss. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossServer extends Server {

	public static final String XML_LABEL_DECORATION_SEPARATOR = "   ";

	public JBossServer(TreeItem treeItem) {
		super(treeItem);
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

	public void openWebPage(){
		select();
		new ContextMenu("Show In", "Web Browser").select();
	}

	@Override
	public void start() {
		checkServerAlreadyRunningDialog();
		try {
			super.start();
		} catch (WaitTimeoutExpiredException e){
			checkServerAlreadyRunningDialog();
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
		TreeItem categoryItem = treeItem.getItem("XML Configuration").getItem(categoryName);

		new WaitUntil(new TreeItemLabelDecorated(categoryItem.getItems().get(0)));

		List<XMLConfiguration> configurations = new ArrayList<XMLConfiguration>();
		for (final TreeItem item : categoryItem.getItems()){
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
		return new JBossServerModule(item);
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
	private static class TreeItemLabelDecorated implements WaitCondition {

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

	class ServerAlreadyStartedException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		public ServerAlreadyStartedException() {
			super("Server already running on localhost");
		}
	}
}

package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewException;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.teiid.reddeer.condition.ServerHasState;

/**
 * Represents a view where one can manage Teiid instances.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class TeiidInstanceView extends WorkbenchView {

	public static final String TOOLBAR_CREATE_TEIID = "Create a new Teiid instance";
	public static final String TOOLBAR_RECONNECT_TEIID = "Reconnect to the selected Teiid Instance";
	private static int TEIID_INSTANCE_TREE_INDEX = 3;

	/**
	 * Previous to Kepler
	 */
	@Deprecated
	public TeiidInstanceView() {
		super("Teiid Designer", "Teiid");
	}
	
	public TeiidInstanceView(boolean isKelperOrMore){
		super("Servers");
	}

	public void reconnect(String teiidInstance) {
		throw new UnsupportedOperationException();
		// bot().tree().getTreeItem(teiidInstance).select();
		// getToolbarButtonWitTooltip(TOOLBAR_RECONNECT_TEIID).click();
	}

	public void deleteDataSource(String teiidInstance, String dataSource) {
		new DefaultTreeItem(teiidInstance, "Data Sources", dataSource).select();
		new ContextMenu("Delete Data Source").select();
	}

	public void undeployVDB(String teiidInstance, String vdb) {
		new DefaultTreeItem(teiidInstance, "VDBs", vdb).select();
		new ContextMenu("Undeploy VDB").select();
	}

	public boolean containsDataSource(String teiidInstance, String datasource) {
		SWTBot bot = new SWTWorkbenchBot();
		try {
			SWTBotTreeItem item = bot.tree().expandNode(teiidInstance, "Data Sources");
			item.getNode(datasource);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}

	/**
	 * Previous to Kepler
	 * @param teiidInstance
	 * @param vdb
	 * @return
	 */
	@Deprecated
	public boolean containsVDB(String teiidInstance, String vdb) {
		SWTBot bot = new SWTWorkbenchBot();
		try {
			SWTBotTreeItem item = bot.tree().expandNode(teiidInstance, "VDBs");
			item.getNode(vdb);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @param isKeplerOrMore
	 * @param pathToVDB path to VDB (the last element is name of VDB)
	 * @return
	 */
	public boolean containsVDB(boolean isKeplerOrMore, String... pathToVDB) {
		SWTBot bot = new SWTWorkbenchBot();
		try {
			//SWTBotTreeItem item = bot.tree(TEIID_INSTANCE_TREE_INDEX).expandNode(pathToVDB);new ContextMenu("Refresh");OK; new TeiidInstanceView(true);expand;getNode;
			new DefaultTreeItem(pathToVDB).select();
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}
	
	public static void setTeiidInstanceTreeIndex(int index){
		TEIID_INSTANCE_TREE_INDEX = index;
	}
	

	/**
	 * Previous to Kepler
	 * @param name
	 * @return
	 */
	@Deprecated
	public boolean containsTeiidInstance(String name) {
		SWTBot bot = new SWTWorkbenchBot();
		try {
			bot.tree().getTreeItem(name);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}

	public void setDefaultTeiidInstance(String teiidInstanceUrl) {
		new GuidesView().chooseAction("Teiid",
				"Set the Default JBoss / Teiid Instance");
		
		try {
			new DefaultCombo().setSelection(teiidInstanceUrl);
			new PushButton("OK").click();
		} catch (Exception ex){
			//dialog doesn't appear if only 1 server instance is defined
		}
		
		//dialog doesn't apper if restart of server precedes setup of default teiid instance
		try {
			if (new SWTWorkbenchBot().activeShell().getText().equals("Change of Server Version")){
				new PushButton("Yes").click();// save all opened editors
			} else {
				new PushButton("No").click();// disconnect from actual teiid instance (Disconnect Current Default Teiid Instance)
			}
		} catch (Exception ex){
			System.err.println(ex.getCause() + "," + ex.getMessage());
		}
		
		new PushButton("OK").click();// confirm change of default server
	}
	
	public void startServer(String serverName){
		Server server = new ServersView().getServer(serverName);
		server.start();
		new WaitUntil(new ServerHasState(serverName), TimePeriod.LONG);
	}
	
	public void stopServer(String serverName){
		Server server = new ServersView().getServer(serverName);
		log.info("Stopping server " + server.getLabel().getName());
		ServerState state = server.getLabel().getState();
		if (!ServerState.STARTING.equals(state) && !state.isRunningState()){
			throw new ServersViewException("Cannot stop server because it not running");
		}
		try {
			new DefaultTreeItem(serverName + "  [Started, Synchronized]").select();
		} catch (Exception ex){
			new DefaultTreeItem(serverName + "  [Started]").select();
		}
		
		new ContextMenu("Stop").select();
		AbstractWait.sleep(TimePeriod.SHORT.getSeconds() * 1000);
		//new WaitUntil(new ServerHasState(serverName), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}

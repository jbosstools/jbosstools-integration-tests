package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;

/**
 * Represents a view where one can manage Teiid instances.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class TeiidInstanceView extends WorkbenchView {

	public static final String TOOLBAR_CREATE_TEIID = "Create a new Teiid instance";
	public static final String TOOLBAR_RECONNECT_TEIID = "Reconnect to the selected Teiid Instance";

	public TeiidInstanceView() {
		super("Teiid Designer", "Teiid");
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
		SWTBot bot = Bot.get();
		try {
			SWTBotTreeItem item = bot.tree().expandNode(teiidInstance, "Data Sources");
			item.getNode(datasource);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}

	public boolean containsVDB(String teiidInstance, String vdb) {
		SWTBot bot = Bot.get();
		try {
			SWTBotTreeItem item = bot.tree().expandNode(teiidInstance, "VDBs");
			item.getNode(vdb);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}

	public boolean containsTeiidInstance(String name) {
		SWTBot bot = Bot.get();
		try {
			bot.tree().getTreeItem(name);
			return true;
		} catch (WidgetNotFoundException e) {
			return false;
		}
	}
}

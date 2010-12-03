package org.jboss.tools.esb.ui.bot.tests.examples;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class ESBExampleTest extends SWTTestExt{
	protected void fixLibrary(String project, String lib) {
		SWTBotTree tree = projectExplorer.show().bot().tree();
		SWTBotTreeItem proj = tree.select(project).getTreeItem(project);
		boolean fixed=false;
		boolean found=false;
		for (SWTBotTreeItem item : proj.getItems()) {
			if (item.getText().startsWith(lib)) {
				found = true;
				ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
				new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.PROPERTIES, false)).click();
				SWTBotShell shell = bot.activeShell();
				shell.bot().table().select(configuredState.getServer().name);
				open.finish(shell.bot(),IDELabel.Button.OK);
				fixed=true;
				break;
			}
		}
		if (!fixed && found) {
			log.error("Libray starting with '"+lib+"' in project '"+project+"' was not fixed.");
			bot.sleep(Long.MAX_VALUE);
		}
	}
	/**
	 * gets label in project examples tree derived by version of soa we currently run
	 * @return
	 */
	protected String getRunningSoaVersionTreeLabel() {
		String ret = "ESB for SOA-P ";
		if (configuredState.getServer().version.equals("5.0")) {
			ret+="5.0";
		}
		if (configuredState.getServer().version.equals("5.1")) {
			ret+="5.0";
		}
		else if (configuredState.getServer().version.equals("4.3")) {
			ret+="4.3";
			if (jbt.isJBDSRun()) {
				return "ESB";
			}
		}
		else {
			return null;
		}
		return ret;
	}
}

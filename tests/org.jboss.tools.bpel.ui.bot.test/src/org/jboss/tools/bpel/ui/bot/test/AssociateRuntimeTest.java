package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;


@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present), perspective="BPEL")
public class AssociateRuntimeTest extends BPELTest {
	
	@Test
	public  void associateRuntimeTest(){
		createNewProject("testproject");
		
		SWTBotView view = bot.viewByTitle("Project Explorer");
		view.show();
		view.setFocus();

		SWTBotTreeItem item = SWTEclipseExt.selectTreeLocation(view.bot(), "testproject");

		ContextMenuHelper.prepareTreeItemForContextMenu(view.bot().tree(), item);
		ContextMenuHelper.clickContextMenu(view.bot().tree(), "Properties");
		
		SWTBotShell shell = bot.shell("Properties for testproject");
		shell.activate();
		
		SWTEclipseExt.selectTreeLocation(shell.bot(), "Targeted Runtimes");
		
		String serverName = AssociateRuntimeTest.configuredState.getServer().name;
		assertTrue(shell.bot().table().containsItem(serverName));
		
		SWTBotTableItem cell = shell.bot().table().getTableItem(serverName);
		cell.select();
		cell.toggleCheck();
		
		assertTrue(cell.isChecked());
		
		shell.bot().button(IDELabel.Button.APPLY).click();
		shell.bot().button(IDELabel.Button.OK).click();
		
	}
	
	

}

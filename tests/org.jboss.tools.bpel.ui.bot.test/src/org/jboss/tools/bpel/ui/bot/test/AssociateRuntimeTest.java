package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.tools.bpel.ui.bot.ext.wizard.NewProjectWizard;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present), perspective = "BPEL")
public class AssociateRuntimeTest extends SWTTestExt {

	@Test
	public void testModeling() throws Exception {
		new NewProjectWizard("runtimeTest").execute();

		new PackageExplorer().getProject("runtimeTest").select();

		new ContextMenu("Properties").select();

		new ShellTreeItem("Targeted Runtimes").select();

		String serverName = configuredState.getServer().name;
		assertTrue(containsItem(new DefaultTable(), serverName));

		new PushButton("OK").click();
	}

	private static boolean containsItem(Table table, String item) {
		int count = table.rowCount();
		for (int i = 0; i < count; i++) {
			if (table.cell(i, 0).equals(item)) {
				return true;
			}
		}
		return false;
	}

}

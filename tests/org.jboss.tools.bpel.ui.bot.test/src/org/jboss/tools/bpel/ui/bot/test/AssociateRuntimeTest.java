package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.bpel.reddeer.wizard.NewProjectWizard;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELSuite;
import org.jboss.tools.bpel.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.bpel.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Type;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "BPEL")
@Server(type = Type.ALL, state = State.PRESENT)
public class AssociateRuntimeTest extends SWTBotTestCase {

	@Test
	public void testModeling() throws Exception {
		new NewProjectWizard("runtimeTest").execute();

		new PackageExplorer().getProject("runtimeTest").select();

		new ContextMenu("Properties").select();

		new DefaultTreeItem("Targeted Runtimes").select();

		String serverName = BPELSuite.getServerName();
		assertTrue(containsItem(new DefaultTable(), serverName));

		new PushButton("OK").click();
	}

	private static boolean containsItem(Table table, String item) {
		int count = table.rowCount();
		for (int i = 0; i < count; i++) {
			if (table.getItem(i).getText(0).equals(item)) {
				return true;
			}
		}
		return false;
	}

}

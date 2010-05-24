package org.jboss.tools.ws.ui.bot.test;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public class JBossWSProjectFacetBotTest extends TestCase {
	private static SWTWorkbenchBot bot;
	private static final String JBOSSWS_HOME_DEFAULT = "D:/softinstall/jboss-5.1.0.GA";
	public static final String JBOSSWS_42_HOME = "jbosstools.test.jboss.home.5.1";
	public static final String JBOSS_AS_42_HOME = System.getProperty(
			JBOSSWS_42_HOME, JBOSSWS_HOME_DEFAULT);
	private IProject project;

	protected void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		createServerRuntime();
	}

	public void testNewWizard() throws IOException, CoreException {
		bot.menu("File").menu("New").menu("Project...").click();
		bot.shell("New Project").activate();
		SWTBotTree tree  = bot.tree();
		tree.expandNode("Web").expandNode("Dynamic Web Project").select();
	    assertTrue(bot.button("Next >").isEnabled());
	    bot.button("Next >").click();
	    bot.shell("New Dynamic Web Project").activate();
	    assertFalse(bot.button("Finish").isEnabled());
	    
	    bot.textWithLabel("Project name:").setText("A");
	    assertTrue(bot.button("Finish").isEnabled());
	    bot.comboBoxInGroup("Dynamic web module version").setSelection("2.5");
	    bot.comboBoxInGroup("Configuration").setSelection("JBossWS Web Service Project v3.0");
	    assertFalse(bot.button("Finish").isEnabled());
	    bot.button("Next >").click();
	    bot.button("Next >").click();
	    bot.button("Next >").click();
	    bot.radio(0).click();
	    bot.sleep(6000);
	    assertTrue(bot.button("Finish").isEnabled());
	    bot.button("Finish").click();
	    
	    project = ResourcesPlugin.getWorkspace().getRoot().getProject("A");
	    assertNotNull(project);
	    
	}

	protected void tearDown() throws Exception {
		bot = null;
	}

	protected void createServerRuntime() {
		if (!isServerRuntimeDefined(bot, "AS4.2Runtime")) {
			bot.menu("File").menu("New").menu("Other...").click();
			bot.shell("New").activate();
			SWTBotTree tree = bot.tree();
			bot.sleep(1000);
			tree.expandNode("Server").select("Server");
			bot.button("Next >").click();
			SWTBotTree tree2 = bot.tree();
			tree2.expandNode("JBoss Community").select("JBoss AS 5.1");
			bot.textWithLabel("Server name:").setText("AS4.2Server");
			bot.button("Next >").click();
			bot.textWithLabel("Name").setText("AS4.2Runtime");
			bot.textWithLabel("Home Directory").setText(JBOSS_AS_42_HOME);
			bot.button("Finish").click();
			bot.sleep(2000);
		}
	}

	public static boolean isServerRuntimeDefined(SWTWorkbenchBot bot,
			String runtimeName) {

		boolean serverRuntimeNotDefined = true;

		bot.menu("Window").menu("Preferences").click();
		bot.shell("Preferences").activate();
		bot.tree().expandNode("Server").select("Runtime Environments");

		SWTBotTable tbRuntimeEnvironments = bot.table();
		int numRows = tbRuntimeEnvironments.rowCount();
		if (numRows > 0) {
			int currentRow = 0;
			while (serverRuntimeNotDefined && currentRow < numRows) {
				if (tbRuntimeEnvironments.cell(currentRow, 0).equalsIgnoreCase(
						runtimeName)) {
					serverRuntimeNotDefined = false;
				} else {
					currentRow++;
				}
			}
		}

		bot.button("OK").click();

		return !serverRuntimeNotDefined;

	}
}

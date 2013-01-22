package org.jboss.tools.jsf.ui.bot.test.jsf2.refactor;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class JSF2MoveParticipantTest extends JSF2AbstractRefactorTest {

	public void testJSF2MoveParticipant() throws Exception {
		createTestPage();
		createCompositeComponent();
		createDistResFolder();
		moveCurrResFolder();
		checkContent();
	}

	private void checkContent() throws Exception {
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		SWTBotTree tree = innerBot.tree();
		tree
				.expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode(JSF2_Test_Page_Name + ".xhtml").doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		delay();
		SWTBotEclipseEditor editor = bot.editorByTitle(
				JSF2_Test_Page_Name + ".xhtml").toTextEditor(); //$NON-NLS-1$
		assertEquals(
				loadFileContent("refactor/jsf2MoveTestPageRefactor.html"), editor.getText()); //$NON-NLS-1$
		delay();
		editor.close();
	}

	private void moveCurrResFolder() throws Exception {
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		SWTBotTree tree = innerBot.tree();
		SWTEclipseExt.getTreeItemOnPath(innerBot,
		    tree, 
		    Timing.time1S(), 
		    "mycomp", 
		    new String[]{JBT_TEST_PROJECT_NAME,"WebContent","resources"}).select();
		bot.menu("Refactor").menu("Move...").click(); //$NON-NLS-1$ //$NON-NLS-2$
		SWTBotShell shMove = bot.shell("Move").activate();
		innerBot = shMove.bot(); //$NON-NLS-1$
		tree = innerBot.tree();
    SWTEclipseExt.getTreeItemOnPath(innerBot,
        tree, 
        Timing.time1S(), 
        "mycomp1", 
        new String[]{JBT_TEST_PROJECT_NAME,"WebContent","resources"}).select();
		bot.button("Preview >").click(); //$NON-NLS-1$
		checkPreview();
		SWTBotShell activeShell = bot.activeShell();
		bot.button("OK").click(); //$NON-NLS-1$
		bot.waitUntil(shellCloses(activeShell));
		bot.sleep(Timing.time3S());
		util.waitForNonIgnoredJobs(Timing.time3S());
	}

	private void createDistResFolder() throws Exception {
	  bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).show();
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		SWTBotTree tree = innerBot.tree();
		try {
			tree
					.expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode("resources").expandNode("mycomp1").select(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} catch (WidgetNotFoundException e) {
			tree
					.getTreeItem(
							JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode("resources").select(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			bot.menu("File").menu("New").menu("Folder").click(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			bot.textWithLabel("Folder name:").setText("mycomp1"); //$NON-NLS-1$ //$NON-NLS-2$
			bot.button("Finish").click(); //$NON-NLS-1$
			delay();
		}
	}

	@Override
	public void tearDown() throws Exception {
		eclipse.deleteFile(JBT_TEST_PROJECT_NAME,"WebContent",JSF2_Test_Page_Name + ".xhtml");
		eclipse.deleteFile(JBT_TEST_PROJECT_NAME,"WebContent","resources","mycomp1");
		super.tearDown();
	}
	
	private void checkPreview(){
		delay();
		SWTBotTree tree = bot.tree();
		tree
				.expandNode("Rename composite URI changes").expandNode("jsf2TestPage.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent").expandNode("Rename composite URI"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

}

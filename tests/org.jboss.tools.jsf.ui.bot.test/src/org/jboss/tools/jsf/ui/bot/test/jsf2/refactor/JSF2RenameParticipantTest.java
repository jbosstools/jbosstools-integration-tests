package org.jboss.tools.jsf.ui.bot.test.jsf2.refactor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class JSF2RenameParticipantTest extends JSF2AbstractRefactorTest {

	public void testJSF2RenameParticipant() throws Exception {
		createCompositeComponent();
		createTestPage();
		renameCompositeComponent();
		checkContent();
	}

	private void renameCompositeComponent() throws Exception {
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		SWTBotTree tree = innerBot.tree();
		tree
				.expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode("resources").expandNode("mycomp").expandNode("echo.xhtml").select(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		bot.menu("Refactor").menu("Rename...").click(); //$NON-NLS-1$ //$NON-NLS-2$
		SWTBotShell shell = bot.shell(IDELabel.Shell.RENAME_RESOURCE).activate();
		bot.textWithLabel("New name:").setText("echo1.xhtml"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Preview >").click(); //$NON-NLS-1$
		checkPreview();
		bot.button("OK").click(); //$NON-NLS-1$
		delay();
		bot.waitWhile(new ShellIsActiveCondition(shell),Timing.time10S());
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
				loadFileContent("refactor/jsf2RenameTestPageRefactor.html"), editor.getText()); //$NON-NLS-1$
		delay();
		editor.close();
	}

	@Override
	public void tearDown() throws Exception {
		eclipse.deleteFile(JBT_TEST_PROJECT_NAME, "WebContent",JSF2_Test_Page_Name + ".xhtml");
		eclipse.deleteFile(JBT_TEST_PROJECT_NAME,"WebContent","resources","mycomp","echo1.xhtml");
		super.tearDown();
	}

	private void checkPreview() {
		delay();
		SWTBotTree tree = bot.tree();
		tree
				.expandNode("Rename composite component changes").expandNode("jsf2TestPage.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent").expandNode("Rename composite component"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

}

package org.jboss.tools.jsf.ui.bot.test.jsf2.refactor;

import java.io.IOException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class JSF2AttributeRenameTest extends JSF2AbstractRefactorTest {

	public void testJSF2AttributeRename() throws Exception {
		createCompositeComponent();
		createTestPage();
		renameCompositeAttribute();
		checkContent();
	}

	private void renameCompositeAttribute() throws IOException {
		SWTBotEclipseEditor editor = bot
				.editorByTitle("echo.xhtml").toTextEditor(); //$NON-NLS-1$
		editor.selectRange(9, 29, 1);
		// for Eclipse Juno focus has to be moved out and back from editor
		packageExplorer.show();
		packageExplorer.bot().tree().setFocus();
		editor.setFocus();
		bot.menu("Refactor").menu("Rename").click(); //$NON-NLS-1$ //$NON-NLS-2$
		SWTBotShell shell = bot.shell("Rename Composite Attribute").activate(); //$NON-NLS-1$
		bot.textWithLabel("New name:").setText("echo1"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Preview >").click(); //$NON-NLS-1$
		checkPreview();
		bot.button("OK").click(); //$NON-NLS-1$
		bot.sleep(Timing.time2S());
		bot.waitWhile(new ShellIsActiveCondition(shell),Timing.time10S());
	}

	private void checkContent() throws IOException {
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode(JSF2_Test_Page_Name + ".xhtml").doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		delay();
		SWTBotEclipseEditor editor = bot.editorByTitle(
				JSF2_Test_Page_Name + ".xhtml").toTextEditor(); //$NON-NLS-1$
		assertEquals(
				loadFileContent("refactor/jsf2RenameAttrTestPageRefactor.html"), editor.getText()); //$NON-NLS-1$
		delay();
		editor.close();
		innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode("resources").expandNode("mycomp").expandNode("echo.xhtml").doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		delay();
		editor = bot.editorByTitle("echo.xhtml").toTextEditor(); //$NON-NLS-1$
		assertEquals(
				loadFileContent("refactor/compositeComponentAfterRename.html"), editor.getText()); //$NON-NLS-1$
	}

	@Override
	protected void createCompositeComponent() throws Exception {
		super.createCompositeComponent();
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER)
				.bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent").expandNode("resources").expandNode("mycomp").expandNode("echo.xhtml").doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		SWTBotEclipseEditor editor = bot
				.editorByTitle("echo.xhtml").toTextEditor(); //$NON-NLS-1$
		bot.menu("Edit").menu("Select All").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$//$NON-NLS-2$
		bot.sleep(2000);
		editor.setText(loadFileContent("refactor/compositeComponent.html")); //$NON-NLS-1$
		editor.save();
		bot.sleep(2000);
	}

	@Override
	public void tearDown() throws Exception {
		eclipse.deleteFile(JBT_TEST_PROJECT_NAME,"WebContent",JSF2_Test_Page_Name + ".xhtml");
		eclipse.deleteFile(JBT_TEST_PROJECT_NAME,"WebContent","resources","mycomp","echo.xhtml");
		super.tearDown();
	}

	private void checkPreview() throws IOException {
		delay();
		SWTBotTree tree = bot.tree();
		tree.expandNode("Composite attribute name changes").expandNode("echo.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent/resources/mycomp").expandNode("Rename composite attribute name"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		tree.expandNode("Composite attribute name changes").expandNode("jsf2TestPage.xhtml - " + JBT_TEST_PROJECT_NAME + "/WebContent").expandNode("Rename composite attribute"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		SWTBotStyledText styledText = bot.styledText(0);
		assertEquals(
				loadFileContent("refactor/compositeComponent.html"), styledText.getText()); //$NON-NLS-1$
	}

}

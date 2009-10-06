package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class ChangeEditorTabForTheFirstOpenPageTest extends PreferencesTestCase{
	
	public void testChangeEditorTabForTheFirstOpenPage(){
		
		//Test set default source tab
		
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.comboBoxWithLabel(SELECT_DEFAULT_TAB).setSelection("Source");
		bot.button("OK").click();
		
		//Create and open new page
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
	
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent")
		.getNode("pages").select();
		
		bot.menu("File").menu("New").menu("JSP File").click();
		bot.shell("New File JSP").activate();
		bot.textWithLabel("Name*").setText("testPage");
		bot.button("Finish").click();
		bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		
		//Check if the tab changed
		WidgetNotFoundException exception = null;
		try {
			bot.toolbarButtonWithTooltip("Refresh").click();
		} catch (WidgetNotFoundException e) {
			exception = e;
		}
		assertNotNull(exception);
	}
	
	@Override
	protected void tearDown() throws Exception {
		
		//Delete test page if it has been created
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent")
		.expandNode("pages").getNode("testPage.jsp").select();
		bot.menu("Edit").menu("Delete").click();
		bot.shell("Confirm Delete").activate();
		bot.button("OK").click();
		bot.multiPageEditorByTitle(TEST_PAGE).selectTab("Visual/Source");
		super.tearDown();
	}
	
}

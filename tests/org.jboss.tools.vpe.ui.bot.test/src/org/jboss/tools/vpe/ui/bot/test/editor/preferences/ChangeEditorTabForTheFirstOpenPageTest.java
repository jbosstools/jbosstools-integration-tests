package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.test.WidgetVariables;

public class ChangeEditorTabForTheFirstOpenPageTest extends PreferencesTestCase{
	
	public void testChangeEditorTabForTheFirstOpenPage(){
		
		//Test set default source tab
		
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.comboBoxWithLabel(SELECT_DEFAULT_TAB).setSelection("Source"); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		
		//Create and open new page
		
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
	
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent") //$NON-NLS-1$
		.getNode("pages").select(); //$NON-NLS-1$
		
		bot.menu("File").menu("New").menu("JSP File").click(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		bot.shell("New File JSP").activate(); //$NON-NLS-1$
		bot.textWithLabel("Name*").setText("testPage"); //$NON-NLS-1$ //$NON-NLS-2$
		bot.button("Finish").click(); //$NON-NLS-1$
		bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).setFocus();
		
		//Check if the tab changed
		WidgetNotFoundException exception = null;
		try {
			bot.toolbarButtonWithTooltip("Refresh").click(); //$NON-NLS-1$
		} catch (WidgetNotFoundException e) {
			exception = e;
		}
		assertNotNull(exception);
	}
	
	@Override
	protected void tearDown() throws Exception {
		
		//Delete test page if it has been created
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		innerBot.tree().expandNode(JBT_TEST_PROJECT_NAME).expandNode("WebContent") //$NON-NLS-1$
		.expandNode("pages").getNode("testPage.jsp").select();  //$NON-NLS-1$//$NON-NLS-2$
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.shell("Confirm Delete").activate(); //$NON-NLS-1$
		bot.button("OK").click(); //$NON-NLS-1$
		bot.multiPageEditorByTitle(TEST_PAGE).selectTab("Visual/Source"); //$NON-NLS-1$
		super.tearDown();
	}
	
}

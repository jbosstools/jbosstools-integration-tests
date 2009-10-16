package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;

public class ShowResourceBundlesUsageasELexpressionsTest extends PreferencesTestCase{

	private static String textEditor;
	private static SWTBotEclipseEditor editor;

	public void testShowResourceBundlesUsageasELexpressions() throws Throwable{
		
		editor = bot.editorByTitle(TEST_PAGE).toTextEditor();
		textEditor = editor.getText();

		//Test check VPE content with resource bundles
		
		selectELExpressions();
		checkVPE("ShowResourceBundlesUsageasELExpressions.xml");
	
		//Test check VPE content without resource bundles
		
		selectELExpressions();
		checkVPE("HideResourceBundlesUsageasELExpressions.xml");
		
	}
	
	@Override
	protected void tearDown() throws Exception {

		//Restore page state before tests
		
		editor.setFocus();
		bot.menu("Edit").menu("Select All").click();
		bot.menu("Edit").menu("Delete").click();
		editor.setText(textEditor);
		editor.save();
		super.tearDown();
	}
	
	private void checkVPE(String testPage) throws Throwable{
//		waitForBlockingJobsAcomplished(VISUAL_REFRESH);
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}
	
	private void selectELExpressions(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_RESOURCE_BUNDLES).click();
		bot.button("OK").click();
	}
	
}

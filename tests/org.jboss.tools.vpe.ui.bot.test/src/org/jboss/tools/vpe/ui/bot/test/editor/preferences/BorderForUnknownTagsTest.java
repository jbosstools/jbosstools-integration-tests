package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;

public class BorderForUnknownTagsTest extends PreferencesTestCase{

	private static String textEditor;
	private static SWTBotEclipseEditor editor;

	public void testBorderForUnknownTags() throws Throwable{
		
		//Test open page
		editor = bot.editorByTitle(TEST_PAGE).toTextEditor();
		textEditor = editor.getText();
			
		//Test insert unknown tag
		
		editor.navigateTo(12, 52);
		editor.insertText("<tagunknown></tagunknown>");
	
		//Test default Show Border value
		
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		SWTBotCheckBox checkBox = bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		bot.button("OK").click();
	
		//Test check VPE content
		
		checkVPE("ShowBorderForUnknownTag.xml");
		
		//Test hide border for unknown tag
		
		selectBorder();
		checkVPE("HideBorderForUnknownTag.xml");
		
		//Test restore previous state
		
		selectBorder();
		checkVPE("ShowBorderForUnknownTag.xml");
		
	}
	
	private void checkVPE(String testPage) throws Throwable{
		try {
			waitForBlockingJobsAcomplished(VISUAL_REFRESH, VISUAL_UPDATE);
		} catch (InterruptedException e) {
		}
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}

	private void selectBorder(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS).click();
		bot.button("OK").click();
	}
	
	@Override
	protected void tearDown() throws Exception {

		//Restore page state before tests
		
		editor.setFocus();
		bot.menu("Edit").menu("Select All").click();
		bot.menu("Edit").menu("Delete").click();
		editor.setText(textEditor);
		editor.save();
		try {
			waitForBlockingJobsAcomplished("Save");
		} catch (InterruptedException e) {
		}
		super.tearDown();
	}
	
}

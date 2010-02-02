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
		editor.insertText("<tagunknown></tagunknown>"); //$NON-NLS-1$
	
		//Test default Show Border value
		
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		SWTBotCheckBox checkBox = bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS);
		if (!checkBox.isChecked()) {
			checkBox.click();
		}
		bot.button("OK").click(); //$NON-NLS-1$
	
		//Test check VPE content
		
		checkVPE("ShowBorderForUnknownTag.xml"); //$NON-NLS-1$
		
		//Test hide border for unknown tag
		
		selectBorder();
		checkVPE("HideBorderForUnknownTag.xml"); //$NON-NLS-1$
		
		//Test restore previous state
		
		selectBorder();
		checkVPE("ShowBorderForUnknownTag.xml"); //$NON-NLS-1$
		
	}
	
	private void checkVPE(String testPage) throws Throwable{
//		waitForBlockingJobsAcomplished(VISUAL_REFRESH);
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}

	private void selectBorder(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_BORDER_FOR_UNKNOWN_TAGS).click();
		bot.button("OK").click(); //$NON-NLS-1$
	}
	
	@Override
	protected void tearDown() throws Exception {

		//Restore page state before tests
		
		editor.setFocus();
		bot.menu("Edit").menu("Select All").click(); //$NON-NLS-1$ //$NON-NLS-2$
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		editor.setText(textEditor);
		editor.save();
		super.tearDown();
	}
	
}

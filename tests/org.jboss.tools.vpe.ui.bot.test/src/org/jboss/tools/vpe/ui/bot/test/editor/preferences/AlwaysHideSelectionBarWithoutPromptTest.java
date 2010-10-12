package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class AlwaysHideSelectionBarWithoutPromptTest extends PreferencesTestCase{
	
	private static final String HID_SEL_BAR = "Hide selection bar"; //$NON-NLS-1$
	
	public void testAlwaysHideSelectionBarWithoutPrompt(){
		
		//Test Hide Selection Bar
	  openPage();
		selectSelection();
		checkIsHide();
		
		//Test Hide selection after reopen
		
		closePage();
		openPage();
		checkIsHide();
		
		//Test Show Selection Bar
		
		selectSelection();
		checkIsShow();
		
		//Test Show Selection Bar after reopen
		
		closePage();
		openPage();
		checkIsShow();
		
		//Test Hide Selection Bar button without confirm
	
		bot.toolbarButtonWithTooltip(HID_SEL_BAR).click();
		checkIsHide();
	
		//Test Show selection after reopen
		
		closePage();
		openPage();
		checkIsHide();
		
	}
	
	private void checkIsHide(){
		WidgetNotFoundException exception = null;
		try {
			bot.toolbarButtonWithTooltip(HID_SEL_BAR);
		} catch (WidgetNotFoundException e) {
			exception = e;
		}
		assertNotNull(exception);
	}
	
	private void checkIsShow(){
		WidgetNotFoundException exception = null;
		try {
			bot.toolbarButtonWithTooltip(HID_SEL_BAR);
		} catch (WidgetNotFoundException e) {
			exception = e;
		}
		assertNull(exception);
	}

	private void selectSelection(){
	  bot.toolbarToggleButton(SHOW_SELECTION_BAR).click();
	}
	
}

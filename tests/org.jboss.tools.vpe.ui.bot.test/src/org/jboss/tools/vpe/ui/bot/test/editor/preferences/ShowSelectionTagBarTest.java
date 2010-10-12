package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

public class ShowSelectionTagBarTest extends PreferencesTestCase{

	private static final String HID_SEL_BAR = "Hide selection bar"; //$NON-NLS-1$

	public void testShowSelectionTagBar(){
		
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
	
	}

	private void selectSelection(){
	  bot.toolbarToggleButton(SHOW_SELECTION_BAR).click();
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
	
}

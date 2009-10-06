package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

public class ShowNonVisualTagsTest extends PreferencesTestCase{
	
	public void testShowNonVisualTags() throws Throwable{
		
		checkVPE("DumpedTestPage.xml");
		
		//Test Show Non-Visual Tags
		
		selectShowNonVisual();
		closePage();
		openPage();
		checkVPE("ShowNonVisualTags.xml");
		
		//Test Hide Non-Visual Tags
		selectShowNonVisual();
		closePage();
		openPage();
		checkVPE("DumpedTestPage.xml");

	}
	
	private void checkVPE(String testPage) throws Throwable{
		try {
			waitForBlockingJobsAcomplished(VISUAL_REFRESH, VISUAL_UPDATE);
		} catch (InterruptedException e) {
		}
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}
	
	private void selectShowNonVisual(){
		bot.toolbarButtonWithTooltip(PREF_TOOLTIP).click();
		bot.shell(PREF_FILTER_SHELL_TITLE).activate();
		bot.checkBox(SHOW_NON_VISUAL_TAGS).click();
		bot.button("OK").click();
	}

}

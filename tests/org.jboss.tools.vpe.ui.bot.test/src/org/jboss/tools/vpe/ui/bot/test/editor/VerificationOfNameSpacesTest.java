package org.jboss.tools.vpe.ui.bot.test.editor;

public class VerificationOfNameSpacesTest extends VPEEditorTestCase{

	private static String testText = "<jsp:root\n" +
			"xmlns:jsp=\"http://java.sun.com/JSP/Page\n" +
			"xmlns:public=\"http://www.jspcentral.com/tags\"\n" +
			"version=\"1.2\">\n" +
			"...\n" +
			"</jsp:root>";
	
	public void testVerificationOfNameSpaces() throws Throwable{
		
		//Test open page
		
		openPage();
		
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
		
		//Test clear source
		
		getEditor().setFocus();
		bot.menu("Edit").menu("Select All").click();
		delay();
		bot.menu("Edit").menu("Delete").click();
		
		//Test insert test text
		
		getEditor().setText(testText);
		getEditor().save();
		try {
			waitForBlockingJobsAcomplished("Save", VISUAL_REFRESH, VISUAL_UPDATE);
		} catch (InterruptedException e) {
		}
		performContentTestByDocument("VerificationOfNameSpaces.xml", bot.multiPageEditorByTitle(TEST_PAGE));
	
	}
	
}

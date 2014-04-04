package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardDialog;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Adds a new jsp file into the jsp project and checks it is hot deployed:
 * <ul>
 * 	<li>loads the new page in the web browser </li>
 * </ul>
 * 
 * NOTE: It is marked as abstract so that concrete implementation can specify their own {@link Server}
 * annotation
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class HotDeployJSPFileTemplate {

	public static final String JSP_CONTENT = 
				"<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%> \n" + 
				"<html> <body> Hot deployment </body> </html>";
	
	@Test
	public void hotDeployment(){
		NewFileCreationWizardDialog newFileDialog = new NewFileCreationWizardDialog();
		newFileDialog.open();
		NewFileCreationWizardPage page = newFileDialog.getFirstPage();
		page.setFileName("hot.jsp");
		page.setFolderPath(DeployJSPProjectTemplate.PROJECT_NAME, "WebContent");
		newFileDialog.finish();
		
		TextEditor editor = new TextEditor();
		editor.setText(JSP_CONTENT);
		editor.save();
		
		BrowserView browserView = new BrowserView();
		browserView.open();
		browserView.openPageURL("http://localhost:8080/" + DeployJSPProjectTemplate.PROJECT_NAME + "/hot.jsp");
		browserView.getText();
		assertThat(browserView.getText(), containsString("Hot deployment"));
	}
}

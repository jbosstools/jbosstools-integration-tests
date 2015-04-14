package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.ui.bot.test.condition.BrowserContainsTextCondition;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardDialog;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

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
public abstract class HotDeployJSPFileTemplate extends AbstractJBossServerTemplate {

	private static final String HOT_JSP_FILE_NAME = "hot.jsp";

	public static final String JSP_CONTENT = 
				"<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%> \n" + 
				"<html> <body> Hot deployment </body> </html>";
	
	private static final Logger log = Logger.getLogger(HotDeployJSPFileTemplate.class);
	
	@Test
	public void hotDeployment(){
		log.step("Create " + HOT_JSP_FILE_NAME + " file");
		NewFileCreationWizardDialog newFileDialog = new NewFileCreationWizardDialog();
		newFileDialog.open();
		NewFileCreationWizardPage page = newFileDialog.getFirstPage();
		page.setFileName(HOT_JSP_FILE_NAME);
		page.setFolderPath(DeployJSPProjectTemplate.PROJECT_NAME, "WebContent");
		newFileDialog.finish();
		
		log.step("Set content of " + HOT_JSP_FILE_NAME + " file");
		TextEditor editor = new TextEditor();
		editor.setText(JSP_CONTENT);
		editor.save();
		editor.close();
		
		log.step("Show " + HOT_JSP_FILE_NAME + " file in browser");
		new WaitUntil(new BrowserContainsTextCondition("http://localhost:8080/" + DeployJSPProjectTemplate.PROJECT_NAME + "/hot.jsp", "Hot deployment", true), TimePeriod.LONG);
	}
}

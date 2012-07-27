package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import static org.hamcrest.MatcherAssert.assertThat;

import org.jboss.ide.eclipse.as.ui.bot.test.web.PageSourceMatcher;
import org.jboss.ide.eclipse.as.ui.bot.test.wizard.NewFileWizard;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.junit.Test;

/**
 * Adds a new jsp file into the jsp project and checks it is hot deployed:
 * <ul>
 * 	<li>loads the new page in the web browser </li>
 * </ul>
 * @author Lucia Jelinkova
 *
 */
public class HotDeployJSPFile extends SWTTestExt {

	public static final String JSP_CONTENT = 
				"<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%> \n" + 
				"<html> <body> Hot deployment </body> </html>";
	
	@Test
	public void hotDeployment(){
		NewFileWizard wizard = new NewFileWizard();
		wizard.setPath(DeployJSPProjectAS7Server.PROJECT_NAME, "WebContent");
		wizard.setFileName("hot.jsp");
		wizard.setText(JSP_CONTENT);
		wizard.execute();
		
		SWTBotFactory.getBot().sleep(5000);
		assertThat("Hot deployment", new PageSourceMatcher("http://localhost:8080/" + DeployJSPProjectAS7Server.PROJECT_NAME + "/hot.jsp"));
	}
}

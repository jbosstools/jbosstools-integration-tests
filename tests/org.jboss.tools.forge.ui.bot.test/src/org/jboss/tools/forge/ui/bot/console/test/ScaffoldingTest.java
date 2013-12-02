package org.jboss.tools.forge.ui.bot.console.test;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;
/**
 * 
 * @author psrna
 *
 */
@Require(clearWorkspace=true)
public class ScaffoldingTest extends ForgeConsoleTestBase {

	private static final String specJPAText =     "***SUCCESS*** Installed [forge.spec.jpa] successfully.";
	private static final String specEJBText =     "***SUCCESS*** Installed [forge.spec.ejb] successfully.";
	private static final String specCDIText =     "***SUCCESS*** Installed [forge.spec.cdi] successfully.";
	private static final String specJsfApiText =  "***SUCCESS*** Installed [forge.spec.jsf.api] successfully.";
	private static final String facesText =       "***SUCCESS*** Installed [faces] successfully.";
	
	@Test
	public void scaffoldSetupTest(){
	
		createProject(ProjectTypes.war);
		getStyledText().setText("scaffold setup --scaffoldType faces\n");
		getStyledText().setText("\n"); // install scaffold provider [faces]
		getStyledText().setText("\n"); //default version of 'jboss-javaee-6.0'
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole(specJPAText, TIME_1S, TIME_20S*3));
		assertTrue(ConsoleUtils.waitUntilTextInConsole(specEJBText, TIME_1S, TIME_20S*3));
		assertTrue(ConsoleUtils.waitUntilTextInConsole(specCDIText, TIME_1S, TIME_20S*3));
		assertTrue(ConsoleUtils.waitUntilTextInConsole(specJsfApiText, TIME_1S, TIME_20S*3));
		assertTrue(ConsoleUtils.waitUntilTextInConsole(facesText, TIME_1S, TIME_20S*3));
		
		getStyledText().setText("\n"); //subdirectory of web-root
		
		bot.sleep(TIME_5S);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
		
		cdWS();
		clear();
		pExplorer.deleteAllProjects();	
	}
}

package org.jboss.tools.arquillian.ui.bot.test.testcase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.junit.JUnitView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.JUnitConfigurationPage;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.JUnitTestTab;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.RunConfigurationsDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.view.JUnitTestIsRunningCondition;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.junit.Test;

/**
 * Runs Arquillian test case on remote Wildfly server and checks that
 * 
 * <ul>
 * 	<li> there are no failing tests in JUnit view
 * </ul>
 * @author Lucia Jelinkova
 *
 */
@OpenPerspective(JavaPerspective.class)
@JBossServer(type=ServerReqType.WILDFLY8x, state=ServerReqState.RUNNING)
public class RunArquillianTestCase extends AbstractArquillianTestCase {

	private static final String LAUNCH_CONFIGURATION = "arq-test";
	private static final Logger log = Logger.getLogger(RunArquillianTestCase.class);

	@Test
	public void testRunningTestCase(){
		runTestCase();
		checkJUnitView();
	}

	private void runTestCase() {
		RunConfigurationsDialog runDialog = new RunConfigurationsDialog();
		runDialog.open();
		runDialog.createConfiguration(new JUnitConfigurationPage(LAUNCH_CONFIGURATION));
		
		JUnitTestTab mainTab = new JUnitTestTab();
		mainTab.setProject(PROJECT_NAME);
		mainTab.setTestClass(PACKAGE + "." + TEST_CASE);
		
//		ArquillianTab arquillianTab = new ArquillianTab();
//		arquillianTab.selectMavenProfile(AddArquillianProfile.PROFILE_NAME);
		
		runDialog.run();
		/* Intermittent timing issue (https://issues.jboss.org/browse/JBIDE-22866) is being seen where the 
		 * Run Configurations dialog is not closed - add this to make test more reliable */
		log.info("Attempting to close the Run Configurations Dialog");
		
		try {
			new DefaultShell(new WithTextMatcher(new RegexMatcher(".*Run Configurations.*"))).close();
			log.info("Closed the Run Configurations Dialog");
		} 
		catch (SWTLayerException swtle){
			log.error("Unable to close the Run Configurations Dialog - " + swtle.getMessage());
			log.error (swtle);				
		}					
	}
	
	private void checkJUnitView(){
		JUnitView view = new JUnitView();
		view.open();
		
		new WaitWhile(new JUnitTestIsRunningCondition(), TimePeriod.LONG);
		
		assertThat(view.getNumberOfErrors(), is(0));
		assertThat(view.getNumberOfFailures(), is(0));
	}
}

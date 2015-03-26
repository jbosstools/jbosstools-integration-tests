package org.jboss.tools.arquillian.ui.bot.test.testcase;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.junit.JUnitView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.ArquillianTab;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.JUnitConfigurationPage;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.JUnitTestTab;
import org.jboss.tools.arquillian.ui.bot.reddeer.configurations.RunConfigurationsDialog;
import org.jboss.tools.arquillian.ui.bot.reddeer.junit.view.JUnitTestIsRunningCondition;
import org.jboss.tools.arquillian.ui.bot.test.AbstractArquillianTestCase;
import org.jboss.tools.arquillian.ui.bot.test.project.AddArquillianProfile;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
		
		ArquillianTab arquillianTab = new ArquillianTab();
		arquillianTab.selectMavenProfile(AddArquillianProfile.PROFILE_NAME);
		
		runDialog.run();
	}
	
	private void checkJUnitView(){
		JUnitView view = new JUnitView();
		view.open();
		
		new WaitWhile(new JUnitTestIsRunningCondition(), TimePeriod.LONG);
		
		assertThat(view.getNumberOfErrors(), is(0));
		assertThat(view.getNumberOfFailures(), is(0));
	}
}

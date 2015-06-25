package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerLaunchConfiguration;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.JBossRuntimeWizardPage;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.jre.JRERequirement;
import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Starts, restarts and stops the server and checks:
 * <ul>
 * 	<li>the console output</li>
 * 	<li>server's label</li>
 * 	<li>welcome page is available (if the result state is started)</li>
 * </ul>
 * @author Lucia Jelinkova
 *
 */
@JRE
public abstract class OperateServerTemplate extends AbstractJBossServerTemplate {

	private static final Logger log = Logger.getLogger(OperateServerTemplate.class);

	@InjectRequirement
	protected JRERequirement jreRequirement;

	public abstract String getWelcomePageText();

	public abstract boolean setHeadlessModeOnMac();

	@Before
	public void setupServerJRE(){
		if (jreRequirement == null){
			return;
		}
		log.step("Set JRE to server (" + jreRequirement.getJREName() + ")");
		JBossRuntimeWizardPage runtimeWizard = getServer().open().editRuntimeEnvironment();
		runtimeWizard.setAlternateJRE(jreRequirement.getJREName());
		new WizardDialog().finish();

		getServer().open().save();
	}

	@Before
	public void setupHeadlessMode(){
		if (setHeadlessModeOnMac() && RunningPlatform.isOSX()){
			log.step("Set -Djava.awt.headless=true to launch configuration");
			JBossServerLaunchConfiguration launchConfig = getServer().open().openLaunchConfiguration();
			String currentArguments = launchConfig.getVMArguments();
			launchConfig.setVMArguments(currentArguments + " -Djava.awt.headless=true");
			new OkButton().click();
		}
	}

	@Test
	public void operateServer(){
		startServer();
		restartServer();
		stopServer();
	}

	public void startServer(){
		log.step("Start server " + getServerName());
		getServer().start();

		assertNoException("Starting server");
		assertServerState("Starting server", ServerState.STARTED);
		assertWebPageContains(getWelcomePageText());
	}

	public void restartServer(){
		log.step("Restart server " + getServerName());
		getServer().restart();

		assertNoException("Restarting server");
		assertServerState("Restarting server", ServerState.STARTED);
		assertWebPageContains(getWelcomePageText());
	}

	public void stopServer(){
		log.step("Stop server " + getServerName());
		getServer().stop();

		assertNoException("Stopping server");
		assertServerState("Stopping server", ServerState.STOPPED);
	}

	protected void assertServerState(String message, ServerState state) {
		log.step("Assert server state is " + state);
		assertThat(message, getServer().getLabel().getState(), is(state));
	}

	private void assertWebPageContains(String string) {
		//		WelcomeToServerEditor editor = getServer().openWebPage();
		// Bug with caching content - JBIDE-18685. Please uncomment when fixed. 
		//		editor.refresh();
		//		new WaitUntil(new EditorWithBrowserContainsTextCondition(editor, string));
		//		assertThat(editor.getText(), containsString(string));
	}
}

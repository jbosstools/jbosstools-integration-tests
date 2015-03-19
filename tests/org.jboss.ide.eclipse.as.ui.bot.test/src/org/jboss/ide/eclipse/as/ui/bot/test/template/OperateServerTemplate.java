package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
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
public abstract class OperateServerTemplate extends AbstractJBossServerTemplate {

	private static final Logger log = Logger.getLogger(OperateServerTemplate.class);
	
	public abstract String getWelcomePageText();

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

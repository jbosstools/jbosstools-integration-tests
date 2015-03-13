package org.jboss.ide.eclipse.as.ui.bot.test.template;

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

	public abstract String getWelcomePageText();

	@Test
	public void operateServer(){
		startServer();
		restartServer();
		stopServer();
	}

	public void startServer(){
		getServer().start();

		assertNoException("Starting server");
		assertServerState("Starting server", ServerState.STARTED);
		assertWebPageContains(getWelcomePageText());
	}

	public void restartServer(){
		getServer().restart();

		assertNoException("Restarting server");
		assertServerState("Restarting server", ServerState.STARTED);
		assertWebPageContains(getWelcomePageText());
	}

	public void stopServer(){
		getServer().stop();

		assertNoException("Stopping server");
		assertServerState("Stopping server", ServerState.STOPPED);
	}

	protected void assertServerState(String message, ServerState state) {
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

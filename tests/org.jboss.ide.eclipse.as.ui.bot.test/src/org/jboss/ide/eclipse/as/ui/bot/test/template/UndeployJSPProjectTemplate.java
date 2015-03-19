package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;

/**
 * Removes JSP project from server. Checks:
 * 
 * <ul>
 * 	<li>the console output</li>
 * 	<li>server's label</li>
 * 	<li>project is not listed under the server</li>
 * </ul>
 * @author Lucia Jelinkova
 *
 */
public abstract class UndeployJSPProjectTemplate extends AbstractJBossServerTemplate {

	private static final Logger log = Logger.getLogger(UndeployJSPProjectTemplate.class);
	
	protected abstract String getConsoleMessage();
	
	@Before
	public void clearServerConsole(){
		super.clearConsole();		
	}
	
	@Test
	public void undeployProject(){
		log.step("Undeploy " + DeployJSPProjectTemplate.PROJECT_NAME);
		JBossServer server = getServer();
		undeploy(server);
		
		// console
		log.step("Assert console has undeployment notification");
		new WaitUntil(new ConsoleHasText(getConsoleMessage()));
		assertNoException("Error in console after undeploy");
		// view
		log.step("Assert module is not visible on Servers view");
		assertTrue("Server contains no project", server.getModules().isEmpty());	
		
		log.step("Assert server's states");
		assertThat(server.getLabel().getState(), is(ServerState.STARTED));
		assertThat(server.getLabel().getPublishState(), is(ServerPublishState.SYNCHRONIZED));
	}

	private void undeploy(JBossServer server) {
		ModifyModulesDialog modifyModulesDialog = server.addAndRemoveModules();
		ModifyModulesPage modifyModulesPage = modifyModulesDialog.getFirstPage();
		modifyModulesPage.remove(DeployJSPProjectTemplate.PROJECT_NAME);;
		modifyModulesDialog.finish();
	}
}

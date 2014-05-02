package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerPublishState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
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
public abstract class UndeployJSPProjectTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	protected abstract String getConsoleMessage();
	
	@Test
	public void undeployProject(){
		JBossServer server = new JBossServerView().getServer(getServerName());
		undeploy(server);
		
		// console
		new WaitUntil(new ConsoleHasText(getConsoleMessage()));
		assertFalse(new ConsoleHasText("Exception").test());
		// view
		assertTrue("Server contains no project", server.getModules().isEmpty());	
		assertThat(server.getLabel().getState(), is(ServerState.STARTED));
		assertThat(server.getLabel().getPublishState(), is(ServerPublishState.SYNCHRONIZED));
	}

	private void undeploy(JBossServer server) {
		ModifyModulesDialog modifyModulesDialog = server.addAndRemoveModules();
		ModifyModulesPage modifyModulesPage = modifyModulesDialog.getFirstPage();
		modifyModulesPage.remove(DeployJSPProjectTemplate.PROJECT_NAME);;
		modifyModulesDialog.finish();
	}
	
	protected String getServerName() {
		return requirement.getServerNameLabelText(requirement.getConfig());
	} 
}

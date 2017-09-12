package org.jboss.tools.ws.ui.bot.test.utils;

import java.util.logging.Logger;

import org.hamcrest.core.StringContains;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.eclipse.condition.ConsoleHasText;
import org.eclipse.reddeer.eclipse.condition.ServerExists;
import org.eclipse.reddeer.eclipse.exception.EclipseLayerException;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServerModule;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerPublishState;
import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerState;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.eclipse.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public class ServersViewHelper {

	private static final Logger LOGGER = Logger
			.getLogger(ServersViewHelper.class.getName());
	
	private ServersViewHelper() {};
	
	/**
	 * Removes the specified <var>project</var> from the configured server.
	 * 
	 * @param project project to be removed from the server
	 */
	public static void removeProjectFromServer(String project, String serverName) {
		ServersView2 serversView = new ServersView2();
		serversView.activate();
		Server server = serversView.getServer(serverName);

		ServerModule serverModule = null;
		try {
			serverModule = server.getModule(project);
		} catch (EclipseLayerException e) {
			LOGGER.info("Project " + project + " was not found on the server");
			return;
		}
		if (serverModule != null) {
			String moduleName = serverModule.getLabel().getName();
			ServerState moduleState = serverModule.getLabel().getState();
			clearServerConsole(serverName);
			serverModule.remove();

			if (moduleState.equals(ServerState.STARTED)) {
				new WaitUntil(new ConsoleHasText("Undeployed \"" + moduleName), TimePeriod.LONG, false);
			}
		}
	}

	/**
	 * Removes all projects from the specified server.
	 */
	public static void removeAllProjectsFromServer(String serverName) {
		ServersView2 serversView = new ServersView2();
		if(!serversView.isOpen())
			serversView.open();
		
		Server server = null;
		try {
			serversView.activate();
			server = serversView.getServer(serverName);
		} catch (EclipseLayerException e) {
			LOGGER.warning("Server " + serverName + "not found, retrying");
			serversView.activate();
			server = serversView.getServer(serverName);			
		}
		
		while(!server.getModules().isEmpty()) {
			ServerModule module = server.getModules().get(0);
			if (module != null) {
				new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
				serversView.activate();
				String moduleName = module.getLabel().getName();
				ServerState moduleState = module.getLabel().getState();
				clearServerConsole(serverName);
				
				new WaitWhile(new JobIsRunning(), TimePeriod.DEFAULT, false);
				serversView.activate();
				module.remove();

				if (moduleState.equals(ServerState.STARTED)) {
					new WaitUntil(new ConsoleHasText("Undeployed \"" + moduleName), TimePeriod.LONG, false);
				}
			}
		}
	}

	/**
	 * Method runs project on the configured server
	 */
	public static void runProjectOnServer(String projectName) {
		new ProjectExplorer().getProject(projectName).select();
		new ShellMenuItem(org.hamcrest.core.Is.is(IDELabel.Menu.RUN), org.hamcrest.core.Is.is(IDELabel.Menu.RUN_AS),
				org.hamcrest.core.StringContains.containsString("Run on Server")).select();
		new DefaultShell("Run On Server");
		new PushButton(IDELabel.Button.FINISH).click();
	}

	/**
	 * Adds the specified project to the specified server
	 */
	public static void addProjectToServer(String projectName, String serverName) {
		ServersView2 serversView = new ServersView2();
		serversView.open();
		Server server = serversView.getServer(serverName);
		ModifyModulesDialog dialog = server.addAndRemoveModules();
		ModifyModulesPage page = new ModifyModulesPage(dialog);
		page.add(projectName);
		dialog.finish();
	}

	public static void serverClean(String serverName) {
		ServersView2 serversView = new ServersView2();
		serversView.open();
		Server server = null;
		try {
			server = serversView.getServer(serverName);
		} catch (EclipseLayerException e) {
			LOGGER.warning("Server " + serverName + "not found, retrying");
			server = serversView.getServer(serverName);
			
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		server.clean();
	}
	
	public static void waitForDeployment(String projectName, String serverName) {
		new WaitUntil(new ProjectIsDeployed(projectName, serverName), TimePeriod.getCustom(20), false);
	}
	
	private static void clearServerConsole(String serverName) {
		ConsoleView consoleView = new ConsoleView();
		if (!consoleView.isOpen()) {
			consoleView.open();
		}
		consoleView.activate();
		consoleView.switchConsole(new WithMnemonicTextMatcher(new StringContains(serverName)));
		consoleView.clearConsole();
	}
	
	private static class ProjectIsDeployed extends AbstractWaitCondition {

		private ServerModule module;
		private ServersView2 view = new ServersView2();
		
		public ProjectIsDeployed(String projectName, String serverName) {
			view.activate();
			Server server = view.getServer(serverName);
			module = server.getModule(projectName);
		}
		
		@Override
		public boolean test() {
			view.activate();
			return module.getLabel().getState().equals(ServerState.STARTED) 
					&& module.getLabel().getPublishState().equals(ServerPublishState.SYNCHRONIZED);
		}

		@Override
		public String description() {
			return "Module state is " + module.getLabel().getState() + ", " + module.getLabel().getPublishState();
		}
		
	}
}

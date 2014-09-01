package org.jboss.tools.ws.ui.bot.test.utils;

import java.util.logging.Logger;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.ModifyModulesPage;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * 
 * @author Radoslav Rabara
 *
 */
public class ServersViewHelper {

	private final Logger LOGGER = Logger
			.getLogger(this.getClass().getName());

	/**
	 * Removes the specified <var>project</var> from the configured server.
	 * @param project project to be removed from the server
	 */
	public void removeProjectFromServer(String project, String serverName) {
		ServersView serversView = new ServersView();
		Server server = serversView.getServer(serverName);

		ServerModule serverModule = null;
		try {
			serverModule = server.getModule(project);
		} catch(EclipseLayerException e) {
			LOGGER.info("Project " + project + " was not found on the server");
			return;
		}
		if(serverModule != null) {
			//TODO: use serverModule.remove(); (not implemented yet)
			ModifyModulesDialog dialog = server.addAndRemoveModules();
			ModifyModulesPage page = dialog.getFirstPage();
			page = dialog.getFirstPage();
			page.remove(serverModule.getLabel().getName());
			dialog.finish();
		}
	}

	/**
	 * Removes all projects ({@link ModifyModulesPage#getConfiguredModules()}
	 * from the specified server.
	 */
	public void removeAllProjectsFromServer(String serverName) {
		if (serverName==null) {
			return;
		}
		ServersView servers = new ServersView();
		Server server = null;
		try {
			server = servers.getServer(serverName);
		} catch(EclipseLayerException e) {
			return;
		}
		ModifyModulesDialog dialog = server.addAndRemoveModules();

		/* workaround for REDDEER-802 */
		Shell s = new DefaultShell();

		ModifyModulesPage page = dialog.getFirstPage();
		if (!page.getConfiguredModules().isEmpty()) {
			s.setFocus();//REDDEER-802
			page.removeAll();
		}
		dialog.finish();
	}

	/**
	 * Method runs project on the configured server
	 */
	public void runProjectOnServer(String projectName) {
		new ProjectExplorer().getProject(projectName).select();
		new ShellMenu(org.hamcrest.core.Is.is(IDELabel.Menu.RUN), org.hamcrest.core.Is.is(IDELabel.Menu.RUN_AS),
				org.hamcrest.core.StringContains.containsString("Run on Server")).select();
		new DefaultShell("Run On Server");
		new PushButton(IDELabel.Button.FINISH).click();
		new WaitUntil(new JobIsRunning(), TimePeriod.getCustom(5), false);
		new WaitWhile(new JobIsRunning());
	}

	/**
	 * Adds the specified project to the specified server
	 */
	public void addProjectToServer(String projectName, String serverName) {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(serverName);
		ModifyModulesDialog dialog = server.addAndRemoveModules();
		ModifyModulesPage page = dialog.getFirstPage();
		page.add(projectName);
		dialog.finish();
	}

	public void serverClean(String serverName) {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(serverName);
		server.clean();
	}
}

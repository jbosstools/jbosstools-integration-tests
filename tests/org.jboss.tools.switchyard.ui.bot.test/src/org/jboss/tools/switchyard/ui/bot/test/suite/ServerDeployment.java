package org.jboss.tools.switchyard.ui.bot.test.suite;

import org.jboss.tools.switchyard.ui.bot.test.util.BackupClient;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServerDeployment extends org.jboss.tools.switchyard.reddeer.server.ServerDeployment {

	public ServerDeployment() {
		super(SwitchyardSuite.getServerName());
	}

	public void deployProject(String project) {
		deployProject(project, project + ".jar");
	}

	@Override
	public void deployProject(String project, String checkPhrase) {
		try {
			super.deployProject(project, checkPhrase);
		} catch (RuntimeException re) {
			BackupClient.backupProject(project);
			throw re;
		}
	}

	public void fullPublish(String project) {
		fullPublish(project, project + ".jar");
	}

	@Override
	public void fullPublish(String project, String checkPhrase) {
		try {
			super.fullPublish(project, checkPhrase);
		} catch (RuntimeException re) {
			BackupClient.backupProject(project);
			throw re;
		}
	}

}

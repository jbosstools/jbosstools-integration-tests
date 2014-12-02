package org.jboss.tools.maven.ui.bot.test.repository;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.jboss.tools.maven.ui.bot.test.utils.RepositoryExists;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MavenRepositories extends AbstractMavenSWTBotTest{
	
	
	public static final String EAP_REPO = "redhat-techpreview-all-repository";
	public static final String JBOSS_REPO = "jboss-public-repository";
	
	@BeforeClass
	public static void setup(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		preferenceDialog.ok();
	}
	
	@AfterClass
	public static void clean(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		preferenceDialog.ok();
	}
	
	@Test
	public void modifyEAPRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		String repoId = mr.chooseRepositoryFromList(EAP_REPO, true);
		mr.confirm();
		preferenceDialog.ok();
		assertTrue("EAP Repository is missing in Maven repositories view", new RepositoryExists(EAP_REPO).test());
		
		preferenceDialog.open();
		preferenceDialog.select(jm);
		mr = jm.configureRepositories();
		mr.editRepo(repoId, false, null, null, null);
		mr.confirm();
		preferenceDialog.ok();
		assertFalse("EAP Repository is present in Maven repositories view", new RepositoryExists(EAP_REPO).test());

		preferenceDialog.open();
		preferenceDialog.select(jm);
		mr = jm.configureRepositories();
		mr.removeRepo(repoId+" (Inactive)");
		mr.confirm();
		preferenceDialog.ok();
		assertFalse("EAP Repository is still present in Maven repositories view", new RepositoryExists(EAP_REPO).test());
	}

	@Test
	public void modifyJBossRepo(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		String repoId = mr.chooseRepositoryFromList(JBOSS_REPO,true);
		mr.confirm();
		jm.apply();
		preferenceDialog.ok();
		assertTrue("JBoss Repository is missing in Maven repositories view", new RepositoryExists(JBOSS_REPO).test());
		
		preferenceDialog.open();
		preferenceDialog.select(jm);

		mr = jm.configureRepositories();
		mr.editRepo(repoId, false, null, null, null);
		mr.confirm();
		jm.apply();
		preferenceDialog.ok();
		assertFalse("JBOSS Repository is present in Maven repositories view", new RepositoryExists(JBOSS_REPO).test());

		jm = new ConfiguratorPreferencePage();
		preferenceDialog.open();
		preferenceDialog.select(jm);

		mr = jm.configureRepositories();
		mr.removeRepo(repoId+" (Inactive)");
		mr.confirm();
		preferenceDialog.ok();
		assertFalse("JBoss Repository is missing in Maven repositories view", new RepositoryExists(JBOSS_REPO).test());
	}

}

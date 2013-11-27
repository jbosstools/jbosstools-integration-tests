package org.jboss.tools.maven.ui.bot.test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.tools.maven.reddeer.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.ui.bot.test.utils.RepositoryExists;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MavenRepositories extends AbstractMavenSWTBotTest{
	
	
	public static final String EAP_REPO = "redhat-techpreview-all-repository";
	public static final String JBOSS_REPO = "jboss-public-repository";
	
	@BeforeClass
	public static void setup(){
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		jm.ok();
	}
	
	@AfterClass
	public static void clean(){
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		jm.ok();
	}
	
	@Test
	public void modifyEAPRepo(){
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		String repoId = mr.chooseRepositoryFromList(EAP_REPO, true);
		mr.confirm();
		jm.ok();
		assertTrue("EAP Repository is missing in Maven repositories view", new RepositoryExists(EAP_REPO).test());
		
		jm = new ConfiguratorPreferencePage();
		jm.open();
		mr = jm.configureRepositories();
		mr.editRepo(repoId, false, null, null, null);
		mr.confirm();
		jm.ok();
		assertFalse("EAP Repository is present in Maven repositories view", new RepositoryExists(EAP_REPO).test());

		jm = new ConfiguratorPreferencePage();
		jm.open();
		mr = jm.configureRepositories();
		mr.removeRepo(repoId+" (Inactive)");
		mr.confirm();
		jm.ok();
		assertFalse("EAP Repository is still present in Maven repositories view", new RepositoryExists(EAP_REPO).test());
	}

	@Test
	public void modifyJBossRepo(){
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		String repoId = mr.chooseRepositoryFromList(JBOSS_REPO,true);
		mr.confirm();
		jm.apply();
		jm.ok();
		assertTrue("JBoss Repository is missing in Maven repositories view", new RepositoryExists(JBOSS_REPO).test());
		
		jm = new ConfiguratorPreferencePage();
		jm.open();
		mr = jm.configureRepositories();
		mr.editRepo(repoId, false, null, null, null);
		mr.confirm();
		jm.apply();
		jm.ok();
		assertFalse("JBOSS Repository is present in Maven repositories view", new RepositoryExists(JBOSS_REPO).test());

		jm = new ConfiguratorPreferencePage();
		jm.open();
		mr = jm.configureRepositories();
		mr.removeRepo(repoId+" (Inactive)");
		mr.confirm();
		jm.ok();
		assertFalse("JBoss Repository is missing in Maven repositories view", new RepositoryExists(JBOSS_REPO).test());
	}

}

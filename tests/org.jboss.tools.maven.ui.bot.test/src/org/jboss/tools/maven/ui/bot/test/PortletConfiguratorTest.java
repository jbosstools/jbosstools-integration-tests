package org.jboss.tools.maven.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.maven.reddeer.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PortletConfiguratorTest extends AbstractConfiguratorsTest{
	
	@BeforeClass
	public static void setup(){
		setPerspective("Java EE");
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.chooseRepositoryFromList(MavenRepositories.JBOSS_REPO, true);
		mr.confirm();
		jm.apply();
		jm.ok();
	}
	
	@After
	public void clean(){
		deleteProjects(true, true);
	}
	
	@AfterClass
	public static void cleanRepo(){
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
	public void portletCoreConfigurator2_0(){
		createWebProject(PROJECT_NAME_PORTLET, null, false);
		convertToMavenProject(PROJECT_NAME_PORTLET, "war", false);
		addDependency(PROJECT_NAME_PORTLET, "javax.portlet", "portlet-api", "2.0");
		updateConf(PROJECT_NAME_PORTLET);
		assertTrue(hasNature(PROJECT_NAME_PORTLET, "2.0", PORTLET_FACET, PORTLET_CORE_FACET));
	}
	
	@Test
	public void portletCoreConfigurator1_0(){
		createWebProject(PROJECT_NAME_PORTLET, null, false);
		convertToMavenProject(PROJECT_NAME_PORTLET, "war", false);
		addDependency(PROJECT_NAME_PORTLET, "javax.portlet", "portlet-api", "1.0");
		updateConf(PROJECT_NAME_PORTLET);
		assertTrue(hasNature(PROJECT_NAME_PORTLET,"1.0", PORTLET_FACET, PORTLET_CORE_FACET));
	}
	
	@Test
	public void portletJSFConfigurator(){
		createWebProject(PROJECT_NAME_PORTLET, null, false);
		convertToMavenProject(PROJECT_NAME_PORTLET, "war", false);
		addDependency(PROJECT_NAME_PORTLET, "javax.portlet", "portlet-api", "1.0");
		addDependency(PROJECT_NAME_PORTLET, "org.jboss.portletbridge", "portletbridge-api", "2.0.0.FINAL");
		addDependency(PROJECT_NAME_PORTLET, "javax.faces", "jsf-api", "2.1");
		updateConf(PROJECT_NAME_PORTLET);
		assertTrue(hasNature(PROJECT_NAME_PORTLET, null, PORTLET_FACET, PORTLET_JSF_FACET));
	}

}

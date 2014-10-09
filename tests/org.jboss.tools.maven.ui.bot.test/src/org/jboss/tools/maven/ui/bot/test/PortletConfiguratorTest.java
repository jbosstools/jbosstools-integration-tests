package org.jboss.tools.maven.ui.bot.test;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class PortletConfiguratorTest extends AbstractConfiguratorsTest{
	
	@BeforeClass
	public static void setup(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		preferenceDialog.select(jm);
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		mr.chooseRepositoryFromList(MavenRepositories.JBOSS_REPO, true);
		mr.confirm();
		jm.apply();
		preferenceDialog.ok();
	}

	
	@AfterClass
	public static void cleanRepo(){
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
	public void portletCoreConfigurator2_0(){
		createWebProject(PROJECT_NAME_PORTLET, null, false);
		convertToMavenProject(PROJECT_NAME_PORTLET, "war", false);
		addDependency(PROJECT_NAME_PORTLET, "javax.portlet", "portlet-api", "2.0");
		updateConf(PROJECT_NAME_PORTLET);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_PORTLET, PORTLET_FACET, PORTLET_CORE_FACET, "2.0"));
	}
	
	@Test
	public void portletCoreConfigurator1_0(){
		createWebProject(PROJECT_NAME_PORTLET, null, false);
		convertToMavenProject(PROJECT_NAME_PORTLET, "war", false);
		addDependency(PROJECT_NAME_PORTLET, "javax.portlet", "portlet-api", "1.0");
		updateConf(PROJECT_NAME_PORTLET);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_PORTLET, PORTLET_FACET, PORTLET_CORE_FACET, "1.0"));
	}
	
	@Test
	public void portletJSFConfigurator(){
		createWebProject(PROJECT_NAME_PORTLET, null, false);
		convertToMavenProject(PROJECT_NAME_PORTLET, "war", false);
		addDependency(PROJECT_NAME_PORTLET, "javax.portlet", "portlet-api", "1.0");
		addDependency(PROJECT_NAME_PORTLET, "org.jboss.portletbridge", "portletbridge-api", "2.0.0.FINAL");
		addDependency(PROJECT_NAME_PORTLET, "javax.faces", "jsf-api", "2.1");
		updateConf(PROJECT_NAME_PORTLET);
		new WaitUntil(new ProjectHasNature(PROJECT_NAME_PORTLET, PORTLET_FACET, PORTLET_JSF_FACET, null));
	}

}

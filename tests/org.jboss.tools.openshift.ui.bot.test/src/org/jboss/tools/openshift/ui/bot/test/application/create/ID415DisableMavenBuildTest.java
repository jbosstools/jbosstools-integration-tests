package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.views.navigator.ResourceNavigator;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.wizard.application.NewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;


/**
 * Test capabilities of disabling maven build while creating a new application and verify that
 * this marker has been added.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID415DisableMavenBuildTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	@Test
	public void testCreateApplicationWithMarker() {
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME,  Datastore.DOMAIN);
	
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP,
				Datastore.DOMAIN, applicationName, false, true, false,
				true, null, null, true, null, null, null, (String[]) null);
		wizard.postCreateSteps(false);
		
		ResourceNavigator navigator = new ResourceNavigator();
		navigator.open();
		Project eapProject = null;
		for (Project project: navigator.getProjects()) {
			if (project.getText().contains(applicationName)) {
				eapProject = project;
				break;
			}
		}
		
		eapProject.select();
		assertTrue("Disable maven build marker has not been added into the project and application.",
				containsMavenMarker(eapProject));
	}
	
	private boolean containsMavenMarker(Project project) {
		for (TreeItem openshiftItem: project.getTreeItem().getItems()) {
			if (openshiftItem.getText().contains(".openshift")) {
				for (TreeItem markersItem: openshiftItem.getItems()) {
					if (markersItem.getText().contains("markers")) {
						for (TreeItem disableMvnBuildMarker: markersItem.getItems()) {
							if (disableMvnBuildMarker.getText().contains("skip_maven_build")) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}

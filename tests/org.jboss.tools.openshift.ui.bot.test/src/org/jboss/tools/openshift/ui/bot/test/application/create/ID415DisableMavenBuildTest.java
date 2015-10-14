package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.views.navigator.ResourceNavigator;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
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
		OpenShift2ApplicationWizard	wizard = new OpenShift2ApplicationWizard(DatastoreOS2.USERNAME,
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP,
				applicationName, false, true, false, true, null, null, true, null, null, null, (String[]) null);
		wizard.postCreateSteps(false);
		
		ResourceNavigator navigator = new ResourceNavigator();
		navigator.open();
		Project eapProject = navigator.getProject(applicationName);
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
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				applicationName, applicationName).perform();
	}
}

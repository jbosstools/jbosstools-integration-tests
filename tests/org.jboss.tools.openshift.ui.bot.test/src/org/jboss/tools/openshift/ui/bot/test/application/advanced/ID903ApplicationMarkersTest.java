package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.views.navigator.ResourceNavigator;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of OpenShift markers addition to application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID903ApplicationMarkersTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	@Before
	public void createApplication() {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, false, true, true);
	}
	
	@Test
	public void testAddMarkers() {
		TreeItem project = new ProjectExplorer().getProject(applicationName).getTreeItem();
		markersTest(new ProjectExplorer(), project, applicationName, 
				OpenShiftLabel.ContextMenu.CONFIGURE_MARKERS);
	}
	
	public static void markersTest(View viewOfItem, TreeItem itemToHandle, String applicationName,
			String... contextMenuPath) {
		List<String> markerFiles = new ArrayList<String>();
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MARKERS + applicationName),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.MARKERS + applicationName);
		
		for (TableItem item: new DefaultTable().getItems()) {
			item.setChecked(true);
			markerFiles.add(item.getText(1));
		}
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MARKERS + applicationName),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		ResourceNavigator navigator = new ResourceNavigator();
		navigator.open();
		Project eapProject = null;
		for (Project project: navigator.getProjects()) {
			if (project.getText().contains(applicationName)) {
				eapProject = project;
				break;
			}
		}
		
		for (String marker: markerFiles) {
			TreeItem markerItem = getMarkerItem(eapProject, marker);
			assertTrue("Marker " + marker + " has not been created for the application " + applicationName,
					markerItem != null);
			deleteItem(markerItem);
		}
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MARKERS + applicationName),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.MARKERS + applicationName);
		
		for (TableItem item: new DefaultTable().getItems()) {
			assertFalse("Marker " + item.getText() + " should be unchecked because "
					+ "file has been deleted.", item.isChecked());
		}
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MARKERS + applicationName),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private static TreeItem getMarkerItem(Project project, String marker) {
		for (TreeItem openshiftItem: project.getTreeItem().getItems()) {
			if (openshiftItem.getText().contains(".openshift")) {
				for (TreeItem markersItem: openshiftItem.getItems()) {
					if (markersItem.getText().contains("markers")) {
						for (TreeItem markerItem: markersItem.getItems()) {
							if (markerItem.getText().contains(marker)) {
								return markerItem;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private static void deleteItem(TreeItem item) {
		item.select();
	    new ContextMenu("Delete").select();
	    new DefaultShell("Delete Resources");
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Delete Resources"),TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}

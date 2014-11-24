package org.jboss.tools.hibernate.reddeer.importer;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.hibernate.factory.ResourceFactory;

/**
 * Project importer class provides handy methods to import project from
 * resources for further utilization by hibernate tests
 * 
 * @author Jiri Peterka
 *
 */
public class ProjectImporter {

	/**
	 * Import porject and requires no errors in problems log
	 * @param pluginId plug-in id of project where project resources are located
	 * @param projectName project name to import 
	 */
	public static void importProjectWithoutErrors(String pluginId, String projectName) {
		importProject(pluginId, projectName);
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		assertTrue("No problems after import are expected", problemsView.getAllErrors().size() == 0);
	}
	
	/**
	 * Import project
	 * @param pluginId plug-in id of project where project resources are located
	 * @param projectName project name to import 
	 */
	public static void importProject(String pluginId, String prjName) {
		System.out.println("Import projects");
		ExternalProjectImportWizardDialog w = new ExternalProjectImportWizardDialog();
		w.open();
		WizardProjectsImportPage p1 = w.getFirstPage();
		p1.setRootDirectory(ResourceFactory.getResourcesLocation(pluginId, "prj"));
		p1.copyProjectsIntoWorkspace(true);
		p1.deselectAllProjects();
		p1.selectProjects(prjName);
		w.finish();
	}

	
}

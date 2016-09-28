package org.jboss.tools.hibernate.reddeer.importer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.common.reddeer.utils.ProjectHelper;
import org.jboss.tools.hibernate.reddeer.factory.ResourceFactory;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;

/**
 * Project importer class provides handy methods to import project from
 * resources for further utilization by hibernate tests
 * 
 * @author Jiri Peterka
 *
 */
public class ProjectImporter {

	private static final String LIB_DIR="target/requirements/";
	private static final Logger log = Logger.getLogger(ProjectImporter.class);
	
	/**
	 * Import porject and requires no errors in problems log
	 * @param pluginId plug-in id of project where project resources are located
	 * @param projectName project name to import 
	 */
	public static void importProjectWithoutErrors(String pluginId, String projectName, Map<String, String>libraryPathMap) {
		
		LogView logView = new LogView();
		logView.open();
		logView.deleteLog();
		
		importProject(pluginId, projectName);
		if(libraryPathMap != null){
			Map<String,String> fullPathJars = new HashMap<>();
			for(String jar: libraryPathMap.keySet()){
				if(libraryPathMap.get(jar) == null){
					fullPathJars.put(jar,LIB_DIR);
				} else {
					fullPathJars.put(jar,LIB_DIR+libraryPathMap.get(jar));
				}
			}
			ProjectHelper.addLibrariesIntoProject(projectName, fullPathJars);
		}
		new WaitUntil(new JobIsRunning(), TimePeriod.SHORT,false);
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		new WaitWhile(new JobIsRunning());
		
		List<Problem> problems = problemsView.getProblems(ProblemType.ERROR);
		for (Problem p : problems) {
			log.error("Unexpected "+ problems.size() + " problem(s):");
			log.dump("Problem: "+ p.toString());
		}
		
		assertTrue("No problems after import are expected", problems.size() == 0);
	}
	
	/**
	 * Import project
	 * @param pluginId plug-in id of project where project resources are located
	 * @param projectName project name to import 
	 */
	public static void importProject(String pluginId, String prjName) {
		ExternalProjectImportWizardDialog w = new ExternalProjectImportWizardDialog();
		w.open();
		WizardProjectsImportPage p1 = new WizardProjectsImportPage();
		p1.setRootDirectory(ResourceFactory.getResourcesLocation(pluginId, "prj"));
		p1.copyProjectsIntoWorkspace(true);
		p1.deselectAllProjects();
		p1.selectProjects(prjName);
		w.finish();
	}
	
	public static void importMavenProject(String pomPath) throws IOException {
		MavenImportWizard importWizard = new MavenImportWizard();
		importWizard.open();
		new MavenImportWizardFirstPage().importProject((new File(pomPath)).getCanonicalPath(),false);
	}

	
}

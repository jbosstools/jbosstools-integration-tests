/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.DefaultProject;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.eclipse.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.batch.reddeer.wizard.NewJobXMLFileWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewJobXMLFileWizardPage;

public abstract class AbstractBatchTest {

	private static final String PROJECT_NAME = "batch-test-project";
	
	protected static final String JAVA_RESOURCES = "Java Resources";

	protected static final String JAVA_FOLDER = "src/main/java";
	
	protected static final String RESOURCES_FOLDER = "src/main/resources";

	protected static final String META_INF_FOLDER = "META-INF";

	protected static final String BATCH_XML_FILE = "batch.xml";

	protected static final String[] BATCH_XML_FILE_FULL_PATH = new String[]{ JAVA_RESOURCES, RESOURCES_FOLDER, META_INF_FOLDER, BATCH_XML_FILE};

	protected static final String JOB_FILES_FOLDER = "batch-jobs";
	
	protected static final String JOB_XML_FILE = "batch-test-job.xml";
	
	protected static final String JOB_ID = "batch-test";

	protected static final String[] JOB_XML_FILE_FULL_PATH = new String[]{ JAVA_RESOURCES, RESOURCES_FOLDER, META_INF_FOLDER, JOB_FILES_FOLDER, JOB_XML_FILE};

	private static final Logger log = Logger.getLogger(AbstractBatchTest.class);
	
	/**
	 * Abstract method for retrieving package name
	 * @return string representing package
	 */
	protected abstract String getPackage();
	
	/**
	 * Returns actual project object base on PROJECT_NAME constant
	 * @return Project object of actual project
	 */
	protected static DefaultProject getProject() {
		ProjectExplorer explorer = new ProjectExplorer();
		explorer.open();
		return explorer.getProject(getProjectName());
	}
	
	protected static String getProjectName() {
		return PROJECT_NAME;
	}
	
	/**
	 * Test environment initialization, imports batch test project and creates
	 * its job XML configuration file.
	 * @param log class logger from which method was called
	 */
	protected static void initTestResources(Logger log, String projectPath) {
		log.info("Import archive project " + projectPath);
		log.info("Name of the project " + PROJECT_NAME);
		importProject(projectPath);
		new WaitWhile(new JobIsRunning());
		getProject().select();
		log.info("Create empty batch-job xml file");
		createJobXMLFile(JOB_ID);
	}
	
	/**
	 * Imports zip test project
	 */
	private static void importProject(String projectPath) {
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		
		WizardProjectsImportPage page = new WizardProjectsImportPage(dialog);
		page.setArchiveFile(Activator.getPathToFileWithinPlugin(projectPath));
		page.selectProjects(getProjectName());
		
		dialog.finish(TimePeriod.VERY_LONG);
	}
	
	/**
	 * Removes project from Project Explorer
	 * @param log class logger from which method was called
	 */
	protected static void removeProject(Logger log) {
		log.info("Removing " + getProjectName());
		// temporary workaround until upstream patch is applied (eclipse bug 478634)
		try {
			org.eclipse.reddeer.direct.project.Project.delete(getProjectName(), true, true);
		} catch (RuntimeException exc) {
			log.error("RuntimeException occured during deleting project");
			exc.printStackTrace();
			log.info("Deleting project second time ...");
			org.eclipse.reddeer.direct.project.Project.delete(getProjectName(), true, true);
		} 
		new WaitWhile(new JobIsRunning());
	}
	
	protected void setupJobXML() {
		if (!getProject().containsResource(JOB_XML_FILE_FULL_PATH)) {
			getProject().select();
			createJobXMLFile(JOB_ID);
		}		
	}
	
	protected void removeJobXML() {
		if (getProject().containsResource(JOB_XML_FILE_FULL_PATH)) {
			getProject().getProjectItem(JOB_XML_FILE_FULL_PATH).delete();
		}		
	}
	
	/**
	 * Provide bundle resource absolute path
	 * @param pluginId - plugin id
	 * @param path - resource relative path
	 * @return resource absolute path
	 */
	public static String getResourceAbsolutePath(String pluginId, String... path) {

		// Construct path
		StringBuilder builder = new StringBuilder();
		for (String fragment : path) {
			builder.append("/" + fragment);
		}

		String filePath = "";
		try {
			filePath = FileLocator.toFileURL(
					Platform.getBundle(pluginId).getEntry("/")).getFile()
					+ "resources" + builder.toString();
			File file = new File(filePath);
			if (!file.isFile()) {
				filePath = FileLocator.toFileURL(
						Platform.getBundle(pluginId).getEntry("/")).getFile()
						+ builder.toString();
			}
		} catch (IOException ex) {
			String message = filePath + " resource file not found";
			//log.error(message);
			fail(message);
		}

		return filePath;
	}
	
	/**
	 * Read test file to string
	 * @param filePath file path
	 * @return content of the file
	 * @throws IOException
	 */
	public String readTextFileToString(String filePath) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);				
				line = br.readLine();
				if (line != null)
					sb.append("\n");
			}
			String everything = sb.toString();
			return everything;
		} finally {
			br.close();
		}
	}
	
	/**
	 * Creates job XML in actual selected project
	 * @param jobID id of the job XML file
	 */
	protected static void createJobXMLFile(String jobID) {
		NewJobXMLFileWizardDialog dialog = new NewJobXMLFileWizardDialog();
		dialog.open();
		
		NewJobXMLFileWizardPage page = new NewJobXMLFileWizardPage(dialog);
		page.setFileName(JOB_XML_FILE);
		page.setJobID(jobID);
		
		dialog.finish();
	}

	public static void assertNoProblems() {
		log.step("Assert there are no problems");
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		
		new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);
		
		List<Problem> problems = null;
		try {
			problems = problemsView.getProblems(ProblemType.ALL);
			assertThat(problems.size(), is(0));
		} catch (AssertionError e){
			String message = "Found unexpected problems\n";
			for (Problem problem : problems){
				message += "\t" + problem.getProblemType() + ": " + problem.getDescription() +
						"(" + problem.getResource() + " at line " + problem.getLocation() + ")\n";
			}
			throw new AssertionError(message, e);
		}
	}
	
	public static void assertNumberOfProblems(int error, int warning) {
		log.step("Assert there are no problems");
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		
		new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);
		
		List<Problem> problems = null;
		try {
			problems = problemsView.getProblems(ProblemType.ERROR);
			assertThat(getUniqueProblemsSize(problems), is(error));
			
			problems = problemsView.getProblems(ProblemType.WARNING);
			assertThat(getUniqueProblemsSize(problems), is(warning));
		} catch (AssertionError e){
			String message = "Found unexpected problems\n";
			for (Problem problem : problems){
				message += "\t" + problem.getProblemType() + ": " + problem.getDescription() + 
						"(" + problem.getResource() + " at line " + problem.getLocation() + ")\n";
			}
			throw new AssertionError(message, e);
		}
	}
	
	private static int getUniqueProblemsSize(List<Problem> problems) {
		List<Problem> uniqueProblems = new ArrayList<Problem>();
		for (Problem problem : problems) {
			boolean contains = false;
			for (Problem item : uniqueProblems) {
				if (isSameProblem(problem, item)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				uniqueProblems.add(problem);
			}
		}
		return uniqueProblems.size();
	} 
	
	private static boolean isSameProblem(Problem x, Problem y) {
		return x.getType().equals(y.getType()) && 
				x.getResource().equals(y.getResource()) && 
				x.getDescription().equals(y.getDescription()) &&
				x.getLocation().equals(y.getLocation());
	}
}

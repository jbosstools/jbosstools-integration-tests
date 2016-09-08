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

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.batch.reddeer.wizard.NewJobXMLFileWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewJobXMLFileWizardPage;

public abstract class AbstractBatchTest {

	protected static final String PROJECT_NAME = "batch-test-project";

	protected static final String JAVA_FOLDER = "src/main/java";
	
	protected static final String RESOURCES_FOLDER = "src/main/resources";

	protected static final String META_INF_FOLDER = "META-INF";

	protected static final String BATCH_XML_FILE = "batch.xml";

	protected static final String[] BATCH_XML_FILE_FULL_PATH = new String[]{RESOURCES_FOLDER, META_INF_FOLDER, BATCH_XML_FILE};

	protected static final String JOB_FILES_FOLDER = "batch-jobs";
	
	protected static final String JOB_XML_FILE = "batch-test-job.xml";
	
	protected static final String JOB_ID = "batch-test";

	protected static final String[] JOB_XML_FILE_FULL_PATH = new String[]{RESOURCES_FOLDER, META_INF_FOLDER, JOB_FILES_FOLDER, JOB_XML_FILE};

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
	protected static Project getProject(){
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		return explorer.getProject(PROJECT_NAME);
	}
	
	/**
	 * Method for test classes initialization, imports batch test project and creates
	 * its job XML configuration file.
	 * @param log class logger from which method was called
	 */
	protected static void initTestResources(Logger log) {
		log.info("Import archive project " + PROJECT_NAME);
		importProject();
		new WaitWhile(new JobIsRunning());
		getProject().select();
	}
	
	/**
	 * Imports zip test project
	 */
	private static void importProject() {
		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		
		WizardProjectsImportPage page = new WizardProjectsImportPage();
		page.setArchiveFile(Activator.getPathToFileWithinPlugin("projects/batch-test-project.zip"));
		page.selectProjects(PROJECT_NAME);
		
		dialog.finish(TimePeriod.VERY_LONG);
	}
	
	/**
	 * Removes project from Project Explorer
	 * @param log class logger from which method was called
	 */
	protected static void removeProject(Logger log) {
		log.info("Removing " + PROJECT_NAME);
		getProject().delete(true);
		new WaitWhile(new JobIsRunning());
	}
	
	/**
	 * Creates job XML in actual selected project
	 * @param jobID id of the job XML file
	 */
	protected static void createJobXMLFile(String jobID) {
		NewJobXMLFileWizardDialog dialog = new NewJobXMLFileWizardDialog();
		dialog.open();
		
		NewJobXMLFileWizardPage page = new NewJobXMLFileWizardPage();
		page.setFileName(JOB_XML_FILE);
		page.setJobID(jobID);
		
		dialog.finish();
	}

	protected void assertNoProblems() {
		log.step("Assert there are no problems");
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		
		List<Problem> problems = null;
		try {
			problems = problemsView.getProblems(ProblemType.ANY);
			assertThat(getUniqueProblemsSize(problems), is(0));
		} catch (AssertionError e){
			String message = "Found unexpected problems\n";
			for (Problem problem : problems){
				message += "\t" + problem.getProblemType() + ": " + problem.getDescription() + "(" + problem.getResource() + ")\n";
			}
			throw new AssertionError(message, e);
		}
	}
	
	protected void assertNumberOfProblems(int error, int warning) {
		log.step("Assert there are no problems");
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		
		// TODO Fix the hard-coded wait
		AbstractWait.sleep(TimePeriod.NORMAL);
		
		List<Problem> problems = null;
		try {
			problems = problemsView.getProblems(ProblemType.ERROR);
			assertThat(getUniqueProblemsSize(problems), is(error));
			
			problems = problemsView.getProblems(ProblemType.WARNING);
			assertThat(getUniqueProblemsSize(problems), is(warning));
		} catch (AssertionError e){
			String message = "Found unexpected problems\n";
			for (Problem problem : problems){
				message += "\t" + problem.getProblemType() + ": " + problem.getDescription() + "(" + problem.getResource() + ")\n";
			}
			throw new AssertionError(message, e);
		}
	}
	
	private int getUniqueProblemsSize(List<Problem> problems) {
		List<Problem> uniqueProblems = new ArrayList<Problem>();
		for (Problem problem : problems) {
			boolean contains = false;
			// hardcoded way how to find out that custom class object is in list
			// TODO implement necessary methods into Reddeer's problem class (equals, hashcode)
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
	
	private boolean isSameProblem(Problem x, Problem y) {
		return x.getType().equals(y.getType()) && 
				x.getResource().equals(y.getResource()) && 
				x.getDescription().equals(y.getDescription()) &&
				x.getLocation().equals(y.getLocation());
	}
}

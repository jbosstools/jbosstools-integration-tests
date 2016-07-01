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

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class AbstractBatchTest {

	protected static final String PROJECT_NAME = "batch-test-project";

	protected static final String JAVA_FOLDER = "src/main/java";
	
	protected static final String RESOURCES_FOLDER = "src/main/resources";

	protected static final String META_INF_FOLDER = "META-INF";

	protected static final String BATCH_XML_FILE = "batch.xml";

	protected static final String[] BATCH_XML_FILE_FULL_PATH = new String[]{RESOURCES_FOLDER, META_INF_FOLDER, BATCH_XML_FILE};

	protected static final String JOB_FILES_FOLDER = "batch-jobs";
	
	protected static final String JOB_XML_FILE = "batch-test-job.xml";

	protected static final String[] JOB_XML_FILE_FULL_PATH = new String[]{RESOURCES_FOLDER, META_INF_FOLDER, JOB_FILES_FOLDER, JOB_XML_FILE};

	private static final Logger log = Logger.getLogger(AbstractBatchTest.class);
	
	/**
	 * Abstract method for retrieving package name
	 * @return string representing package
	 */
	protected abstract String getPackage();
	
	protected Project getProject(){
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		return explorer.getProject(PROJECT_NAME);
	}

	protected void assertNoProblems() {
		log.step("Assert there are no problems");
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		
		List<Problem> problems = null;
		try {
			problems = problemsView.getProblems(ProblemType.ANY);
			assertThat(problems.size(), is(0));
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
			assertThat(problems.size(), is(error));
			
			problems = problemsView.getProblems(ProblemType.WARNING);
			assertThat(problems.size(), is(warning));
		} catch (AssertionError e){
			String message = "Found unexpected problems\n";
			for (Problem problem : problems){
				message += "\t" + problem.getProblemType() + ": " + problem.getDescription() + "(" + problem.getResource() + ")\n";
			}
			throw new AssertionError(message, e);
		}
	}
}

package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractBatchTest {

	protected static final String PROJECT_NAME = "batch-test-project";
	
	protected static final String RESOURCES_SOURCE_FOLDER = "src/main/resources";
	
	protected static final String META_INF_FOLDER = "META-INF";
	
	protected static final String BATCH_FILES_FOLDER = "batch-jobs";
	
	protected static final String BATCH_XML_FILE = "batch-test-job.xml";
	
	protected static final String[] BATCH_XML_FILE_FULL_PATH = new String[]{RESOURCES_SOURCE_FOLDER, META_INF_FOLDER, BATCH_FILES_FOLDER, BATCH_XML_FILE};
	
	protected Project getProject(){
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		return explorer.getProject(PROJECT_NAME);
	}
	
	protected void assertNoProblems() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		assertThat(problemsView.getProblems(ProblemType.ANY).size(), is(0));
	}
}

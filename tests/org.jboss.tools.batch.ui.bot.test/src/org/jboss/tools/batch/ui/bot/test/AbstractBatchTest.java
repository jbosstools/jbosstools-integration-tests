package org.jboss.tools.batch.ui.bot.test;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
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

	private static final Logger log = Logger.getLogger(AbstractBatchTest.class);
	
	protected Project getProject(){
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		return explorer.getProject(PROJECT_NAME);
	}

	protected void assertNoProblems() {
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
}

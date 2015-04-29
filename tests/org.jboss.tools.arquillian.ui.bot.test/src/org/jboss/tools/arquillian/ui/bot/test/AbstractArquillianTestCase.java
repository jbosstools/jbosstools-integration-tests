package org.jboss.tools.arquillian.ui.bot.test;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.tools.arquillian.ui.bot.reddeer.maven.UpdateMavenProjectDialog;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Contains common methods for Arquillian tests. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractArquillianTestCase {

	protected static final String PROJECT_NAME = "arquillian-test-project";
	
	public static final String PROFILE_NAME = "WILDFLY_REMOTE_8.X";
	
	protected static final String PACKAGE = "org.jboss.tools.arquillian.ui.test";
	
	protected static final String TEST_CASE = "ArquillianTest";
	
	private static final Logger log = Logger.getLogger(AbstractArquillianTestCase.class);
	
	protected Project getProject() {
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();
		Project project = explorer.getProject(PROJECT_NAME);
		return project;
	}
	
	protected void checkProblems() {
		log.step("Check errors in Problems view");
		ProblemsView view = new ProblemsView();
		view.open();
		
		List<Problem> problems = view.getProblems(ProblemType.ERROR);
		assertThat("There are errors", problems, is((List) new ArrayList<Problem>()));
	}
	
	protected void forceMavenRepositoryUpdate() {
		log.step("Force Maven update snapshots/releases");
		UpdateMavenProjectDialog dialog = new UpdateMavenProjectDialog();
		dialog.open(getProject());
		dialog.forceUpdate(true);
		dialog.ok();
	}
}

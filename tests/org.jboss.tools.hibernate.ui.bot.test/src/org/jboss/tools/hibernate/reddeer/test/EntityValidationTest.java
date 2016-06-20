package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ProblemExists;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests checks if entity validation is working
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class EntityValidationTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "mvn-jpa21-hibernate43";
	private static final Logger log = Logger.getLogger(EntityValidationTest.class);
		
	@Before 
	public void prepare() {
		log.step("Import test project");
		importMavenProject(PROJECT_NAME);
	}
	
	@After 
	public void clean() {			
		deleteAllProjects();
	}

	
	@Test
	public void embeddedEntityValidationTest() {		
		buildProject();
		log.step("Check problems view for no errors");
		ProblemsView pv = new ProblemsView();
		pv.open();

		List<Problem> problems = pv.getProblems(ProblemType.ERROR);
		assertTrue(problems.isEmpty());
		
		log.step("Delete embedded entity Address.java");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("src/main/java","org.hibernate.ui.test.model","Address.java").delete();
		buildProject();
		
		pv.activate();
		String expectedProblem = "org.hibernate.ui.test.model.Address is not mapped as an embeddable";
		new WaitUntil(new ProblemExists(ProblemType.ERROR, new ProblemsDescriptionMatcher(expectedProblem)));
		problems = pv.getProblems(ProblemType.ERROR, new ProblemsDescriptionMatcher(expectedProblem));
		assertTrue(expectedProblem + " error is expected", problems.size() == 2);
	}
			
	@Test
	public void userIdentifierGeneratorValidationTest() {		
		buildProject();
		log.step("Check problems view for no errors");
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> problems = pv.getProblems(ProblemType.ERROR);
		assertTrue(problems.isEmpty());
		
		log.step("Delete generator UserIdGenerator.java");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("src/main/java","org.hibernate.ui.test.model","UserIdGenerator.java").delete();
		buildProject();
		
		pv.activate();
		String expectedProblem = "Strategy class \"org.hibernate.ui.test.model.UserIdGenerator\" could not be found.";
		problems = pv.getProblems(ProblemType.ERROR, new ProblemsDescriptionMatcher(expectedProblem));
		assertTrue(expectedProblem + " Error is expected, known issue(s):JBIDE-19526", problems.size() == 2);
	}
	
	private void buildProject(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		new ShellMenu("Project","Build Project").select();
		new WaitWhile(new JobIsRunning());
	}
}

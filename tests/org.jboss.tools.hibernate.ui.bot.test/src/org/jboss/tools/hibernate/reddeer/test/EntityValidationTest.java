package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
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
public class EntityValidationTest extends HibernateRedDeerTest {

	private String PROJECT_NAME = "mvn-jpa21-hibernate43";
	private static final Logger log = Logger.getLogger(EntityValidationTest.class);
		
	@Before 
	public void prepare() {
		log.step("Import test project");
		importProject(PROJECT_NAME);
	}
	
	@Test
	public void embeddedEntityValidationTest() {		
		
		log.step("Check problems view for no errors");
		ProblemsView pv = new ProblemsView();
		pv.open();

		List<Problem> problems = pv.getProblems(ProblemType.ERROR);
		assertTrue(problems.isEmpty());
		
		log.step("Delete embedded entity Address.java");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem item = new DefaultTreeItem(PROJECT_NAME,"src/main/java","org.hibernate.ui.test.model","Address.java");
		item.select();
		new ContextMenu("Delete").select();
		new WaitUntil(new ShellWithTextIsActive("Delete"));
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Delete"));
		new WaitWhile(new JobIsRunning());

		pv.activate();
		String expectedProblem = "org.hibernate.ui.test.model.Address is not mapped as an embeddable";
		problems = pv.getProblems(ProblemType.ERROR, new ProblemsDescriptionMatcher(expectedProblem));
		assertTrue(expectedProblem + " error is expected", problems.size() == 2);
	}
			
	@Test
	public void userIdentifierGeneratorValidationTest() {		
		
		log.step("Check problems view for no errors");
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> problems = pv.getProblems(ProblemType.ERROR);
		assertTrue(problems.isEmpty());
		
		log.step("Delete generator UserIdGenerator.java");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem item = new DefaultTreeItem(PROJECT_NAME,"src/main/java","org.hibernate.ui.test.model","UserIdGenerator.java");
		item.select();
		new ContextMenu("Delete").select();
		new WaitUntil(new ShellWithTextIsActive("Delete"));
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Delete"));
		new WaitWhile(new JobIsRunning());

		pv.activate();
		String expectedProblem = "Strategy class \"org.hibernate.ui.test.model.UserIdGenerator\" could not be found.";
		problems = pv.getProblems(ProblemType.ERROR, new ProblemsDescriptionMatcher(expectedProblem));
		assertTrue(expectedProblem + " error is expected, known issue(s):", problems.size() == 2);
	}
	
	@After 
	public void clean() {			
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).delete(true);
	}

}

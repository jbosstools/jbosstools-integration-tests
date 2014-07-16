package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.hibernate.reddeer.wizard.JPAFacetWizardPage;
import org.jboss.tools.hibernate.reddeer.wizard.JPAProjectWizard;
import org.jboss.tools.hibernate.reddeer.wizard.JPAProjectWizardFirstPage;
import org.jboss.tools.hibernate.reddeer.wizard.JpaImplementation;
import org.jboss.tools.hibernate.reddeer.wizard.JpaPlatform;
import org.jboss.tools.hibernate.reddeer.wizard.JpaVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Create JPA Project test
 * 
 * @author Jiri Peterka
 * 
 */
@RunWith(RedDeerSuite.class)
public class CreateJPAProjectTest extends HibernateRedDeerTest {

	final String PROJECT_NAME = "jpa35test";
	
	@Before 
	public void before() {
		CleanWorkspaceRequirement req = new CleanWorkspaceRequirement();
		req.fulfill();
	}

	@Test
	public void createJPAProject10() {
		createProject(JpaVersion.JPA10, JpaPlatform.HIBERNATE10);
	}

	@Test
	public void createJPAProject20() {
		createProject(JpaVersion.JPA20, JpaPlatform.HIBERNATE20);
	}

	@Test
	public void createJPAProject21() {
		createProject(JpaVersion.JPA21, JpaPlatform.HIBERNATE21);
	}

	private void createProject(JpaVersion version, JpaPlatform platform) {

		JPAProjectWizard wizard = new JPAProjectWizard();
		wizard.open();

		JPAProjectWizardFirstPage firstPage = new JPAProjectWizardFirstPage();
		firstPage.setProjectName(PROJECT_NAME);
		firstPage.setJPAVersion(version);

		wizard.next();
		wizard.next();

		JPAFacetWizardPage facetPage = new JPAFacetWizardPage();
		facetPage.setPlatform(platform);
		facetPage
				.setJpaImplementation(JpaImplementation.DISABLE_LIBRARY_CONFIGURATION);

		wizard.finish();

		new WaitWhile(new JobIsRunning());
		ProblemsView problemsView = new ProblemsView();
		List<TreeItem> allErrors = problemsView.getAllErrors();
		assertTrue("No problems are expected (JBIDE-17855)", allErrors.size() == 0);
	}
	
	@After
	public void cleanup() {
		// nothing
	}
}

package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Mavenized project tests that hibernate project can be
 * imported without errors
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@CleanWorkspace
public class MavenizedProjectTest extends HibernateRedDeerTest {

	@Test
	public void testHibernateMavenProject35() {
		importHibernateMavenProject("mvn-hibernate35");
	}

	@Test
	public void testHibernateMavenProject36() {
		importHibernateMavenProject("mvn-hibernate36");
	}

	@Test
	public void testHibernateMavenProject40() {
		importHibernateMavenProject("mvn-hibernate40");
	}

	@Test
	public void testHibernateMavenProject43() {
		importHibernateMavenProject("mvn-hibernate43");
	}
	
	private void importHibernateMavenProject(String projectName) {
		importProject(projectName);
	}
}

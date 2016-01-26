package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.common.logging.Logger;
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
public class MavenizedProjectTest extends HibernateRedDeerTest {

	private static final Logger log = Logger.getLogger(MavenizedProjectTest.class);
	
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
	
	@Test
	public void testHibernateMavenProject50() {
		importHibernateMavenProject("mvn-hibernate50");
	}
	
	private void importHibernateMavenProject(String projectName) {
		log.step("Import mavenized test project" + projectName);
		importMavenProject(projectName);
	}
}

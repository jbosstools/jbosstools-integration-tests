package org.jboss.tools.forge.ui.bot.test;

import java.io.IOException;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;
/**
 * 
 * @author psrna
 *
 */
@Require(clearWorkspace=true)
public class PersistenceTest extends ForgeTest {

	@Test
	public void hibernateJBossAS7(){
		
		createProject(ProjectTypes.jar);
		
		getStyledText().setText("persistence setup\n");
		getStyledText().setText("HIBERNATE\n");
		getStyledText().setText("JBOSS_AS7\n");
		getStyledText().setText("N\n");
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole("persistence.xml", TIME_1S, TIME_20S*3));
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		try {
			String pContent = ResourceUtils.readFile(projectLocation + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.hibernate.ejb.HibernatePersistence</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
			assertTrue(pContent.contains("<property name=\"hibernate.hbm2ddl.auto\" value=\"create-drop\"/>"));
			assertTrue(pContent.contains("<property name=\"hibernate.show_sql\" value=\"true\"/>"));
			assertTrue(pContent.contains("<property name=\"hibernate.format_sql\" value=\"true\"/>"));
			assertTrue(pContent.contains("<property name=\"hibernate.transaction.flush_before_completion\" value=\"true\"/>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();
	}
	
	@Test
	public void openjpaJBossAS7(){
		createProject(ProjectTypes.jar);
		
		getStyledText().setText("persistence setup\n");
		getStyledText().setText("OPENJPA\n");
		getStyledText().setText("JBOSS_AS7\n");
		getStyledText().setText("N\n");
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole("persistence.xml", TIME_1S, TIME_20S*3));
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		try {
			String pContent = ResourceUtils.readFile(projectLocation + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.apache.openjpa.persistence.PersistenceProviderImpl</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();
	}
	
	@Test
	public void eclipselinkJBossAS7(){
		createProject(ProjectTypes.jar);
		
		getStyledText().setText("persistence setup\n");
		getStyledText().setText("ECLIPSELINK\n");
		getStyledText().setText("JBOSS_AS7\n");
		getStyledText().setText("N\n");
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole("persistence.xml", TIME_1S, TIME_20S*3));
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		try {
			String pContent = ResourceUtils.readFile(projectLocation + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
			assertTrue(pContent.contains("<property name=\"eclipselink.ddl-generation\" value=\"drop-and-create-tables\"/>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();
	}
	
	@Test
	public void infinispanJBossAS7(){
		createProject(ProjectTypes.jar);
		
		getStyledText().setText("persistence setup\n");
		getStyledText().setText("INFINISPAN\n");
		getStyledText().setText("JBOSS_AS7\n");
		getStyledText().setText("N\n");
		
		assertTrue(ConsoleUtils.waitUntilTextInConsole("persistence.xml", TIME_1S, TIME_20S*3));
		
		String projectLocation = SWTUtilExt.getPathToProject(PROJECT_NAME);
		try {
			String pContent = ResourceUtils.readFile(projectLocation + "/src/main/resources/META-INF/persistence.xml");
			assertTrue(pContent.contains("<provider>org.hibernate.ogm.HibernateOgmPersistence</provider>"));
			assertTrue(pContent.contains("<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>"));
			assertTrue(pContent.contains("<property name=\"hibernate.dialect\" value=\"org.hibernate.ogm.dialect.NoopDialect\"/>"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the 'persistence.xml' file failed!");
		}
		getStyledText().setText("cd ..\n");
		clear();
		pExplorer.deleteAllProjects();
	}
}

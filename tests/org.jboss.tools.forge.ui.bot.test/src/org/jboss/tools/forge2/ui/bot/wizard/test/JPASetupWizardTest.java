package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.junit.Test;

public class JPASetupWizardTest extends WizardTestBase {
	
	@Test
	public void testPersistenceXmlCreated(){
		persistenceSetup(PROJECT_NAME);
		ProjectExplorer pe = new ProjectExplorer();
		assertTrue("persistence.xml not found in project explorer", 
					pe.getProject(PROJECT_NAME).containsItem("src", "main", "resources", "META-INF", "persistence.xml"));
		
	}
	
	@Test
	public void testPersistenceOpenedInEditor(){
		persistenceSetup(PROJECT_NAME);
		DefaultEditor e = new DefaultEditor();
		assertTrue("Persistence editor is not active", e.isActive());
		assertTrue(e.getTitle().equals("persistence.xml"));
		e.close();
	}
	
	@Test
	public void testPersistenceHasRightContent(){
		persistenceSetup(PROJECT_NAME);
		try {
			String pContent = ResourceUtils.readFile(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
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
	}
}

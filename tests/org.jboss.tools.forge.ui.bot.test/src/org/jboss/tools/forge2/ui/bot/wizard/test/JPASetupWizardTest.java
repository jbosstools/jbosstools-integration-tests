package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Test;

public class JPASetupWizardTest extends WizardTestBase {
	
	@Test
	public void testPersistenceXmlCreated(){
		newProject(PROJECT_NAME);
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(PROJECT_NAME); //this will set context for forge
		WizardDialog wd = getWizardDialog("jpa-setup", "(JPA: Setup).*");
		wd.finish();
		File persistence = new File(WORKSPACE + "/" + PROJECT_NAME + "/src/main/resources/META-INF/persistence.xml");
		assertTrue("persistence.xml file does not exist", persistence.exists());
		assertTrue("persistence.xml not found in project explorer", 
					pe.getProject(PROJECT_NAME).containsItem("src", "main", "resources", "META-INF", "persistence.xml"));
		
	}
	
	@Test
	public void testPersistenceOpenedInEditor(){
		newProject(PROJECT_NAME);
		ProjectExplorer pe = new ProjectExplorer();
		pe.selectProjects(PROJECT_NAME); //this will set context for forge
		WizardDialog wd = getWizardDialog("jpa-setup", "(JPA: Setup).*");
		wd.finish();
		
		DefaultEditor e = new DefaultEditor();
		assertTrue("Persistence editor is not active", e.isActive());
		assertTrue(e.getTitle().equals("persistence.xml"));
		e.close();
	}

}

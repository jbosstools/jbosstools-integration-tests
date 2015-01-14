package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.factory.DriverDefinitionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test prepares project and generate JPA entities from database 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class JPAEntityGenerationTest extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43"; 
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    @Before
	public void testConnectionProfile() {
    	importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);		
		ConnectionProfileFactory.createConnectionProfile(cfg);
		testSetJPAFacets();
	}
    
    
    private void testSetJPAFacets()
    {
    	DatabaseConfiguration cfg = dbRequirement.getConfiguration();
    	
    	// Convert to facets project
    	ProjectExplorer pe = new ProjectExplorer();
    	pe.open();
    	pe.selectProjects(PRJ);
    	new ContextMenu("Properties").select();
    	new WaitUntil(new ShellWithTextIsActive("Properties for " + PRJ));    	
    	new DefaultTreeItem("Project Facets").select();
    	new DefaultLink("Convert to faceted form...").click();
    	new WaitUntil(new ShellWithTextIsActive("Properties for " + PRJ));    	
    	PushButton apply = new PushButton("Apply");
    	new WaitUntil(new ButtonWithTextIsActive(apply));
    	apply.click();
    	new OkButton().click();
    	
    	// Set JPA Connection Profile
    	pe.open();
    	pe.selectProjects(PRJ);
    	new ContextMenu("Properties").select();
    	

    	new DefaultTreeItem("JPA").select();
    	DefaultGroup group = new DefaultGroup("Connection");
    	new WaitUntil(new WidgetIsEnabled(new DefaultCombo(group)));
    	new DefaultCombo(group).setSelection(cfg.getProfileName());;
    	
    	OkButton okButton = new OkButton();
    	okButton.click();
    	new WaitWhile(new ShellWithTextIsActive("Properties for " + PRJ));
    }
    
    @Test
    public void testEntityGeneration() {
    	DatabaseConfiguration cfg = dbRequirement.getConfiguration();
    	
    	ProjectExplorer pe = new ProjectExplorer();
    	pe.open();
    	pe.selectProjects(PRJ);
    	
    	new ContextMenu("JPA Tools","Generate Entities from Tables...").select();
    	new WaitUntil(new ShellWithTextIsActive("Generate Entities"));
    	CheckBox useConsole = new CheckBox("Use Console Configuration");
    	if (useConsole.isEnabled()) {
    		useConsole.click();
    	}
    	new LabeledText("Package:").setText("org.gen");
    	new LabeledCombo("Hibernate Version:").setSelection("4.3");
    	new LabeledCombo("Database Connection").setSelection(cfg.getProfileName());
    	
    	new FinishButton().click();
    	new WaitWhile(new JobIsRunning());
    	
    	pe.open();
    	pe.selectProjects(PRJ);
    	new DefaultTreeItem(PRJ,"src/main/java","org.gen","Actor.java").doubleClick();
    	new DefaultEditor("Actor.java");
    }
    
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}
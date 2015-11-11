package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.hibernate.reddeer.common.FileHelper;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.editor.CriteriaEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate Criteria Editor test for Code Assist 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CriteriaEditorCodeAssistTest extends HibernateRedDeerTest {

	private String prj = "mvn-hibernate43";
	private String hbVersion = "4.3";
	private String jpaVersion = "2.1"; 

	private static final Logger log = Logger.getLogger(CriteriaEditorCodeAssistTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;     

    @Test
    public void testCriteriaEditorCodeAssistMvn35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0");
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0");
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0");
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1");
    	testCriteriaEditorCodeAssistMvn();
    }
        
    @Test
    public void testCriteriaEditorCodeAssistEcl35() {
    	setParams("ecl-hibernate35-ent","3.5","2.0");
    	testCriteriaEditorCodeAssistEcl();
    }
 
    @Test
    public void testCriteriaEditorCodeAssistEcl36() {
    	setParams("ecl-hibernate36-ent","3.6","2.0");
    	testCriteriaEditorCodeAssistEcl();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistEcl40() {
    	setParams("ecl-hibernate40-ent","4.0","2.0");
    	testCriteriaEditorCodeAssistEcl();
    }
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }
    
	private void prepareMaven() {
		log.step("Import mavenized project " + prj);
    	importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create connection profile");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		
		log.step("Convert project into faceted form");
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA Facets");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		
		log.step("Open Hibernate Configurations View");
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		log.step("Add New Hibernate Configuration and set parameters");
		new ContextMenu("Add Configuration...").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"));
		DefaultGroup prjGroup = new DefaultGroup("Project:");
		new DefaultText(prjGroup).setText(prj);
		new RadioButton("JPA (jdk 1.5+)").click();
		DefaultGroup dbConnection = new DefaultGroup("Database connection:");
		new DefaultCombo(dbConnection,0).setText("[JPA Project Configured Connection]");
		new LabeledCombo("Hibernate Version:").setSelection(hbVersion);				
		new PushButton("Apply").click();
		log.step("Click OK to finish the dialog");
		new OkButton().click();
	}
    
    private void testCriteriaEditorCodeAssistEcl() {
    	prepareEclipseProject();
    	testCriteriaEditor();    	
    }
    
    private void prepareEclipseProject() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		String destDir = FileHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,"resources","prj","hibernatelib","connector" );
		try {
			FileHelper.copyFilesBinary(cfg.getDriverPath(), destDir);
		} catch (IOException e) {
			Assert.fail("Cannot copy db driver: " + e.getMessage());
		}
		log.step("Import java project for hibernate test");
    	importProject("hibernatelib");
    	importProject(prj);
    	
    	log.step("Create hibernate configuration file");
    	HibernateToolsFactory.testCreateConfigurationFile(cfg, prj, "hibernate.cfg.xml", false);
	}
	
	private void testCriteriaEditorCodeAssistMvn() {
		prepareMaven();
		testCriteriaEditor();		
	}
		
	private void testCriteriaEditor() {
		log.step("By Hibernate Console Configuration open Criteria Editor");
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.selectConsole(prj);
		new ContextMenu("Hibernate Criteria Editor").select();

		String expression = "ses";
		CriteriaEditor criteriaEditor = new CriteriaEditor(prj);
		criteriaEditor.setText(expression);
		criteriaEditor.setCursorPosition(expression.length());		
		String proposal = "session : Session";
		ContentAssistant ca = criteriaEditor.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		assertTrue(proposal + " is expected", proposals.contains(proposal));
		
		expression = "session.cre";
		criteriaEditor.setText(expression);
		criteriaEditor.setCursorPosition(expression.length());
		proposal = "createCriteria(Class arg0) : Criteria - Session";
		if (hbVersion.equals("4.3") || hbVersion.equals("4.0")) {
			proposal = "createCriteria(Class arg0) : Criteria - SharedSessionContract";
		}
		ca = criteriaEditor.openContentAssistant();
		proposals = ca.getProposals();
		ca.close();
		assertTrue(proposal + " is expected", proposals.contains(proposal));
		
		expression = "session.createCriteria(Act";
		criteriaEditor.setText(expression);
		criteriaEditor.setCursorPosition(expression.length());
		proposal = "Actor - org.gen";
		ca = criteriaEditor.openContentAssistant();
		proposals = ca.getProposals();
		ca.close();
		assertTrue(proposal + " is expected", proposals.contains(proposal));						
	}
   
	@After
	public void cleanUp() {
		ConnectionProfileFactory.deleteAllConnectionProfiles();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
}
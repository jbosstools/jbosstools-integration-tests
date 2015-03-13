package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.common.FileHelper;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.EntityGenerationFactory;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate Criteria Editor test 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CriteriaEditorTest extends HibernateRedDeerTest {

	private String prj = "mvn-hibernate43";
	private String hbVersion = "4.3";
	private String jpaVersion = "2.1"; 

	private Logger log = Logger.getLogger(CriteriaEditorTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
	private void prepareMaven() {
    	importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);		
		ConnectionProfileFactory.createConnectionProfile(cfg);
		
		log.info("Converting project into faceted form");
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		EntityGenerationFactory.generateJPAEntities(cfg,prj,"org.gen",hbVersion,true);
		
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		new ContextMenu("Add Configuration...").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"));
		DefaultGroup prjGroup = new DefaultGroup("Project:");
		new DefaultText(prjGroup).setText(prj);
		new RadioButton("JPA (jdk 1.5+)").click();
		DefaultGroup dbConnection = new DefaultGroup("Database connection:");
		new DefaultCombo(dbConnection,0).setText("[JPA Project Configured Connection]");
		new LabeledCombo("Hibernate Version:").setSelection(hbVersion);
		new PushButton("Apply").click();
		
		new OkButton().click();
	}
    

    @Test
    public void testCriteriaEditorMvn35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	testCriteriaEditorMvn();
    }
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }
    
    @Test
    public void testCriteriaEditorEcl35() {
    	setParams("ecl-hibernate35-ent","3.5","2.0");
    	testCriteriaEditorEcl();
    }
 
    @Test
    public void testCriteriaEditorEcl36() {
    	setParams("ecl-hibernate36-ent","3.5","2.0");
    	testCriteriaEditorEcl();
    }
    
    @Test
    public void testCriteriaEditorEcl40() {
    	setParams("ecl-hibernate40-ent","3.5","2.0");
    	testCriteriaEditorEcl();
    }
       
    private void testCriteriaEditorEcl() {
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
    	importProject("hibernatelib");
    	importProject(prj);
    	
    	HibernateToolsFactory.testCreateConfigurationFile(cfg, prj, "hibernate.cfg.xml", false);
	}
	
	private void testCriteriaEditorMvn() {
		prepareMaven();
		testCriteriaEditor();		
	}
		
	private void testCriteriaEditor() {
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.selectConsole(prj);
		new ContextMenu("Hibernate Criteria Editor").select();
		TextEditor criteriaEditor = new TextEditor("Criteria:" + prj);
		criteriaEditor.setText("session.createCriteria(Actor.class).list();");
		criteriaEditor.save();
	
		new DefaultToolItem("Run criteria").click();
		
		new WaitUntil(new ShellWithTextIsActive("Open Session factory"));
		new YesButton().click();
		
    	QueryPageTabView result = new QueryPageTabView();
    	result.open();	
    	assertTrue("Query result items expected", result.getResultItems().size() > 10);
	}

   
	@After
	public void cleanUp() {
		ConnectionProfileFactory.deleteAllConnectionProfiles();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
}
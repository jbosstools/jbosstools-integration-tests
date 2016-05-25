package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.editor.CriteriaEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate Criteria Editor test 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CriteriaEditorTest extends HibernateRedDeerTest {

	private String prj;
	private String hbVersion;
	private String jpaVersion; 
	private Map<String,String> libraryPathMap;

	private static final Logger log = Logger.getLogger(CriteriaEditorTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;     
    
    @After
	public void cleanUp() {
		ConnectionProfileFactory.deleteAllConnectionProfiles();
		deleteAllProjects();
	}

    @Test
    public void testCriteriaEditorMvn35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0", null);
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0", null);
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0", null);
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1", null);
    	testCriteriaEditorMvn();
    }
    
    @Test
    public void testCriteriaEditorMvn50() {
    	setParams("mvn-hibernate50-ent","5.0","2.1", null);
    	testCriteriaEditorMvn();
    }
        
    @Test
    public void testCriteriaEditorEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0",libraries);
    	testCriteriaEditorEcl();
    }
 
    @Test
    public void testCriteriaEditorEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	testCriteriaEditorEcl();
    }
    
    @Test
    public void testCriteriaEditorEcl43() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	testCriteriaEditorEcl();
    }
    
    private void setParams(String prj, String hbVersion, String jpaVersion, Map<String,String> libraryPathMap) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    	this.libraryPathMap = libraryPathMap;
    }
    
	private void prepareMaven() {
		prepareMvn(prj, hbVersion);
		//TODO is the rest of the method needed ?
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		
		//log.step("Convert project into faceted form");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA Facets");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		
		log.step("Open Hibernate Configurations View");
		deleteHibernateConfigurations();
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		log.step("Add New Hibernate Configuration and set parameters");
		new ContextMenu("Add Configuration...").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"));
		new LabeledText("Name:").setText(prj);
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
    
    private void testCriteriaEditorEcl() {
    	prepareEcl(prj, hbVersion, libraryPathMap);
    	testCriteriaEditor();    	
    }
	
	private void testCriteriaEditorMvn() {
		prepareMaven();
		testCriteriaEditor();		
	}
		
	private void testCriteriaEditor() {
		log.step("By Hibernate Console Configuration open Criteria Editor");
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.selectConsole(prj);
		new ContextMenu("Hibernate Criteria Editor").select();
		
		CriteriaEditor criteriaEditor = new CriteriaEditor(prj);
		criteriaEditor.setText("session.createCriteria(Actor.class).list();");
		criteriaEditor.save();
		criteriaEditor.runCriteria();
		
    	QueryPageTabView result = new QueryPageTabView();
    	result.open();	
    	assertTrue("Query result items expected - known issue https://issues.jboss.org/browse/JBIDE-19743", result.getResultItems().size() > 10);
	}
   
	
}
package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.editor.CriteriaEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate Criteria Editor test for Code Assist 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class CriteriaEditorCodeAssistTest extends HibernateRedDeerTest {

	private String prj;
	private String hbVersion;
	private String jpaVersion; 
	private Map<String,String> libraryPathMap;

	private static final Logger log = Logger.getLogger(CriteriaEditorCodeAssistTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;     
    
	@After
	public void cleanUp() {
		ConnectionProfileFactory.deleteAllConnectionProfiles();
		deleteAllProjects();
	}

    @Test
    public void testCriteriaEditorCodeAssistMvn35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0", null);
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0", null);
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0", null);
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1", null);
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn50() {
    	setParams("mvn-hibernate50-ent","5.0","2.1", null);
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn51() {
    	setParams("mvn-hibernate51-ent","5.1","2.1", null);
    	testCriteriaEditorCodeAssistMvn();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistMvn52() {
    	setParams("mvn-hibernate52-ent","5.2","2.1", null);
    	testCriteriaEditorCodeAssistMvn();
    }
        
    @Test
    public void testCriteriaEditorCodeAssistEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0",libraries);
    	testCriteriaEditorCodeAssistEcl();
    }
 
    @Test
    public void testCriteriaEditorCodeAssistEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	testCriteriaEditorCodeAssistEcl();
    }
    
    @Test
    public void testCriteriaEditorCodeAssistEcl40() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	testCriteriaEditorCodeAssistEcl();
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
		//log.step("Convert project into faceted form");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA Facets");
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		
		log.step("Open Hibernate Configurations View");
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		try{
			Tree configs = new DefaultTree();
			for(TreeItem i: configs.getItems()){
				i.select();
				new ContextMenu("Delete Configuration").select();
				new DefaultShell("Delete console configuration");
				new OkButton().click();
				new WaitWhile(new ShellWithTextIsAvailable("Delete console configuration"));
			}
		} catch (CoreLayerException ex){
		}
		log.step("Add New Hibernate Configuration and set parameters");
		new ContextMenu("Add Configuration...").select();
		Shell editConfig = new DefaultShell("Edit Configuration");
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
		new WaitWhile(new ShellIsAvailable(editConfig));
	}
    
    private void testCriteriaEditorCodeAssistEcl() {
    	prepareEcl(prj, hbVersion, libraryPathMap);
    	testCriteriaEditor();    	
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
		proposal = "createCriteria\\(Class arg0\\) \\: Criteria \\- Session";
		Double hv = Double.parseDouble(hbVersion);
		if(hv >= 4.0) {
			proposal = "createCriteria\\(Class \\w*\\) : Criteria - SharedSessionContract";
		}
		ca = criteriaEditor.openContentAssistant();
		proposals = ca.getProposals();
		ca.close();
		boolean shouldFail = true;
		for(String p: proposals){
			if(p.matches(proposal)){
				shouldFail = false;
				break;
			}
		}
		if(shouldFail){
			fail(proposal + " CA proposal is expected but was "+proposals);
		}
		
		expression = "session.createCriteria(Act";
		criteriaEditor.setText(expression);
		criteriaEditor.setCursorPosition(expression.length());
		proposal = "Actor - org.gen";
		ca = criteriaEditor.openContentAssistant();
		proposals = ca.getProposals();
		ca.close();
		assertTrue(proposal + " is expected", proposals.contains(proposal));						
	}
  
}
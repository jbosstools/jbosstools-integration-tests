package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.jboss.tools.hibernate.reddeer.wizard.ConsoleConfigurationCreationWizardPage;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleConnectionType;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate HQL Editor test 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class HQLEditorTest extends HibernateRedDeerTest {

	private String prj = "mvn-hibernate43";
	private String hbVersion = "4.3";
	private String jpaVersion = "2.1";
	
	Logger log = Logger.getLogger(HQLEditorTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
	private void prepare() {

    	importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);		
		ConnectionProfileFactory.createConnectionProfile(cfg);
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
	}

    @Test
    public void testHQLEditor35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0");
    	testHQLEditor();
    }
    
    @Test
    public void testHQLEditor43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1");
    	testHQLEditor();
    }
    
    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }
    

	public void testHQLEditor() {
		prepare();
		
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.openConsoleConfiguration(prj);
				
		ConsoleConfigurationCreationWizardPage p = new ConsoleConfigurationCreationWizardPage();
		p.setProject(prj);
		p.setHibernateConsoleType(HibernateConsoleType.JPA);
		p.setHibernateConsoleConnectionType(HibernateConsoleConnectionType.JPA);
		p.setHibernateVersion(hbVersion);		
		p.ok();
		
		v.open();
		v.selectConsole(prj);
		new ContextMenu("HQL Editor").select();
		TextEditor hqlEditor = new TextEditor(prj);
		hqlEditor.setText("from Actor");
		hqlEditor.save();
		
		new DefaultToolItem("Run HQL").click();
		
		try {
			new WaitUntil(new ShellWithTextIsActive("Open Session factory"), TimePeriod.SHORT);
			new YesButton().click();
		}
		catch (WaitTimeoutExpiredException e) {
			log.warn("Open Session factory question dialog was expected");
		}
		
		QueryPageTabView result = new QueryPageTabView();
    	result.open();
    	assertTrue("Query result items expected", result.getResultItems().size() > 10);
	}

   
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(prj).delete(true);
	}
}
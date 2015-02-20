package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
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
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.EntityGenerationFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Hibernate HQL Editor test 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class HQLEditorTest extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43"; 
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;
    
    @Before
	public void testConnectionProfile() {

    	importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);		
		ConnectionProfileFactory.createConnectionProfile(cfg);
		ProjectConfigurationFactory.convertProjectToFacetsForm(PRJ);
		ProjectConfigurationFactory.setProjectFacetForDB(PRJ, cfg);
		EntityGenerationFactory.generateJPAEntities(cfg,PRJ,"org.gen","4.3",false);
	}
    
    

	@Test
	public void testMappingDiagram() {
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		new ContextMenu("Add Configuration...").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"));
		DefaultGroup prjGroup = new DefaultGroup("Project:");
		new DefaultText(prjGroup).setText(PRJ);
		new RadioButton("JPA (jdk 1.5+)").click();
		DefaultGroup dbConnection = new DefaultGroup("Database connection:");
		new DefaultCombo(dbConnection,0).setText("[JPA Project Configured Connection]");
		new LabeledCombo("Hibernate Version:").setSelection("4.3");
		new PushButton("Apply").click();
		
		new OkButton().click();		
		
		v.open();
		v.selectConsole("hibernate");
		new ContextMenu("HQL Editor").select();
		TextEditor hqlEditor = new TextEditor("hibernate");
		hqlEditor.setText("from Actor");
		
		new DefaultToolItem("Run HQL").click();
		
		new WaitUntil(new ShellWithTextIsActive("Open Session factory"));
		new YesButton().click();	
		
		QueryPageTabView result = new QueryPageTabView();
    	result.open();
    	assertTrue("Query result items expected", result.getResultItems().size() > 10);
	}

   
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}
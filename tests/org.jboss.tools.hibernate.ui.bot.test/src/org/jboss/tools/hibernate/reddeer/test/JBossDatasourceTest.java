package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.wizard.JBossDatasourceWizard;
import org.jboss.tools.hibernate.reddeer.wizard.NewJBossDatasourceWizardPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Tests JBoss Datasource   
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class JBossDatasourceTest extends HibernateRedDeerTest {
	
	public static final String PRJ = "mvn-hibernate35";
	private static final Logger log = Logger.getLogger(JBossDatasourceTest.class);

    @InjectRequirement
    DatabaseRequirement dbRequirement;
    
	@Before
	public void prepare() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create database connection profile definition");
		ConnectionProfileFactory.createConnectionProfile(cfg);		
		log.step("Import testing project");
		importMavenProject(PRJ);
	}
	
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
	
	@Test
	public void jbossDatasourceTest() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();

		log.step("Open JBoss Datasource Wizard");
		JBossDatasourceWizard wizard = new JBossDatasourceWizard();
		wizard.open();
		log.step("Fillin basic details");
		NewJBossDatasourceWizardPage page =  new NewJBossDatasourceWizardPage();
		page.setConnectionProfile(cfg.getProfileName());
		page.setParentFolder("/" + PRJ + "/src/main/resources");
		log.step("Finish");
		wizard.finish();

		String dsFileName = cfg.getProfileName() + "-ds.xml";
		
		assertFalse(new DefaultEditor(dsFileName).isDirty());
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.getProject(PRJ).containsItem("src","main","resources",dsFileName));
	
	}
		

}

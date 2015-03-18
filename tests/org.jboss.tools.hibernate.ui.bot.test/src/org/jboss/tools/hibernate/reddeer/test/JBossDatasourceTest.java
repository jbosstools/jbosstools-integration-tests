package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
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
@CleanWorkspace
public class JBossDatasourceTest extends HibernateRedDeerTest {
	
	public static final String PRJ = "mvn-hibernate35";

    @InjectRequirement
    DatabaseRequirement dbRequirement;
    
	@Before
	public void prepare() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);
		
		importProject(PRJ);
	}
	
	@Test
	public void jbossDatasourceTest() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();

		JBossDatasourceWizard wizard = new JBossDatasourceWizard();
		wizard.open();
		NewJBossDatasourceWizardPage page =  new NewJBossDatasourceWizardPage();
		page.setConnectionProfile(cfg.getProfileName());
		page.setParentFolder("/" + PRJ + "/src/main/resources");
		page.finish();

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		String dsFileName = cfg.getProfileName() + "-ds.xml";
		DefaultTreeItem item = new DefaultTreeItem(PRJ,"src","main","resources",dsFileName);
		item.doubleClick();
		
		assertFalse(new DefaultEditor(dsFileName).isDirty());
	}
		
	
	
	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}

package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.EntityGenerationFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.wizard.GenerateDdlWizard;
import org.jboss.tools.hibernate.reddeer.wizard.GenerateDdlWizardPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Generates ddl and tables from Entities
 * 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name = "testdb")
public class TablesFromJPAEntitiesGeneration extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43";
	private final String DDL_FILE = "output.ddl";
	@InjectRequirement
	private DatabaseRequirement dbRequirement;

	@Before
	public void testConnectionProfile() {
		importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);
		testSetJPAFacets(cfg);
		EntityGenerationFactory.generateJPAEntities(cfg, PRJ, "org.gen", "4.3",true);
	}

	private void testSetJPAFacets(DatabaseConfiguration cfg) {
		ProjectConfigurationFactory.convertProjectToFacetsForm(PRJ);
		ProjectConfigurationFactory.setProjectFacetForDB(PRJ, cfg);
	}

	@Test
	public void testEntityGeneration() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PRJ);
		GenerateDdlWizard w = new GenerateDdlWizard();
		w.open();
		GenerateDdlWizardPage p = new GenerateDdlWizardPage();
		p.setFileName(DDL_FILE);
		w.finish();

		pe.open();
		new DefaultTreeItem(PRJ, DDL_FILE).doubleClick();
		assertTrue("DDL file cannot be empty", new TextEditor(DDL_FILE)
				.getText().length() > 0);
	}

	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}
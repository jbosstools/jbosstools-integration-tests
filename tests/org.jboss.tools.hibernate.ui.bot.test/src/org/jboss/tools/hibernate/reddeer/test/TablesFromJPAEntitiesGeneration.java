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

	private String prj = "mvn-hibernate43";
	private String hbVersion = "4.3";
	private String jpaVersion = "2.0";

	private final String DDL_FILE = "output.ddl";
	@InjectRequirement
	private DatabaseRequirement dbRequirement;
	
	public void prepare() {
		importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);
		testSetJPAFacets(cfg);
		EntityGenerationFactory.generateJPAEntities(cfg, prj, "org.gen", hbVersion, true);
	}

	private void testSetJPAFacets(DatabaseConfiguration cfg) {
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
	}
	
    @Test
    public void testDDLGeneration35() {
    	setParams("mvn-hibernate35","3.5","2.0");
    	testDDLGeneration();
    }
    
    @Test
    public void testDDLGeneration36() {
    	setParams("mvn-hibernate36","3.6","2.0");
    	testDDLGeneration();
    }
    
    @Test
    public void testDDLGeneration40() {
    	setParams("mvn-hibernate40","4.0","2.0");
    	testDDLGeneration();
    }
    
    @Test
    public void testDDLGeneration43() {
    	setParams("mvn-hibernate43","4.3","2.1");
    	testDDLGeneration();
    }
    

    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }
    
	private void testDDLGeneration() {
		prepare();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		GenerateDdlWizard w = new GenerateDdlWizard();
		w.open();
		GenerateDdlWizardPage p = new GenerateDdlWizardPage();
		p.setFileName(DDL_FILE);
		w.finish();

		pe.open();
		new DefaultTreeItem(prj,"src/main/java", DDL_FILE).doubleClick();
		assertTrue("DDL file cannot be empty, known issue(s) - https://issues.jboss.org/browse/JBIDE-19431", new TextEditor(DDL_FILE)
				.getText().length() > 0);
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
package org.jboss.tools.hibernate.reddeer.test;


import java.io.IOException;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.common.FileHelper;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.wizard.ConsoleConfigurationCreationWizardPage;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleConnectionType;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleType;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test Mapping Diagram for multiple Hibernate versions
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class MappingDiagramTest extends HibernateRedDeerTest {

	private String prj = "mvn-hibernate43-ent"; 
	private String hbVersion = "4.3";
	private String jpaVersion = "2.0";
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement;    

	@Test
    public void testMappingDiagram35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0");
    	testMappingDiagramMaven();
    }

    @Test
    public void testMappingDiagram36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0");
    	testMappingDiagramMaven();
    }
    
    @Test
    public void testMappingDiagram40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0");
    	testMappingDiagramMaven();
    }

    @Test
    public void testMappingDiagram43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1");
    	testMappingDiagramMaven();
    }

    @Test
    public void testMappingDiagramEcl35() {
    	setParams("ecl-hibernate35-ent","3.5","2.0");
    	testMappingDiagramEclipse();
    }
 
    @Test
    public void testMappingDiagramEcl36() {
    	setParams("ecl-hibernate36-ent","3.5","2.0");
    	testMappingDiagramEclipse();
    }
    
    @Test
    public void testMappingDiagramEcl40() {
    	setParams("ecl-hibernate40-ent","3.5","2.0");
    	testMappingDiagramEclipse();
    }

    private void setParams(String prj, String hbVersion, String jpaVersion) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    }

    private void testMappingDiagramMaven() {    	
    	prepareMavenProject();
    	testMappingDiagram();
    	cleanUpMvn();
    }
    
    private void testMappingDiagramEclipse() {
    	prepareEclipseProject();
    	testMappingDiagram();
    	cleanUpEcl();    
    }
    
	public void prepareMavenProject() {
    	importProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);		
		ConnectionProfileFactory.createConnectionProfile(cfg);
		ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.openConsoleConfiguration(prj);
				
		ConsoleConfigurationCreationWizardPage p = new ConsoleConfigurationCreationWizardPage();
		p.setProject(prj);
		p.setHibernateConsoleType(HibernateConsoleType.JPA);
		p.setHibernateConsoleConnectionType(HibernateConsoleConnectionType.JPA);
		p.setHibernateVersion(hbVersion);		
		p.ok();
	}

    
    private void prepareEclipseProject() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		String destDir = FileHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,"resources","prj","hibernatelib","connector" );
		try {
			FileHelper.copyFilesBinary(cfg.getDriverPath(), destDir);
		} catch (IOException e) {
			// Assert.fail("Cannot copy h2 driver");
		}
    	importProject("hibernatelib");
    	importProject(prj);
    	
    	HibernateToolsFactory.testCreateConfigurationFile(cfg, prj, "hibernate.cfg.xml", false);
	}

	private void testMappingDiagram() {
    	
		KnownConfigurationsView v = new KnownConfigurationsView();
		
		v.open();
		v.selectConsole(prj);

		ContextMenu mappingMenu = new ContextMenu("Mapping Diagram");
		mappingMenu.select();
		
		new DefaultEditor(prj+": Actor and 15 others");
	}
	
	private void cleanUpMvn() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(prj).delete(true);	
	}
	
	private void cleanUpEcl() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.deleteAllProjects();
	}
}
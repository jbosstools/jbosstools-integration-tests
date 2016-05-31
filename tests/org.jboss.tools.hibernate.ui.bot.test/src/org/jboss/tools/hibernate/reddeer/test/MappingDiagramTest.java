package org.jboss.tools.hibernate.reddeer.test;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.wizard.ConsoleConfigurationCreationWizardPage;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleConnectionType;
import org.jboss.tools.hibernate.reddeer.wizard.HibernateConsoleType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test Mapping Diagram for multiple Hibernate versions
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class MappingDiagramTest extends HibernateRedDeerTest {

	private String prj; 
	private String hbVersion;
	private String jpaVersion;
	private Map<String,String> libraryPathMap;
	
	private static final Logger log = Logger.getLogger(MappingDiagramTest.class);
	
    @InjectRequirement    
    private DatabaseRequirement dbRequirement; 
    
    @After
  	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
  		deleteAllProjects();
  	}

	@Test
    public void testMappingDiagram35() {
    	setParams("mvn-hibernate35-ent","3.5","2.0", null);
    	testMappingDiagramMaven();
    }

    @Test
    public void testMappingDiagram36() {
    	setParams("mvn-hibernate36-ent","3.6","2.0", null);
    	testMappingDiagramMaven();
    }
    
    @Test
    public void testMappingDiagram40() {
    	setParams("mvn-hibernate40-ent","4.0","2.0", null);
    	testMappingDiagramMaven();
    }

    @Test
    public void testMappingDiagram43() {
    	setParams("mvn-hibernate43-ent","4.3","2.1", null);
    	testMappingDiagramMaven();
    }
    
    @Test
    public void testMappingDiagram50() {
    	setParams("mvn-hibernate50-ent","5.0","2.1", null);
    	testMappingDiagramMaven();
    }

    @Test
    public void testMappingDiagramEcl35() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate35LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate35-ent","3.5","2.0", libraries);
    	testMappingDiagramEclipse();
    }
 
    @Test
    public void testMappingDiagramEcl36() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate36LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate36-ent","3.6","2.0", libraries);
    	testMappingDiagramEclipse();
    }
    
    @Test
    public void testMappingDiagramEcl40() {
    	Map<String,String> libraries = new HashMap<>();
    	libraries.putAll(hibernate43LibMap);
    	libraries.put("h2-1.3.161.jar", null);
    	setParams("ecl-hibernate40-ent","4.3","2.0", libraries);
    	testMappingDiagramEclipse();
    }

    private void setParams(String prj, String hbVersion, String jpaVersion, Map<String,String> libraryPathMap) {
    	this.prj = prj;
    	this.hbVersion = hbVersion;
    	this.jpaVersion = jpaVersion;
    	this.libraryPathMap = libraryPathMap;
    }

    private void testMappingDiagramMaven() {    	
    	prepareMavenProject();
    	testMappingDiagram();
    }
    
    private void testMappingDiagramEclipse() {
    	prepareEclipseProject();
    	testMappingDiagram();
    }
    
	public void prepareMavenProject() {
		log.step("Import test project");
    	importMavenProject(prj);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create database connection profile ");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		//log.step("Convert project to faceted form");
		//ProjectConfigurationFactory.convertProjectToFacetsForm(prj);
		log.step("Set JPA facets to Hibernate Platform");
		ProjectConfigurationFactory.setProjectFacetForDB(prj, cfg, jpaVersion);
		
		log.step("Open and set hibernate console configuration");

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
		log.step("Import test project");
    	importProject(prj, libraryPathMap);
		
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create database connection profile ");
		ConnectionProfileFactory.createConnectionProfile(cfg);
    	
    	log.step("Create hibernate console configuartion file");
    	HibernateToolsFactory.createConfigurationFile(cfg, prj, "hibernate.cfg.xml", false);
	}

	private void testMappingDiagram() {
		AutoBuilding ab = new AutoBuilding() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean value() {
				return false;
			}
			
			@Override
			public boolean cleanup() {
				// TODO Auto-generated method stub
				return true;
			}
		};
		
		AutoBuildingRequirement abr = new AutoBuildingRequirement();
		abr.setDeclaration(ab);
		openMappingDiagram();
		try{
			new DefaultEditor(prj+": Actor and 15 others");
		} catch (RedDeerException e) { //workaroud due to buggy auto building
			abr.fulfill();
			PackageExplorer pe = new PackageExplorer();
			pe.getProject(prj).select();
			new ShellMenu("Project","Build Project").select();
			new WaitWhile(new JobIsRunning());
			openMappingDiagram();
			new DefaultEditor(prj+": Actor and 15 others");
		} finally {
			abr.cleanUp();
		}
	}
	
	private void openMappingDiagram(){
		log.step("Open Hibernate Console Configuration view");
		KnownConfigurationsView v = new KnownConfigurationsView();
		
		v.open();
		v.selectConsole(prj);

		log.step("Open Mapping diagram");
		ContextMenu mappingMenu = new ContextMenu("Mapping Diagram");
		mappingMenu.select();
	}
}

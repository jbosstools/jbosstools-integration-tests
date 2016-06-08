package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.dialogs.ExplorerItemPropertyDialog;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.database.DatabaseUtils;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.HibernateToolsFactory;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;

@AutoBuilding(value=false,cleanup=false)
public class HibernateRedDeerTest {
	private static final Logger log = Logger.getLogger(HibernateRedDeerTest.class);
	
	private String hibernate35Base = "hibernate-distribution-3.5.3-Final/";
	private String hibernate35Prefix = hibernate35Base + "lib/required/";
	protected  Map<String,String> hibernate35LibMap = Arrays.stream(new String[][] {
		{"antlr-2.7.6.jar",hibernate35Prefix},
		{"commons-collections-3.1.jar",hibernate35Prefix},
		{"dom4j-1.6.1.jar",hibernate35Prefix},
		{"javassist-3.9.0.GA.jar",hibernate35Prefix},
		{"jta-1.1.jar",hibernate35Prefix},
		{"slf4j-api-1.5.8.jar",hibernate35Prefix},
		{"hibernate3.jar",hibernate35Base},
	}).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

	
	private String hibernate36Base = "hibernate-distribution-3.6.10.Final/";
	private String hibernate36Prefix = hibernate36Base + "lib/required/";
	protected  Map<String,String> hibernate36LibMap = Arrays.stream(new String[][] {
		{"antlr-2.7.6.jar",hibernate36Prefix},
		{"commons-collections-3.1.jar",hibernate36Prefix},
		{"dom4j-1.6.1.jar",hibernate36Prefix},
		{"javassist-3.12.0.GA.jar",hibernate36Prefix},
		{"jta-1.1.jar",hibernate36Prefix},
		{"slf4j-api-1.6.1.jar",hibernate36Prefix},
		{"hibernate3.jar",hibernate36Base},
	}).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
	
	
	private String hibernate43Base = "hibernate-release-4.3.11.Final/";
	private String hibernate43Prefix = hibernate43Base + "lib/required/";
	protected  Map<String,String> hibernate43LibMap = Arrays.stream(new String[][] {
		{"antlr-2.7.7.jar",hibernate43Prefix},
		{"dom4j-1.6.1.jar",hibernate43Prefix},
		{"hibernate-commons-annotations-4.0.5.Final.jar",hibernate43Prefix},
		{"hibernate-core-4.3.11.Final.jar",hibernate43Prefix},
		{"hibernate-jpa-2.1-api-1.0.0.Final.jar",hibernate43Prefix},
		{"jandex-1.1.0.Final.jar",hibernate43Prefix},
		{"javassist-3.18.1-GA.jar",hibernate43Prefix},
		{"jboss-logging-3.1.3.GA.jar",hibernate43Prefix},
		{"jboss-logging-annotations-1.2.0.Beta1.jar",hibernate43Prefix},
		{"jboss-transaction-api_1.2_spec-1.0.0.Final.jar",hibernate43Prefix},		
	}).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

	
	@InjectRequirement
	protected static DatabaseRequirement dbRequirement;
	
	@BeforeClass
	public static void beforeClass() {
		if(!new WorkbenchShell().isMaximized()){
			new WorkbenchShell().maximize();
		}
		String dbPath = dbRequirement.getConfiguration().getDriverPath();
		DatabaseUtils.runSakilaDB(dbPath.substring(0, dbPath.lastIndexOf(File.separator)));
		
		//https://bugs.eclipse.org/bugs/show_bug.cgi?id=470094
		PropertiesView pw = new PropertiesView();
		if(pw.isOpened()){
			pw.close();
		}
	}

	@AfterClass
	public static void afterClass() {
		DatabaseUtils.stopSakilaDB();
		deleteAllProjects();
		
	}
	
	public static void importProject(String prjName, Map<String,String> libraryPathMap) {
		ProjectImporter.importProjectWithoutErrors(Activator.PLUGIN_ID, prjName,libraryPathMap);
	}
	
	private static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                fail("Unable to delete "+dir);
	            }
	        }
	    }

	    return dir.delete();
	}
	
	public static void importMavenProject(String prjName) {
		try {
			Path sourceFolder = new File("resources/prj/"+prjName).toPath();
			File dir = new File("target/"+prjName);
			if(dir.exists()){
				deleteDir(dir);
			}
			Path destFolder = dir.toPath();
			Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(destFolder.resolve(sourceFolder.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
					Files.copy(file, destFolder.resolve(sourceFolder.relativize(file)));
					return FileVisitResult.CONTINUE;
				}
			});
			ProjectImporter.importMavenProject("target/"+prjName);
		} catch (IOException e) {
			fail("Unable to find pom "+prjName);
		}
		//TODO check error log for errors
	}

	protected static void deleteAllProjects(){
		deleteHibernateConfigurations();
		CleanWorkspaceRequirement req = new CleanWorkspaceRequirement();
		req.fulfill();
	}
	
	protected void prepareMvn(String project, String hbVersion) {
		
		log.step("Import maven project");
    	importMavenProject(project);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create Hibernate configuration file with Hibernate Console");
		HibernateToolsFactory.createConfigurationFile(cfg, project, "hibernate.cfg.xml", true);
		ConnectionProfileFactory.createConnectionProfile(cfg);
		//log.step("Set hibernate version in Hibernate Console");
		//HibernateToolsFactory.setHibernateVersion(project, hbVersion); //TODO is this needed ?
	}

	protected void prepareEcl(String project, String hbVersion, Map<String, String> libs) {
		
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Import hibernate project"); 	
    	importProject(project, libs);
    	
    	log.step("Create Hibernate configuration file with Hibernate Console");
		HibernateToolsFactory.createConfigurationFile(cfg, project, "hibernate.cfg.xml", false);
		log.step("Set Hibernate Version in Hibernate Console");
		HibernateToolsFactory.setHibernateVersion(project, hbVersion);
		log.step("Hibernate console in Hibernate Settings in Project Properties");
		//setHibernateSettings(project); TODO Is this really necessary ?

	}
	
	protected void setHibernateSettings(String project) {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		ExplorerItemPropertyDialog pd = new ExplorerItemPropertyDialog(pe.getProject(project));
		pd.open();
		pd.select("Hibernate Settings");
		
		CheckBox cb = new CheckBox();		
		if (!cb.isChecked()) cb.click();
		
		new DefaultCombo().setSelection(project);
		pd.ok();
	}
	
	protected static void deleteHibernateConfigurations(){
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		try{
			Tree configs = new DefaultTree();
			List<TreeItem> tis = configs.getItems();
			if(tis.size() == 0){
				return;
			}
			for(TreeItem t: tis){
				t.select();
				ContextMenu closeConfig = new ContextMenu("Close Configuration");
				if(closeConfig.isEnabled()){
					closeConfig.select();
				}
				new WaitWhile(new JobIsRunning());
			}
			configs.selectItems(tis.toArray(new TreeItem[tis.size()]));
			String deleteShellName = "Delete console configuration";
			if(tis.size() > 1){
				deleteShellName = "Delete console configurations";
			}
			new ContextMenu("Delete Configuration").select();
			new DefaultShell(deleteShellName);
			new OkButton().click();
			new WaitWhile(new ShellWithTextIsAvailable(deleteShellName));
		} catch (CoreLayerException ex){
		}
	}
}	



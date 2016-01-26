package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.interceptor.SyncInterceptorManager;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.hibernate.reddeer.database.DatabaseUtils;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;

public class HibernateRedDeerTest {
	
	private SyncInterceptorManager sim = SyncInterceptorManager.getInstance();
	private final String LOG_INTERCEPTOR = "error-log-interceptor";
	private static String dbFolder = System.getProperty("test.database");
	private static final Logger log = Logger.getLogger(HibernateRedDeerTest.class);
	
	@BeforeClass
	public static void beforeClass() {
		DatabaseUtils.runSakilaDB(dbFolder);
	}

	@AfterClass
	public static void afterClass() {
		DatabaseUtils.stopSakilaDB();
		deleteAllProjects();
		
	}

	public HibernateRedDeerTest() {
		/*
		sim.unregisterAll();
		String enabled = System.getProperty("hibernate.reddeer.errorLogInterceptor");
		if (enabled != null) 
		  sim.register(LOG_INTERCEPTOR, new ErrorLogInterceptor());		
		  */
	}
	
	public static void importProject(String prjName) {
		ProjectImporter.importProjectWithoutErrors(Activator.PLUGIN_ID, prjName);
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
			
			
			
			//dir.mkdir();
			/*
			try {
	            Stream<Path> allFilesPathStream = Files.walk(sourceFolder);
	            Consumer<? super Path> action = new Consumer<Path>(){

	                @Override
	                public void accept(Path t) {
	                    try {
	                        String destinationPath = t.toString().replaceAll("resources/prj/"+prjName, "target/"+prjName);
	                        Files.copy(t, Paths.get(destinationPath));
	                    } 
	                    catch(FileAlreadyExistsException e){
	                       fail("project "+prjName+" already exists");
	                    }
	                    catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }

	                }

	            };
	            allFilesPathStream.forEach(action );

	        } catch(FileAlreadyExistsException e) {
	        	fail("project "+prjName+" already exists");
	        } catch (IOException e) {
	            //permission issue
	            e.printStackTrace();
	        }

			*/
			ProjectImporter.importMavenProject("target/"+prjName);
		} catch (IOException e) {
			fail("Unable to find pom "+prjName);
		}
	}

	protected static void deleteAllProjects(){
		deleteProjects();
		deleteTree();
	}
	
	private static void deleteProjects(){
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(pe.getProjects().size() > 0){
			for(Project p: pe.getProjects()){
				try{
					org.jboss.reddeer.direct.project.Project.delete(p.getName(), true, true);
				} catch (Exception ex) {
					AbstractWait.sleep(TimePeriod.NORMAL);
					if(!p.getTreeItem().isDisposed()){
						try{
						org.jboss.reddeer.direct.project.Project.delete(p.getName(), true, true);
						}catch (Exception e){
							pe.deleteAllProjects();
						}
					}
				}
			}
		}
	}
	
	private static void deleteTree(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		try{
			if(new DefaultTree().getItems().size() > 0){
				log.debug("Delete "+new DefaultTree().getItems().size()+" projects");
				deleteTreeItemProjects();
			}
		} catch (RedDeerException ex){
			log.debug("No tree items found to delete");
		}
	}
	
	private static void deleteTreeItemProjects(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		List<TreeItem> items = new DefaultTree().getItems();
		if(items.size() > 0){
			new DefaultTree().selectItems(items.toArray(new TreeItem[items.size()]));
			new ContextMenu("Refresh").select();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			new ContextMenu("Delete").select();
			Shell s = new DefaultShell("Delete Resources");
			new CheckBox().toggle(true);
			new PushButton("OK").click();
			DeleteUtils.handleDeletion(s, TimePeriod.LONG);
		}
		
	}
}	



package org.jboss.tools.smooks.ui.bot.testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.jboss.tools.smooks.ui.bot.test.Activator;
import org.jboss.tools.smooks.ui.bot.tests.Project;
import org.jboss.tools.smooks.ui.bot.tests.SmooksTest;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.UserLibraryHelper;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.zest.SWTBotZestContextMenu;
import org.jboss.tools.ui.bot.ext.zest.SWTBotZestGraph;
import org.jboss.tools.ui.bot.ext.zest.SWTBotZestNode;
import org.jboss.tools.ui.bot.ext.zest.SWTZestBot;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SmooksProject extends SmooksTest {

	boolean projectCreated = false;

	/**
	 * Create Java Project as dependency for other tests
	 */
	@Test
	public void createProject() {
		open.perspective(ActionItem.Perspective.JAVA.LABEL);

		// Create Java Project
		JavaProjectEntity projectEntity = new JavaProjectEntity();
		projectEntity.setProjectName(Project.PROJECT_NAME);
		eclipse.createJavaProject(projectEntity);

		// Check if project is created
		open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		projectExplorer.selectProject(Project.PROJECT_NAME);
		assertTrue(eclipse.isProjectInPackageExplorer(Project.PROJECT_NAME));
		
		
		projectCreated = true;
	}

	
	/**
	 * Defines smooks user library inside
	 */
	@Test
	public void defineSmooksUserLibrary() {

		String[] jarList = UserLibraryHelper.getJarList(Project.SMOOKS_PATH
				+ "/lib");
		UserLibraryHelper.addUserLibrary("smooks-1.2.4", jarList);

		// Check if library is defined TODO
	}

	/**
	 * Add Smooks User Library to Smooks project classpath
	 */
	@Test
	public void addSmooksUserLibraryToProject() {

		// Open Project Properties
		open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		projectExplorer.selectProject(Project.PROJECT_NAME);
		ContextMenuHelper.clickContextMenu(projectExplorer.tree(), "Properties");

		// Add Library
		eclipse.waitForShell("Properties for " + Project.PROJECT_NAME);
		bot.tree().expandNode("Java Build Path").select();
		bot.tabItem("Libraries").activate();
		bot.button("Add Library...").click();
		bot.list().select("User Library");
		bot.clickButton(IDELabel.Button.NEXT);
		bot.table().getTableItem(Project.SMOOKS_LIBNAME).check();
		bot.clickButton(IDELabel.Button.FINISH);
		bot.clickButton(IDELabel.Button.OK);
	}

	/**
	 * Ads java classes from resources for testing smoooks engine
	 */
	@Test
	public void addTestingJavaClasses() {
		
		// Copy class files\
		try {
			copyFileFromResource(Project.PROJECT_NAME, "src", "org", "smooks",
					"Header.java");
			copyFileFromResource(Project.PROJECT_NAME, "src", "org", "smooks",
					"OrderItem.java");
			copyFileFromResource(Project.PROJECT_NAME, "src", "org", "smooks",
					"Order.java");
		} catch (IOException e) {
			log.error(e.getStackTrace());			
			fail("Unable to copy smooks classes resources");
		}

		projectExplorer.selectProject(Project.PROJECT_NAME);
		ContextMenuHelper.clickContextMenu(projectExplorer.tree(), "Refresh");
		
		open.viewOpen(ActionItem.View.JavaPackageExplorer.LABEL);

		// Check file existence
		assertTrue(packageExplorer.isFilePresent(Project.PROJECT_NAME, "src","org.smooks","Header.java"));
		assertTrue(packageExplorer.isFilePresent(Project.PROJECT_NAME, "src","org.smooks","OrderItem.java"));
		assertTrue(packageExplorer.isFilePresent(Project.PROJECT_NAME, "src","org.smooks","Order.java"));
	}

	@Test
	public void addTestingXMLFiles() {
		// Copy class files
		try {
			copyFileFromResource(Project.PROJECT_NAME, "xml", "order.xml");
		} catch (IOException e) {
			log.error(e.getStackTrace());			
			fail("Unable to copy smooks xml resources");
		}

		open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		projectExplorer.selectProject(Project.PROJECT_NAME);
		ContextMenuHelper.clickContextMenu(projectExplorer.tree(), "Refresh");
		
		open.viewOpen(ActionItem.View.JavaPackageExplorer.LABEL);

		// Check file existence		
		assertTrue(packageExplorer.isFilePresent(Project.PROJECT_NAME, "xml","order.xml"));
	}
	
	@Test
	public void createSmooksConfig() {
		SWTBotView view = open.viewOpen(ActionItem.View.JavaPackageExplorer.LABEL);


		eclipse.selectTreeLocation(view.bot(),Project.PROJECT_NAME,"src");
		eclipse.createNew(EntityType.SMOOKS_CONFIG);
		
		open.finish(bot.activeShell().bot());
		
		// check file
		assertTrue(packageExplorer.isFilePresent(Project.PROJECT_NAME, "src","smooks-config.xml"));
	}
	
	
	@Test 
	public void defineInputTask() {
		//SWTGefBot gefBot = new SWTGefBot();		
		///SWTBotGefEditor editor = gefBot.gefEditor("smooks-config.xml");
		
		SWTZestBot zestBot = new SWTZestBot();
		SWTBotZestGraph graph = zestBot.getZestGraph(0);		
		
		SWTBotZestNode node = graph.node("Input Task");
		node.click();
		
		bot.sleep(2000, "--------> Trying to click");
		
		SWTBotZestContextMenu menu = node.contextMenu(); 
		menu.clickMenu("Add Task","Java Mapping");
		
		bot.sleep(2000, " Context menu on node clicke <------------");
		graph.debugGraph();
		bot.sleep(2000, " Check debug info");

		// Select Order XML file
		graph.node("Input Task").click();
		bot.clickButton("Add");
		bot.clickButton("Browse WorkSpace");
		SWTBot shellBot  = bot.shell("Select Files").bot();
		eclipse.selectTreeLocation(shellBot, Project.PROJECT_NAME, "xml", "order.xml");
		bot.clickButton(IDELabel.Button.OK);
		bot.clickButton(IDELabel.Button.FINISH);	
		bot.activeEditor().save();

		SWTBotEditor editor;
		graph.connection(graph.node("Input Task"),graph.node("Java Mapping")).click();
		bot.sleep(2000,"check if edge is clicked");
	}
	
	
	@Test
	public void addJavaMapping() {
		
		bot.sleep(1000);
		SWTZestBot zestBot = new SWTZestBot();
		SWTBotZestGraph graph = zestBot.getZestGraph(0);		
		graph.node("Java Mapping").click();		
		bot.sleep(2000, "Check java mapping");
		
		bot.sleep(1000, "check widgets");
	}
	
	@Test 
	public void defineJavaMapping() {
		
		
		}
	
	@Test
	public void removeProject() {
		
		open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
		// Action
		projectExplorer.deleteProject(Project.PROJECT_NAME, true);
		util.waitForNonIgnoredJobs();
		// Check
		assertFalse(eclipse.isProjectInPackageExplorer(Project.PROJECT_NAME));		
	}

	private void copyFileFromResource(String project, String... path)
			throws IOException {

		StringBuilder inBuilder = new StringBuilder();
		StringBuilder outBuilder = new StringBuilder();

		inBuilder.append(FileLocator.toFileURL(
				Platform.getBundle(Activator.PLUGIN_ID).getEntry("/"))
				.getFile());
		inBuilder.append("resource");

		outBuilder.append(Platform.getLocation() + File.separator);
		outBuilder.append(project);

		for (int i = 0; i < path.length; i++) {
			inBuilder.append(File.separator);
			inBuilder.append(path[i]);

			outBuilder.append(File.separator);
			outBuilder.append(path[i]);

			// Create folder if doesn't exist
			if ((path.length > 1) && (i == (path.length - 2))) {
				File folder = new File(outBuilder.toString());
				folder.mkdirs();
				log.info("Folder created: " + outBuilder.toString());
			}
		}

		File in = new File(inBuilder.toString());
		File out = new File(outBuilder.toString());

		FileChannel inChannel = null;
		FileChannel outChannel = null;

		inChannel = new FileInputStream(in).getChannel();
		outChannel = new FileOutputStream(out).getChannel();

		inChannel.transferTo(0, inChannel.size(), outChannel);

		if (inChannel != null)
			inChannel.close();
		if (outChannel != null)
			outChannel.close();
		log.info("In file: " + inBuilder.toString() + " -> Out file: "
				+ outBuilder.toString());
	}
}

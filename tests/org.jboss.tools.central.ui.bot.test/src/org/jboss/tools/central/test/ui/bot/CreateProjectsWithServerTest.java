package org.jboss.tools.central.test.ui.bot;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTFormsBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Preference;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


//TODO When testing new build try it with type=ServerType.EAP !!!!
@Require(clearProjects=false,server=@org.jboss.tools.ui.bot.ext.config.Annotations.Server(type=ServerType.ALL))
public class CreateProjectsWithServerTest extends SWTTestExt{
	
	
	private class QuickstartCategoryLabel extends SWTBotLabel{

		public QuickstartCategoryLabel(Label widget)
				throws WidgetNotFoundException {
			super(widget);
		}
		
		public void select(){
			notify(SWT.MouseEnter);
		}
		
	}
	
	@BeforeClass
	public static void setup() throws FileNotFoundException{
		util.closeAllEditors(false);
		util.closeAllViews();
		SWTOpenExt open = new SWTOpenExt(bot);
		open.preferenceOpen(Preference.create("Maven"));
		bot.checkBox("Download repository index updates on startup").deselect();
		bot.clickButton("OK");
		bot.menu("Help").menu(IDELabel.JBossCentralEditor.JBOSS_CENTRAL).click();
		util.waitForAll();
		if (configuredState.getServer().type.equalsIgnoreCase("EAP")){
			setupEAP();
		}
	}
	/**
	 * Sets up maven configuration file with configured EAP and WFK repository and clears ~/.m2/clean-repository 
	 * Clears ~/.m2/clean-repository if exists
	 * @throws FileNotFoundException 
	 */
	private static void setupEAP() throws FileNotFoundException{
		String mvnConfigFileName = System.getProperty("eap.maven.config.file");
		File mvnConfigFile;
		try {
			mvnConfigFile = new File(mvnConfigFileName);
		} catch (NullPointerException e) {
			throw new NullPointerException("eap.maven.config.file wasn't set");
		}
		if (!mvnConfigFile.exists()) throw new FileNotFoundException("File configured in eap.maven.config.file " +
				"property does not exist");
		File mvnLocalRepo = new File(System.getProperty("user.home")+"/.m2/clean-repository");
		if (mvnLocalRepo.exists()){
			deleteDirectory(mvnLocalRepo);
		}
		//Now is ~/.m2/clean-repository deleted and settings.xml exists. Next step is to tell eclipse to use our settings.xml
		open.preferenceOpen(Preference.create("Maven", "User Settings"));
		if (bot.text(1).getText().equals("User settings file doesn't exist")){
			bot.text(2).setText(mvnConfigFileName);
		}else{
			bot.text(1).setText(mvnConfigFileName);
		}
		bot.clickButton("Update Settings");
		util.waitForNonIgnoredJobs();
		bot.clickButton("Apply");
		bot.clickButton("OK");
		util.waitForNonIgnoredJobs();
	}
	
	private static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	@After
	public void teardown(){
		packageExplorer.deleteAllProjects();
		servers.removeAllProjectsFromServer("AS-7.0");
		bot.closeAllShells();
	}
	
	@Test 
	public void createProjectsSectionTest(){
		//Openshift app
		
		bot.hyperlink(IDELabel.JBossCentralEditor.OPENSHIFT_APP).click();
		bot.waitForShell(IDELabel.JBossCentralEditor.OPENSHIFT_APP_WIZARD);
		bot.waitWhile(new NonSystemJobRunsCondition());
		assertTrue("New OpenShift Express Application window should have appeared", bot.activeShell().getText().equals(IDELabel.JBossCentralEditor.OPENSHIFT_APP_WIZARD));
		bot.waitWhile(new NonSystemJobRunsCondition());
		bot.activeShell().close();
		bot.waitWhile(new NonSystemJobRunsCondition());
		
		bot.hyperlink(IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT).click();
		SWTBotShell projectExampleShell = bot.waitForShell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE);
		assertTrue("Project Example window should have appeared", bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).isActive());
		try{
			bot.clickButton("Install");
			fail("Button \"Install\" should not be enabled, because all requirements should have been met");
		}catch(WidgetNotFoundException wnfex){
			//ok
		}
		projectExampleShell.activate();
		assertFalse("Button \"Download and Install...\" should not be enabled, because all requirements should have been met, condition", bot.button("Download and Install...").isEnabled());
		projectExampleShell.close();
		bot.toolbarDropDownButtonWithTooltip("New").click();
		bot.waitForShell("New");
		assertTrue("Shell \"New\" should have appeared", bot.shell("New").isActive());
		bot.activeShell().close();
	}
	

	@Test
	public void createProjectSectionJavaEEWebProjectTest(){
		checkExample(null, IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT, true, false);
		canBeDeployedTest();
	}
	
	@Test
	public void createProjectSectionJavaEEWebProjectBlankTest(){
		checkExample(null, IDELabel.JBossCentralEditor.JAVA_EE_WEB_PROJECT, false, true);
	}
	
	@Test
	public void createProjectSectionHTML5ProjectTest(){
		checkExample(null, IDELabel.JBossCentralEditor.HTML5_PROJECT, true, false);
		canBeDeployedTest();
	}
	
	@Test
	public void createProjectSectionRichFacesProjectTest(){
		checkExample(null, IDELabel.JBossCentralEditor.RICHFACES_PROJECT, true, false);
		canBeDeployedTest();
	}
	
	@Test @Ignore // JBIDE-14534
	public void createProjectSectionSpringMVCProjectTest(){
		checkExample(null, IDELabel.JBossCentralEditor.SPRING_MVC_PROJECT, true, false);
		canBeDeployedTest();
	}
	
	@Test
	public void createProjectSectionMavenProjectTest(){
		bot.hyperlink("Maven Project").click();
		bot.waitForShell("New Maven Project");
		bot.activeShell().close();
		bot.waitWhile(new ShellIsActiveCondition("New Maven Project"));
	}
	
	public void projectExamplesSectionTest(String name, String projectName, String category){
		importProjectExample(name, projectName, category);
		canBeDeployedTest();
	}
	
	public void importProjectExample(String name, String projectName, String category){
		SWTFormsBotExt formsBot = SWTBotFactory.getFormsBot();
		QuickstartCategoryLabel label = new QuickstartCategoryLabel(bot.label(category).widget);
		label.select();
		checkExample(formsBot, name, true, projectName);
	}
	
	
	@Test
	public void projectExamplesSectionKitchensinkHTML5Test(){
		projectExamplesSectionTest("kitchensink-html5-mobile", "jboss-as-kitchensink-html5-mobile", "Mobile Applications");
	}
	
	@Test
	public void projectExamplesSectionKitchensinkTest(){
		projectExamplesSectionTest("kitchensink", "jboss-as-kitchensink", "Web Applications");
	}
	
	@Test
	public void projectExamplesSectionGreeterTest(){
		projectExamplesSectionTest("greeter", "jboss-as-greeter", "Web Applications");
	}
	
	@Test
	public void projectExamplesSectionHelloworldTest(){
		projectExamplesSectionTest("helloworld", "jboss-as-helloworld", "Web Applications");
	}
	
	@Test
	public void projectExamplesSectionKitchensinkRfTest(){
		projectExamplesSectionTest("kitchensink-rf", "jboss-as-kitchensink-rf", "Web Applications");
	}
	
	@Test
	public void projectExamplesSectionHelloworldJMSTest(){
		projectExamplesSectionTest("helloworld-jms", "jboss-as-helloworld-jms", "Back-end Applications");
	}
	
	/**
	 * Tries to deploy all imported projects
	 */
	public void canBeDeployedTest(){
		servers.show();
		try{
			bot.viewByTitle("Project Explorer").close();
		}catch (WidgetNotFoundException ex){
			//do nothing
			log.info("Project Explorer is already closed");  
		}
		try {
			bot.viewByTitle("Problems").close();
		}catch (WidgetNotFoundException ex){
			//let be...
		}
		String serverName = configuredState.getServer().name;
		servers.findServerByName(servers.bot().tree(), serverName).contextMenu("Add and Remove...").click();
		try{
			bot.shell("Add and Remove...").activate();
			for (SWTBotTreeItem treeItem : bot.tree().getAllItems()) {
				treeItem.select();
				log.info("Adding "+treeItem.getText()+" to server");
				bot.clickButton("Add >");
					log.info("Succesfully added");
			}
			bot.clickButton("Finish");
		}catch (WidgetNotFoundException ex){
			bot.shell("Server").activate();
			bot.clickButton("OK");
		}
		servers.show();
		bot.waitWhile(new NonSystemJobRunsCondition(), TaskDuration.LONG.getTimeout());
		assertTrue("Errors node should be null", new ProblemsView().getAllErrors().isEmpty());
		servers.show();
		bot.waitWhile(new NonSystemJobRunsCondition());
		final SWTBotTreeItem serverTreeItem = servers.findServerByName(servers.bot().tree(), serverName).expand();
		bot.sleep(TIME_1S);
		for (final SWTBotTreeItem projectName : packageExplorer.show().bot().tree().getAllItems()) {
			log.info("Testing project "+projectName.getText());
			try{
				new WaitUntil(new WaitForProjectDeployed(serverTreeItem, projectName.getText()));
				log.info("Project: "+projectName.getText()+" is properly deployed.");
			}catch (WaitTimeoutExpiredException wnfe){
				//exception for Java EE Web project. It hase 4 projects, multi, multi-ear, multi-ejb and multi-web.
				if (!projectName.getText().contains(IDELabel.JBossCentralEditor.JAVA_EE_PROJECT.replaceAll("\\s", ""))){
					//jms and osgi aren't project, that can be deployed to server
					if (!projectName.getText().equals("jboss-as-helloworld-jms") && !projectName.getText().equals("jboss-as-helloworld-osgi")){
						fail("Project <"+projectName.getText()+"> is not deployed on server correctly");
					}
				}
			}
		}
		servers.removeProjectFromServers(serverName);
	}
	
	
	public void checkExample(SWTFormsBotExt formsBot, String formText, boolean readme, boolean blank){
		checkExample(formsBot, formText, readme, blank, null, null);
	}

	/**
	 * 
	 * @param formsBot formBot==null => link is of type HyperLink else it is of type FormText
	 * @param formText
	 * @param readme true if readme should be shown
	 */
	
	private void checkExample(SWTFormsBotExt formsBot, String formText, boolean readme, String projectName){
		checkExample(formsBot, formText, readme, false, projectName, null);
	}
	
	/**
	 * Checks example
	 * @param formsBot bot for Forms
	 * @param formText text to be clicked at
	 * @param readme true if readme is supposed to show, false otherwise
	 * @param readmeFileName 
	 */
	
	protected void checkExample(SWTFormsBotExt formsBot, String formText, boolean readme, boolean blank, String projectName, String readmeFileName){
		new ProblemsView();
		if (formsBot==null){
			bot.hyperlink(formText).click();
		}else{
			try{
				formsBot.formTextWithText(formText).click();
			}catch(WidgetNotFoundException wnfex){
				throw new WidgetNotFoundException("Could not found widget of type Hyperlink and text " +
						formText, wnfex);
			}
		}
		bot.waitForShell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE);
		SWTBotWizard wizard = new SWTBotWizard(bot.shell(IDELabel.JBossCentralEditor.PROJECT_EXAMPLE).widget);
		if (formsBot == null){
			bot.comboBox(0).setSelection(1); //Target runtime combobox
			try{
				bot.link();
				if (!bot.link().getText().isEmpty()){
					fail("There is something wrong with maven repo. Message: \n"+bot.link().getText());
				}
			}catch (WidgetNotFoundException ex){
				//everything fine
			}
			if (blank){
				bot.checkBox(0).click(); //Create a blank project checkbox
			}
			wizard.next();
			bot.comboBox().setText(formText.replaceAll("\\s", ""));
			if (wizard.canNext()) wizard.next();
			wizard.finishWithWait();
		}else{
			while (wizard.canNext()) wizard.next();
			wizard.finishWithWait();
		}
		String readmeText = bot.checkBox(1).getText();
		assertFalse("Quick fix should not be enabled (Everything should be fine)", bot.checkBox(0).isEnabled());
		if (readme){
			assertTrue("Show readme checkbox should be enabled", bot.checkBox(1).isEnabled());
			assertTrue("Show readme checkbox should be checked by default", bot.checkBox(1).isChecked());
			if (readmeFileName != null){
				assertTrue(readmeText.toLowerCase().contains(readmeFileName));
				bot.clickButton("Finish");
				assertTrue("Cheat Sheets view should be opened right now", bot.activeView().getTitle().equals("Cheat Sheets"));
				bot.activeView().close();
			}else if (readmeText.contains("cheatsheet.xml")){
				bot.clickButton("Finish");
				assertTrue("Cheat Sheets view should be opened right now", bot.activeView().getTitle().equals("Cheat Sheets"));
				bot.activeView().close();
			}else if (readmeText.toLowerCase().contains("readme.md") || readmeText.toLowerCase().contains("readme.txt")){
				bot.clickButton("Finish");
				assertTrue("Readme should have opened in Text Editor", bot.activeEditor().getReference().getEditor(false).getClass().getName().contains("org.eclipse.ui.editors.text.TextEditor")); //because readmes are opening in browser now.. It's a bug. Jira is created.
				bot.activeEditor().close();
			}else if (readmeText.toLowerCase().contains("readme.htm")){
				bot.clickButton("Finish");
				assertTrue("Readme should have opened in Internal Browser", bot.activeEditor().getReference().getEditor(false).getClass().getName().contains("org.eclipse.ui.internal.browser.WebBrowserEditor"));
			}
		}else{
			bot.clickButton("Finish");
		}
	}
	
	class WaitForProjectDeployed implements WaitCondition{

		private String projectName;
		private SWTBotTreeItem serverTreeItem;
		
		public WaitForProjectDeployed(SWTBotTreeItem serverTreeItem, String projectName) {
			this.projectName = projectName;
			this.serverTreeItem = serverTreeItem;
		}
		
		@Override
		public String description() {
			return "Waiting for " + projectName + " to be deployed";
		}

		@Override
		public boolean test() {
			try{
				serverTreeItem.getNode(projectName+"  [Started, Synchronized]");
			}catch(WidgetNotFoundException wnfe){
				return false;
			}
			return true;
		}
		
	}
}

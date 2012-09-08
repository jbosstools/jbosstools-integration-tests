package org.jboss.tools.esb.ui.bot.tests.examples;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.ExampleTest;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;

public class ESBExampleTest extends ExampleTest{

	
	/**
	 * returns example project name (as it is imported to workspace)
	 * @return
	 */
	public String getExampleProjectName() {
		return getProjectNames()[0];
	}
	/**
	 * returns name of example client project (null if none)
	 * @return
	 */
	public String getExampleClientProjectName() {
		return getProjectNames()[1];
	}
	@Override
	public String[] getProjectNames() {
		return new String[2];
	}
	
	@Override
	public String getExampleCategory() {
		return getRunningSoaVersionTreeLabel();
	}
	/**
	 * called after example projects are imported, by default fixes both example project references and (if defined) client example references 
	 */
	@Override
	protected void postImport() {
		fixExampleLibs();
		if (getExampleClientProjectName()!=null) {
			fixExampleClientLibs();
		}	
		
		/* Close the open, but empty "Quick Fix" dialog if it is open.
		 * Temporary fix to workaround https://issues.jboss.org/browse/JBIDE-11781 */
		try {
			SWTTestExt.bot.shell("Quick Fix").close();
		}
		catch (Exception E) {
			log.error("Condition from https://issues.jboss.org/browse/JBIDE-11781 not found " + E.getMessage());
		}
		
		openESBConfig();
		
		/* Temporary fix to workaround JBDS-2011 */
		//System.out.println ("DEBUG - name = " + getExampleProjectName() );
		if (!getExampleProjectName().equals("transform_CSV2XML")) {
			assertProblemsView();
		}
	}
	/**
	 * opens up ESB config file contained in example project in ESB Editor
	 */
	protected void openESBConfig() {
		String[] config = {getExampleProjectName(),"esbcontent","META-INF","jboss-esb.xml"};
		assertTrue("ESB config file does not exist",projectExplorer.existsResource(config));
		SWTBotEditor editor = projectExplorer.openFile(getExampleProjectName(), "esbcontent","META-INF","jboss-esb.xml");
		assertNotNull("No editor was opened", editor);
		assertEquals("Wrong editor was opened", "jboss-esb.xml", editor.getTitle());
		editor.close();
	}
	/**
	 * executes (deploys) example project
	 */
	@Override
	protected void executeExample() {
		super.executeExample();
		packageExplorer.runOnServer(getExampleProjectName());
		util.waitForNonIgnoredJobs();
	}
	/**
	 * executes given class in given project (path must include project name)
	 * @param path clientClass as could be seen in package explorer (e.g src, org.jboss.tools.test.Class.java)
	 * @return string in server log console that was appended  or null if nothing appended
	 */
	protected String executeClientGetServerOutput(String... clientClass) {
		//String text = console.getConsoleText();
		SWTBotTreeItem jmsCall = SWTEclipseExt.selectTreeLocation(packageExplorer.show().bot(),clientClass);
		// eclipse.runTreeItemAsJavaApplication(jmsCall);
		
		/* ldimaggi - Aug 2012 - https://issues.jboss.org/browse/JBQA-6462 */
		eclipse.runTreeItemAsJavaApplication2(jmsCall);
		
		bot.sleep(Timing.time5S());
		util.waitForNonIgnoredJobs();
		String returnString = consoleWaiting();
		return returnString;				
	}
	

	
	/**
	 * executes given class in given project (path must include project name)
	 * @param path clientClass as could be seen in package explorer (e.g src, org.jboss.tools.test.Class.java)
	 * @return string in log console that was appended
	 */
	protected String executeClient(String... clientClass) {	
		SWTBotTreeItem jmsCall = SWTEclipseExt.selectTreeLocation(packageExplorer.show().bot(),clientClass);
		
		//eclipse.runTreeItemAsJavaApplication(jmsCall);
		/* ldimaggi - Aug 2012 - https://issues.jboss.org/browse/JBQA-6462 */
		eclipse.runTreeItemAsJavaApplication2(jmsCall);
		
		bot.sleep(Timing.time5S());
		String text =  console.getConsoleText(TIME_5S, TIME_20S, false);
		console.switchConsole(configuredState.getServer().name); // switch console back for sure
		return text;
	}
	/**
	 * executes given class in given project (path must include project name)
	 * @param className full name of class to run
	 * @param arguments arguments that should be passed to classes main method (can be null)
	 * @return string in server log that was appended  or null if nothing appended
	 */
	protected String executeClientGetServerOutput(String className, String arguments) {
		String text = console.getConsoleText();		
		eclipse.runJavaApplication(getExampleClientProjectName(), className, arguments);
		String returnString = consoleWaiting();
		return returnString;	
	}	
	
	
	protected String consoleWaiting () {
		// New - the consoles fail to switch....sometimes
		bot.sleep(Timing.time30S());		
		boolean consoleSwitched = false;	
		int switchLimit = 30;
		int switchCounter = 0;
		consoleSwitched = console.switchConsole(configuredState.getServer().name);
		while (!consoleSwitched) {
			consoleSwitched = console.switchConsole(configuredState.getServer().name);
			bot.sleep(Timing.time10S());
			log.error("Console did not switch - retrying.");
			if (switchCounter++ > switchLimit) {
				break;
			}
		}		
		//console.switchConsole(configuredState.getServer().name);
		//String text2 = console.getConsoleText(TIME_5S, TIME_20S, false);
		String text2 = console.getConsoleText(TIME_5S, TIME_30S, false);  /* https://issues.jboss.org/browse/JBQA-5838 - ldimaggi  */
		log.info("text2 = " + text2);
		//console.clearConsole();

		if (text2.length() == 0) {			
			return null;
		}
		else {
			return text2;
		}	
	}
	
	
	
	protected void fixJREToWorkspaceDefault(String project) {
		SWTBotTree tree = projectExplorer.show().bot().tree();
		SWTBotTreeItem proj = tree.select(project).getTreeItem(project);
		for (SWTBotTreeItem item : proj.getItems()) {
			if (item.getText().startsWith("JRE System")) {
				ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
				new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.PROPERTIES, false)).click();
				SWTBotShell shell = bot.activeShell();
				shell.bot().radio(2).click();
				open.finish(shell.bot(),IDELabel.Button.OK);
				break;
			}
		}

	}
	protected void fixExampleLibs() {
		fixLibrary(getExampleProjectName(),"Server Library");
		fixLibrary(getExampleProjectName(),"JBoss ESB Runtime");
		util.waitForNonIgnoredJobs();
	}
	protected void fixExampleClientLibs() {
		fixLibrary(getExampleClientProjectName(),"Server Library");
		fixLibrary(getExampleClientProjectName(),"JBoss ESB Runtime");
		fixJREToWorkspaceDefault(getExampleClientProjectName());
		util.waitForNonIgnoredJobs();
	}
	protected void assertProblemsView() {
		//bot.sleep(60000l);
		SWTBotTreeItem errors = ProblemsView.getErrorsNode(bot);
		assertNull("Project still contain problems :"+SWTEclipseExt.getFormattedTreeNode(errors),errors);
	}
	protected void fixLibrary(String project, String lib) {
		SWTBotTree tree = projectExplorer.show().bot().tree();
		SWTBotTreeItem proj = tree.select(project).getTreeItem(project);
		proj.expand();
		boolean fixed=false;
		boolean found=false;
		for (SWTBotTreeItem item : proj.getItems()) {
			if (item.getText().startsWith(lib)) {
				found = true;
				ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
				new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.PROPERTIES, false)).click();
				SWTBotShell shell = bot.activeShell();

				/* ldimaggi - July 2012 - added this as all tests were failing - unclear how this EVER worked before the change */
				if (lib.contains("ESB")) {
					shell.bot().table().select("ESB-" + configuredState.getServer().bundledESBVersion);
				}
				else {
					shell.bot().table().select(configuredState.getServer().name);	
				}					
				open.finish(shell.bot(),IDELabel.Button.OK);
				fixed=true;
				break;
			}
		}
		if (!fixed && found) {
			log.error("Libray starting with '"+lib+"' in project '"+project+"' was not fixed.");
			bot.sleep(Long.MAX_VALUE);
		}
		if (!found) {
			log.info("Libray starting with '"+lib+"' in project '"+project+"' was not found.");
		}
	}
	/**
	 * gets label in project examples tree derived by version of soa we currently run
	 * @return
	 */
	protected String getRunningSoaVersionTreeLabel() {
		String ret = "ESB for JBoss Enterprise SOA Platform ";
		//String ret = "ESB for SOA-P ";
		
		if (!configuredState.getServer().isConfigured) {
			throw new NullPointerException("No server was configured for test, but it is required");
		}
		if (configuredState.getServer().version.startsWith("5.")) {
			ret+="5.0";
		}
		else if (configuredState.getServer().version.equals("4.3")) {
			ret+="4.3";
		}
		else if (configuredState.getServer().version.equals("6.0")) {
			ret="JBoss Quickstarts";
		}
		else {
			assertNotNull("We are running on unexpected SOA-P version "+configuredState.getServer().version+" update test source code "+this.getClass().getName(), null);
			return null;
		}
		return ret;
	}
}

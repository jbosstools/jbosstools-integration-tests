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
		openESBConfig();
		assertProblemsView();
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
		String text = console.getConsoleText();		
		SWTBotTreeItem jmsCall = SWTEclipseExt.selectTreeLocation(packageExplorer.show().bot(),clientClass);
		eclipse.runTreeItemAsJavaApplication(jmsCall);
		bot.sleep(Timing.time5S());
		util.waitForNonIgnoredJobs();
		console.switchConsole(configuredState.getServer().name);
		String text2 = console.getConsoleText(TIME_5S, TIME_20S, false);
		if (text.length()>=text2.length()) {
			return null;
		}
		return text2.substring(text.length());
	}
	/**
	 * executes given class in given project (path must include project name)
	 * @param path clientClass as could be seen in package explorer (e.g src, org.jboss.tools.test.Class.java)
	 * @return string in log console that was appended
	 */
	protected String executeClient(String... clientClass) {	
		SWTBotTreeItem jmsCall = SWTEclipseExt.selectTreeLocation(packageExplorer.show().bot(),clientClass);
		eclipse.runTreeItemAsJavaApplication(jmsCall);
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
		bot.sleep(Timing.time5S());
		console.switchConsole(configuredState.getServer().name);
		String text2 = console.getConsoleText(TIME_5S, TIME_20S, false);
		if (text.length()>=text2.length()) {
			return null;
		}
		return text2.substring(text.length());
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
				shell.bot().table().select(configuredState.getServer().name);
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
		String ret = "ESB for SOA-P ";
		if (!configuredState.getServer().isConfigured) {
			throw new NullPointerException("No server was configured for test, but it is required");
		}
		if (configuredState.getServer().version.startsWith("5.")) {
			ret+="5.0";
		}
		else if (configuredState.getServer().version.equals("4.3")) {
			ret+="4.3";
		}
		else {
			assertNotNull("We are running on unexpected SOA-P version "+configuredState.getServer().version+" update test source code "+this.getClass().getName(), null);
			return null;
		}
		return ret;
	}
}

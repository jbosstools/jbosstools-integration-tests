package org.jboss.tools.forge.ui.bot.test.suite;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.forge.ui.bot.test.util.ViewUtils;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.junit.After;
import org.junit.Before;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * 
 * @author psrna
 *
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present))
public class ForgeConsoleTestBase extends SWTTestExt {

	protected static final String PROJECT_NAME = "testproject";
	protected static final String PACKAGE_NAME = "org.jboss.testpackage";
	protected static final String ENTITY_NAME = "testentity";
	protected static final String FIELD_NAME = "testfield";
	
	protected static final long WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT = TIME_60S*5;
	
	protected static ProjectExplorer pExplorer = null;
	
	@Before
	public void setup(){
				
		pExplorer = new ProjectExplorer();
		openForgeView();
		startForge();
		cdWS();
		clear();
	}
	
	@After
	public void cleanup(){

		cdWS();
		clear();
		pExplorer = new ProjectExplorer();
		pExplorer.deleteAllProjects();
		bot.sleep(TIME_5S);
	}
	
	public enum ProjectTypes{
		jar, war, pom
	}
	
	protected void createProject(){
		
		cdWS();
		getStyledText().setText("new-project --named " + PROJECT_NAME + 
								" --topLevelPackage " + PACKAGE_NAME + "\n");
		
		getStyledText().setText("Y\n");
		
		ConsoleUtils.waitUntilTextInConsole("project [" + PROJECT_NAME + "]", TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
	}
	
	protected void createProject(ProjectTypes type){
		
		cdWS();
		getStyledText().setText("new-project --named " + PROJECT_NAME + 
								" --topLevelPackage " + PACKAGE_NAME + 
								" --type " + type + "\n");
		
		getStyledText().setText("Y\n");
		
		ConsoleUtils.waitUntilTextInConsole("project [" + PROJECT_NAME + "]", TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
	}
	
	protected void createPersistence(){
		
		createPersistence("HIBERNATE", "JBOSS_AS7");
	}
	
	protected void createPersistence(String provider, String container){
		
		getStyledText().setText("persistence setup\n");
		getStyledText().setText(provider + "\n");
		getStyledText().setText(container + "\n");
		getStyledText().setText("\n"); //accept default java-ee-spec
		getStyledText().setText("N\n"); //JPA 2 metamodel generator?
		getStyledText().setText("N\n"); //extended APIs. Install these as well?
		
		ConsoleUtils.waitUntilTextInConsole("persistence.xml", TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
	}
	
	protected void createEntity(){
		createEntity(ENTITY_NAME, PACKAGE_NAME);
	}
	
	protected void createEntity(String entityName, String packageName){
		
		final String ENTITY_CREATED = "Created @Entity [" + packageName + "." + entityName + "]";
		
		getStyledText().setText("entity\n");
		getStyledText().setText(entityName + "\n");
		getStyledText().setText(packageName + "\n");
		
		ConsoleUtils.waitUntilTextInConsole(ENTITY_CREATED , TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
		
	}
	
	protected void createStringField(String fieldName){
		
		final String FIELD_ADDED = "Added field to " + PACKAGE_NAME + "." +
									ENTITY_NAME + ": @Column private String " + fieldName + ";";
		
		getStyledText().setText("field string --named " + fieldName + "\n" );
		
		ConsoleUtils.waitUntilTextInConsole(FIELD_ADDED , TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
	}
	
	protected void installPlugin(String pluginName){
		
		getStyledText().setText("forge install-plugin " + pluginName + "\n");
		
		ConsoleUtils.waitUntilTextInConsole("***SUCCESS*** Installed" , TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
		
		bot.sleep(TIME_1S*2);
	}
	
	public static SWTBotView openForgeView(){
		if(isForgeViewActive())
			return getForgeView();
		
		bot.menu(IDELabel.Menu.WINDOW)
		   .menu(IDELabel.Menu.SHOW_VIEW)
		   .menu(IDELabel.Menu.OTHER).click();	
	
		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		shell.bot().tree().expandNode("Forge", false).select("Forge Console");

		open.finish(bot.activeShell().bot(), IDELabel.Button.OK);
		
		SWTBotView view = getForgeView();
		return view;
	}
	
	public static SWTBotShell getFRuntimesPrefShell(){
		
		bot.menu(IDELabel.Menu.WINDOW)
		   .menu(IDELabel.Menu.PREFERENCES).click();
		
		SWTBotShell shell = bot.shell("Preferences");
		shell.activate();
		
		SWTBotTreeItem forge = shell.bot().tree().getTreeItem("Forge");
		forge.expand();
		SWTBotTreeItem item = forge.getNode("Installed Forge Runtimes");
		item.setFocus();
		item.select();
		item.click();
		
		return shell;
	}
	
	
	public static void clear() {
		if(!isForgeViewActive())
			openForgeView();
		if(!isForgeRunning())
			return;
		
		getStyledText().setText("clear\n");
		bot.sleep(TIME_5S);
	}
	
	public static void cdWS() {
		getStyledText().setText("cd #" + "\n");
		bot.sleep(TIME_1S);
	}
	
	public static String pwd(){
		
		getStyledText().setText("pwd\n");
		bot.sleep(TIME_1S);
		int line = getStyledText().cursorPosition().line;
		return getStyledText().getTextOnLine(line - 1);
	}
	
	public static String getForgeVersion(){
		
		clear();
		
		getStyledText().setText("version\n");
		int line = getStyledText().cursorPosition().line;
		String versionStr = getStyledText().getTextOnLine(line - 1);
		int leftBracket = versionStr.indexOf("[");
		int rightBracket = versionStr.indexOf("]");
		
		return versionStr.substring(leftBracket+1, rightBracket).trim();
	}
	
	public static void setForgeRuntime(String name, String location){
		
		SWTBotShell preferences = getFRuntimesPrefShell();
		
		preferences.bot().button("Add...").click();
		SWTBotShell addShell = bot.shell("Add Forge Runtime");
		addShell.activate();
		
		addShell.bot().text(0).setText(name);
		addShell.bot().text(1).setText(location);
		addShell.bot().button("OK").click();
		
		assertTrue(preferences.bot().table().cell(1, 0).equals(name));
		assertTrue(preferences.bot().table().cell(1, 1).equals(location));	
		
		SWTBotTableItem cell_embedded = preferences.bot().table().getTableItem("embedded");
		SWTBotTableItem cell_new = preferences.bot().table().getTableItem(name);
		
		cell_new.check();
		assertFalse(cell_embedded.isChecked());
		assertTrue(cell_new.isChecked());
		
		preferences.bot().button("OK").click();
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
		
	}
	
	public static void removeForgeRuntime(String name){
		
		SWTBotShell preferences = getFRuntimesPrefShell();
		SWTBotTableItem cell_embedded = preferences.bot().table().getTableItem("embedded");
		cell_embedded.check(); //check embedded first
		
		SWTBotTableItem cell = preferences.bot().table().getTableItem(name);
		assertTrue("Forge Runtime to be removed does not exist!", cell != null);
		cell.select();
		preferences.bot().button("Remove").setFocus();
		preferences.bot().button("Remove").click();
		
		preferences.bot().button("OK").click();
		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
	}
	
	/*
	 * This is private, use openForgeView method outside this class to get 
	 * Forge Console View.
	 */
	public static SWTBotView getForgeView(){
		SWTBotView view = bot.viewByTitle("Forge Console");
		view.setFocus();
		view.show();
		return view;
	}
	
	public static boolean isForgeViewActive(){
		
		try{
			SWTBotView view = getForgeView();
			if(view.isActive()){
				log.info("Forge Console View is active.");
				return true;
			}
		}catch (Exception e){
		}
		log.info("Forge Console View NOT active.");
		return false;
	}
	
	public static boolean isForgeRunning(){
		
		if(!isForgeViewActive())
			openForgeView();
		
		try{
			SWTBotView view = openForgeView();
			PageBook pb = view.bot().widget(widgetOfType(PageBook.class));
			SWTBot pbbot = new SWTBot(pb);
			pbbot.styledText().setText("clear\n");
			bot.sleep(TIME_5S);
	
			log.info("Sending 'about' command to get forge version");
			pbbot.styledText().setText("about\n");
			log.info("'about' Command sent");
			bot.sleep(TIME_5S); //wait for response
			
			String text = pbbot.styledText(0).getText();
			
			if(text.contains("Forge")){
				pbbot.styledText().setText("clear\n"); 
				return true;
			}else{
				log.info("Response from 'about' command does NOT contain Forge info");
			}
		}catch (Exception e) {
		}
		return false;
	}
	
	public static void startForge(){
		log.info("Trying to start forge...");
		
		if(!isForgeViewActive())
			openForgeView();
		if(isForgeRunning())
			return;
		
		SWTBotView view = getForgeView();
		/* view.toolbarButton("Start the default Forge runtime").click(); */
		//not working with juno ^^^
		//workaround:
		ViewUtils.getToolbarButton("Start the default Forge runtime").click();	

		util.waitForNonIgnoredJobs(WAIT_FOR_NON_IGNORED_JOBS_TIMEOUT);
	}
	
	public static void stopForge(){
		
		if(!isForgeViewActive())
			openForgeView();
		
		SWTBotView view = getForgeView();
		/* view.toolbarButton("Stop the running Forge runtime").click(); */
		//not working with juno ^^^
		//workaround:
		ViewUtils.getToolbarButton("Stop the running Forge runtime").click();
		
		bot.sleep(TIME_5S);
	}
	
	public static SWTBotStyledText getStyledText(){
		SWTBotView view = getForgeView();
		PageBook pb = view.bot().widget(widgetOfType(PageBook.class));
		SWTBot pbbot = new SWTBot(pb);
		return pbbot.styledText();
	}
	
	public static SWTBot getForgeViewBot(){
		return bot.viewByTitle("Forge Console").bot();
	}
	
}

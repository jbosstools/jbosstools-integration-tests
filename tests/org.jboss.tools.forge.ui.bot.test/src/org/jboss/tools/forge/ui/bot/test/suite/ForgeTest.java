package org.jboss.tools.forge.ui.bot.test.suite;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.forge.ui.bot.test.util.ConsoleUtils;
import org.jboss.tools.forge.ui.bot.test.util.ViewUtils;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * 
 * @author psrna
 *
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present))
public class ForgeTest extends SWTTestExt {

	protected static final String PROJECT_NAME = "testproject";
	protected static final String PACKAGE_NAME = "org.jboss.testproject";
	
	protected static ProjectExplorer pExplorer = null;
	
	@BeforeClass
	public static void setup(){
		pExplorer = new ProjectExplorer();
		openForgeView();
		startForge();
		clear();
	}
	
	@AfterClass
	public static void cleanup(){
		//TODO?
	}
	
	public enum ProjectTypes{
		jar, war, pom
	}
	
	protected void createProject(){
		getStyledText().setText("new-project \n");
		
		getStyledText().setText(PROJECT_NAME + "\n");
		getStyledText().setText(PACKAGE_NAME + "\n");
		getStyledText().setText("Y\n");
		
		ConsoleUtils.waitUntilTextInConsole("project [" + PROJECT_NAME + "]", TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs();
	}
	
	protected void createProject(ProjectTypes type){
		
		getStyledText().setText("new-project --type " + type + "\n");
		
		getStyledText().setText(PROJECT_NAME + "\n");
		getStyledText().setText(PACKAGE_NAME + "\n");
		getStyledText().setText("Y\n");
	
		ConsoleUtils.waitUntilTextInConsole("project [" + PROJECT_NAME + "]", TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs();
	}
	
	protected void createPersistence(){
		
		getStyledText().setText("persistence setup\n");
		getStyledText().setText("HIBERNATE\n");
		getStyledText().setText("JBOSS_AS7\n");
		getStyledText().setText("\n"); //accept default java-ee-spec
		getStyledText().setText("N\n");
	
		ConsoleUtils.waitUntilTextInConsole("persistence.xml", TIME_1S, TIME_20S*3);
		util.waitForNonIgnoredJobs();
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
	
	public static void clear() {
		if(!isForgeViewActive())
			openForgeView();
		if(!isForgeRunning())
			return;
		
		getStyledText().setText("clear\n");
		bot.sleep(TIME_5S);
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

		util.waitForNonIgnoredJobs();
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

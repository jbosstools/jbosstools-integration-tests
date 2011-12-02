package org.jboss.tools.forge.ui.bot.test.suite;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;

/**
 * 
 * @author psrna
 *
 */
public class ForgeTest extends SWTTestExt {

	protected PackageExplorer pExplorer = new PackageExplorer();
	
	protected SWTBotView openForgeView(){
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
	
	protected void clear() {
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
	private SWTBotView getForgeView(){
		SWTBotView view = bot.viewByTitle("Forge Console");
		view.setFocus();
		view.show();
		return view;
	}
	
	protected boolean isForgeViewActive(){
		
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
	
	protected boolean isForgeRunning(){
		
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
	
	protected void startForge(){
		
		if(!isForgeViewActive())
			openForgeView();
		
		SWTBotView view = getForgeView();
		view.toolbarButton("Start Forge").click();
		bot.sleep(TIME_5S);
	}
	
	protected void stopForge(){
		
		if(!isForgeViewActive())
			openForgeView();
		
		SWTBotView view = getForgeView();
		view.toolbarButton("Stop Forge").click();
		bot.sleep(TIME_1S);
	}
	
	protected SWTBotStyledText getStyledText(){
		SWTBotView view = getForgeView();
		PageBook pb = view.bot().widget(widgetOfType(PageBook.class));
		SWTBot pbbot = new SWTBot(pb);
		return pbbot.styledText();
	}
	
}

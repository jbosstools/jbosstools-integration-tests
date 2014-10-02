package org.jboss.tools.forge.reddeer.view;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ToolItemMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolBar;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
/**
 * Forge Console view RedDeer implementation
 * @author psrna
 *
 */
public class ForgeConsoleView extends WorkbenchView{

	protected final static Logger log = Logger.getLogger(ForgeConsoleView.class);
	
	public ForgeConsoleView() {
		super("Forge Console");
	}
	
	/**
	 * Start currently selected Forge Runtime
	 * Call {@link #selectRuntime(RegexMatcher)} if there is a need to start different runtime 
	 * as the one that is currently selected.
	 */
	public void start(){
		
		log.info("Attempt to START Forge Runtime");
		new ForgeConsoleView().open();
		ViewToolBar tb = new ViewToolBar();
		
		RegexMatcher rm = new RegexMatcher("Start.*");
		WithTooltipTextMatcher tm = new WithTooltipTextMatcher(rm);
		new DefaultToolItem(tb, 0, tm).click();		
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);	
	}
	
	/**
	 * Stop Forge Console
	 */
	public void stop(){
		
		log.info("Attempt to STOP Forge Runtime");
		new ForgeConsoleView().open();
		ViewToolBar tb = new ViewToolBar();
		
		RegexMatcher rm = new RegexMatcher("Stop.*");
		WithTooltipTextMatcher tm = new WithTooltipTextMatcher(rm);
		new DefaultToolItem(tb, 0, tm).click();	
		
		new WaitWhile(new JobIsRunning());	
	}
	
	/**
	 * Clear Forge Console
	 */
	public void clear(){
		
		log.info("Attempt to CLEAR Forge Runtime");
		new ForgeConsoleView().open();
		ViewToolBar tb = new ViewToolBar();
		
		WithTooltipTextMatcher tm = new WithTooltipTextMatcher("Clear Console");
		new DefaultToolItem(tb, 0, tm).click();
	}
	
	/**
	 * Select Forge Runtime
	 * @param rm RegexMatcher witch matches Forge Runtime version. e.g. "Forge 2.*" or "Forge 1.*"
	 */
	@SuppressWarnings("unchecked")
	public void selectRuntime(RegexMatcher rm){
		
		log.info("Attempt to SELECT Forge Runtime");
		new ForgeConsoleView().open();
		ViewToolBar tb = new ViewToolBar();
		
		WithTooltipTextMatcher tm = new WithTooltipTextMatcher("Select Forge Runtime");
		DefaultToolItem defaultToolItem = new DefaultToolItem(tb, 0, tm);	
		
		new ToolItemMenu(defaultToolItem, rm).select();
	}
	
}

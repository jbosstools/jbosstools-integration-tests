package org.jboss.tools.ui.bot.ext.condition;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.List;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.ui.bot.ext.SWTBotExt;

/**
 * Checks if active shell contains at least one 
 * instance of widget (specified in constructor)
 * 
 * @author jjankovi
 *
 */
public class ActiveShellContainsWidget implements ICondition {

	private Class<? extends Widget> widgetClass = null;
	private SWTBotExt bot = null;
	
	public ActiveShellContainsWidget(SWTBotExt bot, Class<? extends Widget> clazz) {
		this.widgetClass = clazz;
		this.bot = bot;
	}
	
	@Override
	public boolean test() throws Exception {
		List<? extends Widget> widgets = bot.activeShell().bot().widgets(widgetOfType(widgetClass));
		return !widgets.isEmpty();
	}

	@Override
	public void init(SWTBot bot) {
		// do nothing here
	}

	@Override
	public String getFailureMessage() {
		return "Active shell doesn't contain any widget of type '" + widgetClass + "'";
	}

}

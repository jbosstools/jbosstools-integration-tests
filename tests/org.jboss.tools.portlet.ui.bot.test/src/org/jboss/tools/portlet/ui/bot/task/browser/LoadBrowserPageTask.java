package org.jboss.tools.portlet.ui.bot.task.browser;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;

/**
 * Displays the given page in internal browser. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class LoadBrowserPageTask extends AbstractSWTTask {

	private String url;
	
	public LoadBrowserPageTask(String url) {
		super();
		this.url = url;
	}

	@Override
	public void perform() {
		SWTBotBrowserExt browser = SWTBotFactory.getBot().browserExt();
		browser.loadUrlToBrowser(url, SWTBotFactory.getBot());
	}
}

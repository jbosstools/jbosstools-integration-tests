package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLink;

/**
 * Represents a link widget.
 * 
 * @author apodhrad
 * 
 */
public class Link {

	private SWTBotLink link;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 

	public Link(String label) {
		link = bot.link("<a>" + label + "</a>");
	}

	public void click() {
		link.click();
	}
}

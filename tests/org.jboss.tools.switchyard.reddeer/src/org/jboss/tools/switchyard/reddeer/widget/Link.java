package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotLink;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Represents a link widget.
 * 
 * @author apodhrad
 * 
 */
public class Link {

	private SWTBotLink link;

	public Link(String label) {
		link = Bot.get().link("<a>" + label + "</a>");
	}

	public void click() {
		link.click();
	}
}

package org.jboss.tools.portlet.ui.bot.test.task;

import org.eclipse.swtbot.swt.finder.SWTBot;

/**
 * Marking that the class should be aware of SWT bot. 
 * 
 * @author ljelinko
 *
 */
public interface SWTBotAware {

	 void setBot(SWTBot bot);
}

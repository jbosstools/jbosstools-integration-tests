package org.jboss.tools.ui.bot.ext.widgets;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.ui.forms.widgets.Section;
/**
 * this class represents Section (expandable labeled area, 
 * section is often used as a basic building block if forms 
 * because it provides for logical grouping of information.)
 * @author lzoubek
 *
 */
public class SWTBotSection extends AbstractSWTBotControl<Section> {

	@Override
	protected AbstractSWTBotControl<Section> click(boolean post) {
		// TODO Auto-generated method stub
		return super.click(post);
	}
	private final SWTBot bot;
	public SWTBotSection(Section w) throws WidgetNotFoundException {
		super(w);
		bot = new SWTBot(w);
	}
	public SWTBot bot() {
		return bot;
	}

}

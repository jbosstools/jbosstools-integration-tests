package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.jboss.tools.switchyard.reddeer.utils.ControlUtils;
import org.jboss.tools.switchyard.reddeer.utils.MouseUtils;

/**
 * Represents a radio button widget.
 * 
 * @author apodhrad
 * 
 */
public class RadioButton extends org.jboss.reddeer.swt.impl.button.RadioButton {

	private SWTBotRadio radio;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 

	public RadioButton(String label) {
		radio = bot.radio(label);
	}

	@Override
	public void click() {
		Point point = ControlUtils.getAbsolutePoint(radio.widget);
		MouseUtils.click(point.x, point.y);
	}

}

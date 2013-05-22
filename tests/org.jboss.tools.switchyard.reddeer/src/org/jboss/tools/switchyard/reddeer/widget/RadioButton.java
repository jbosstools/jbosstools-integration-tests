package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Represents a radio button widget.
 * 
 * @author apodhrad
 * 
 */
public class RadioButton extends org.jboss.reddeer.swt.impl.button.RadioButton {

	private SWTBotRadio radio;

	public RadioButton(String label) {
		radio = Bot.get().radio(label);
	}

	@Override
	public void click() {
		new SWTBotRadioFixed(radio).click();
	}

	public class SWTBotRadioFixed extends SWTBotRadio {

		public SWTBotRadioFixed(SWTBotRadio radio) {
			super(radio.widget);
		}

		public SWTBotRadioFixed(Button w) throws WidgetNotFoundException {
			super(w);
		}

		@Override
		/**
		 * Selects the radio button.
		 */
		public SWTBotRadio click() {
			if (isSelected()) {
				log.debug(MessageFormat.format("Widget {0} is already selected, not clicking again.", this)); //$NON-NLS-1$
				return this;
			}
			waitForEnabled();

			log.debug(MessageFormat.format("Clicking on {0}", this)); //$NON-NLS-1$

			notify(SWT.Activate);
			notify(SWT.MouseDown, createMouseEvent(0, 0, 1, 0, 1));
			notify(SWT.MouseUp, createMouseEvent(0, 0, 1, SWT.BUTTON1, 1));
			asyncExec(new VoidResult() {
				public void run() {
					widget.setSelection(true);
				}
			});
			notify(SWT.Selection);

			log.debug(MessageFormat.format("Clicked on {0}", this)); //$NON-NLS-1$
			return this;
		}

	}

}

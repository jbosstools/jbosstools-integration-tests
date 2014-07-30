package org.jboss.tools.ui.bot.ext.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList;
import org.hamcrest.SelfDescribing;

/**
 * This class represents TabbedPropertyList widget used in Tabbed Properties
 * view
 * 
 * @author apodhrad
 * 
 */
@SuppressWarnings("restriction")
public class SWTBotTabbedPropertyList extends AbstractSWTBotControl<TabbedPropertyList> {

	/**
	 * Constructs an instance of this object with the given TabbedPropertyList
	 * 
	 * @param w
	 * @throws WidgetNotFoundException
	 */
	public SWTBotTabbedPropertyList(TabbedPropertyList w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * Select a tab in the view located on index
	 * 
	 * @param index
	 */
	public void selectTab(final int index) {
		syncExec(new VoidResult() {

			@Override
			public void run() {
				Object element = widget.getElementAt(index);
				if (element instanceof Control) {
					ClickControl clickControl = new ClickControl((Control) element, null);
					clickControl.click();
				}
			}
		});
	}

	/**
	 * Select a tab in the view with the given mnemonic text
	 * 
	 * @param mnemonicText
	 */
	public void selectTab(final String label) {
		syncExec(new VoidResult() {

			@Override
			public void run() {
				Object element = null;
				int i = 0;
				while ((element = widget.getElementAt(i)) != null) {
					TabbedPropertyList.ListElement listElement = (TabbedPropertyList.ListElement) element;
					if (listElement.toString().equals(label)) {
						selectTab(i);
					}
					i++;
				}
			}
		});
	}

	/**
	 * Help class
	 */
	private static class ClickControl extends AbstractSWTBotControl<Control> {

		public ClickControl(Control w, SelfDescribing description) throws WidgetNotFoundException {
			super(w);
		}

		public ClickControl click() {
			log.debug(MessageFormat.format("Clicking on {0}", SWTUtils.getText(widget)));
			waitForEnabled();
			notify(SWT.MouseEnter);
			notify(SWT.MouseMove);
			notify(SWT.Activate);
			notify(SWT.FocusIn);
			notify(SWT.MouseDown);
			notify(SWT.MouseUp);
			notify(SWT.Selection);
			notify(SWT.MouseHover);
			notify(SWT.MouseMove);
			notify(SWT.MouseExit);
			notify(SWT.Deactivate);
			notify(SWT.FocusOut);
			log.debug(MessageFormat.format("Clicked on {0}", SWTUtils.getText(widget)));
			return this;
		}
	}
}

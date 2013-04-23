package org.jboss.tools.bpmn2.itests.editor.properties;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.internal.SiblingFinder;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarPushButton;
import org.eclipse.ui.forms.widgets.Section;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;

/**
 * 
 * @author Andrej Podhradsky <apodhrad@redhat.com>
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2PropertiesView extends WorkbenchView {

	/**
	 * 
	 */
	public BPMN2PropertiesView() {
		super("General", "Properties");
		open();
	}
	
	/**
	 * 
	 * @param label
	 */
	public void selectTab(String label) {
		new ListElement(label).select();
	}

	/**
	 * 
	 */
	public String getTitle() {
		return Bot.get().clabel().getText();
	}
	
	/**
	 * 
	 * @param checkBox
	 * @param select
	 */
	public void selectCheckBox(CheckBox checkBox, boolean select) {
		if ((checkBox.isChecked() && !select) || (!checkBox.isChecked() && select)) {
			checkBox.click();
		}
	}
	
	/**
	 * 
	 * @param checkBox
	 * @param select
	 */
	public void selectCheckBox(SWTBotCheckBox checkBox, boolean select) {
		if ((checkBox.isChecked() && !select) || (!checkBox.isChecked() && select)) {
			checkBox.click();
		}
	}
	
	/**
	 * 
	 * @param section
	 * @param tooltip
	 */
	public void clickToolbarButton(final String section, final String tooltip) {
		final Widget widget = Bot.get().label(section).widget;
		widget.getDisplay().asyncExec(new Runnable() {
			
			public void run() {
				Widget[] siblings = new SiblingFinder(widget).run();
				for (Widget sibling : siblings) {
					if (sibling instanceof ToolBar) {
						ToolItem[] items = ((ToolBar) sibling).getItems();
						for (ToolItem item : items) {
							if (item.getToolTipText().equals(tooltip)) {
								new SWTBotToolbarPushButton(item).click();
								return;
							}
						}
					}
				}		
				
			}
			
		});
		
		throw new WidgetNotFoundException("Toolbar button in section '" + section + "' with tooltip '" + tooltip + "' was not found.");
	}

	/**
	 * 
	 * @param label
	 * @return
	 */
	public int indexOfSection(final String label) {
		return UIThreadRunnable.syncExec(new Result<Integer>() {

			public Integer run() {
				List<? extends Widget> widgets = Bot.get().widgets(new BaseMatcher<Widget>() {

					public void describeTo(Description description) {
						// ignore
					}

					public boolean matches(Object item) {
						return (item instanceof Section);
					}

				});
				for (int i = 0; i < widgets.size(); i++) {
					Section s = (Section) widgets.get(i);
					if (label.equals(s.getText())) {
						return i;
					}
					
				}
				return -1;
			}
		});
	}
	
//	/**
//	 * 
//	 * @param shell
//	 * @param field
//	 * @param value
//	 */
//	public void addNewObjectDefinition(SWTBot bot, String[] field, String[] value) {
//		for (int i=0; i<field.length; i++) {
//			bot.textWithLabel(field[i]).setText(value[i]);
//		}
//	}
//	
//	/**
//	 * 
//	 * @param dataType
//	 */
//	public void addNewDataType(SWTBot bot, String dataType) {
//		bot.comboBoxWithLabel("Item Kind").setSelection("Physical");
//		bot.textWithLabel("Data Type").setText(dataType);
//		bot.button("OK").click();
//	}
	
	/**
	 * 
	 * @author Andrej Podhradsky <apodhrad@redhat.com>
	 */
	private class ListElement {

		private Canvas canvas;

		/**
		 * 
		 * @param label
		 */
		public ListElement(String label) {
			canvas = (Canvas) Bot.get().widget(new ListElementWithLabel(label));
		}

		/**
		 * 
		 */
		public void select() {
			new ClickControl(canvas).click();
		}

	}
	
	/**
	 * 
	 * @author Andrej Podhradsky <apodhrad@redhat.com>
	 */
	private class ListElementWithLabel extends BaseMatcher<Widget> {

		private String label;

		/**
		 * 
		 * @param label
		 */
		public ListElementWithLabel(String label) {
			this.label = label;
		}

		/**
		 * 
		 */
		public boolean matches(Object item) {
			if (item.getClass().getSimpleName().equals("ListElement")) {
				return item.toString().equals(label);
			}
			return false;
		}

		/**
		 * 
		 */
		public void describeTo(Description description) {
			description.appendText("of class '").appendText(label).appendText("'");
		}
	}
	
	/**
	 * 
	 * @author Andrej Podhradsky <apodhrad@redhat.com>
	 */
	private class ClickControl extends AbstractSWTBotControl<Composite> {

		/**
		 * 
		 * @param w
		 */
		public ClickControl(Composite w) {
			super(w);
		}

		/**
		 * 
		 */
		public ClickControl click() {
			waitForEnabled();
			notify(SWT.MouseDown);
			notify(SWT.MouseUp);
			return this;
		}
	}
	
}

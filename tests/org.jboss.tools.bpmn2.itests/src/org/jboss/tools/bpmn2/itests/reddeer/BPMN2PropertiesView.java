package org.jboss.tools.bpmn2.itests.reddeer;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.internal.SiblingFinder;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarPushButton;
import org.eclipse.ui.forms.widgets.Section;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;

/**
 * 
 * @author Andrej Podhradsky <apodhrad@redhat.com>
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BPMN2PropertiesView extends WorkbenchView {

	SWTBot bot = new SWTBot();
	
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
		open();
		new ListElement(label).select();
	}

	/**
	 * 
	 */
	public String getTitle() {
		return bot.clabel().getText();
	}
	
	/**
	 * 
	 * @param comboBox
	 * @param item
	 * @return
	 */
	public boolean contains(SWTBotCombo comboBox, String item) {
		if (item == null || item.isEmpty())
			return false;
		
		return Arrays.asList(comboBox.items()).contains(item);
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
	public SWTBotToolbarPushButton toolbarButton(final String section, final String tooltip) {
		SWTBotToolbarPushButton toolbarButton = UIThreadRunnable.syncExec(new Result<SWTBotToolbarPushButton>() {

			public SWTBotToolbarPushButton run() {
				Widget widget = bot.label(section).widget;
				Widget[] siblings = new SiblingFinder(widget).run();
				for (Widget sibling : siblings) {
					if (sibling instanceof ToolBar) {
						ToolItem[] items = ((ToolBar) sibling).getItems();
						for (ToolItem item : items) {
							if (item.getToolTipText().equals(tooltip)) {
								return new SWTBotToolbarPushButton(item);
							}
						}
					}
				}		
				return null;
			}
			
		});
		
		if (toolbarButton == null) {
			throw new WidgetNotFoundException("Toolbar button in section '" + section + "' with tooltip '" + tooltip + "' was not found.");
		}
		
		return toolbarButton;
	}

	/**
	 * 
	 * @param label
	 * @return
	 */
	public int indexOfSection(final String label) {
		return UIThreadRunnable.syncExec(new Result<Integer>() {

			public Integer run() {
				List<? extends Widget> widgets = bot.widgets(new BaseMatcher<Widget>() {

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
	
	protected void activateShell(SWTWorkbenchBot bot) {
		try {
			bot.activeShell();
		} catch (WidgetNotFoundException e) {
			SWTBotShell[] shellArray = bot.shells();
			for (SWTBotShell shell : shellArray) {
				if (shell.getText() != null && shell.getText().endsWith("Eclipse Platform")) {
					shell.activate();
					return;
				}
			}
			throw new WidgetNotFoundException("Main shell was not found!");
		}
	}
	
	@Override
	public void minimize() {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		activateShell(bot);
		bot.menu("Window").menu("Navigation").menu("Minimize Active View or Editor").click();
	}
	
	@Override
	public void maximize() {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		activateShell(bot);
		bot.menu("Window").menu("Navigation").menu("Maximize Active View or Editor").click();
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
			canvas = (Canvas) bot.widget(new ListElementWithLabel(label));
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

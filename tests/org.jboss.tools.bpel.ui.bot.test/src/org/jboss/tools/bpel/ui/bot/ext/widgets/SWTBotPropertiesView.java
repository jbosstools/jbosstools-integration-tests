package org.jboss.tools.bpel.ui.bot.ext.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.matchers.IsInstanceOf;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.ControlFinder;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

/**
 * 
 * @author mbaluch
 */
public class SWTBotPropertiesView extends SWTBotView {

	Matcher<Composite> matcher = new IsInstanceOf<Composite>(
			Composite.class);
	
	/** 
	 * Creates a new instance of SWTBotPropertiesView
	 * 
	 * @param bot
	 */
	public SWTBotPropertiesView(SWTWorkbenchBot bot) {
		super(bot.viewByTitle("Properties").getReference(), bot);
		show();
	}
	
	/**
	 * Select a tab in the view located on index
	 * 
	 * @param index index of tab to select
	 */
	public void selectTab(int index) {
		List<Composite> found = UIThreadRunnable.syncExec(new FindTabsResult());
		Composite foundTab = found.get(index);
		CompositeControl mw = new CompositeControl(foundTab, matcher);
		mw.click();
	}
	
	/**
	 * Select a tab in the view with the given mnemonic text
	 * 
	 * @param mnemonicText
	 */
	public void selectTab(String mnemonicText) {
	}
	
	
	/**
	 * Help class
	 */
	private class FindTabsResult implements Result<List<Composite>> {
		
		public List<Composite> run() {
			Composite parent = (Composite) getWidget();
			ControlFinder cf = new ControlFinder();

			List<Composite> findControls = cf.findControls(parent, matcher, true);
			List<Composite> tabs = new ArrayList<Composite>();
			for (int i = 0; i < findControls.size(); i++) {
				Composite c = findControls.get(i);
				String className = SWTUtils.toString(c);
				if (className.startsWith("TabbedPropertyList$ListElement")) {
					tabs.add(c);
				}
			}

			return tabs;
		}
		
	}
	
	/**
	 * Help class
	 */
	private static class CompositeControl extends AbstractSWTBotControl<Composite> {

		public CompositeControl(Composite w, SelfDescribing description) throws WidgetNotFoundException {
			super(w);
		}

		public CompositeControl click() {
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
	
	
//	/**
//	 * Help class
//	 */
//	private class SelectTabResult implements VoidResult {
//		
//		int tab;
//		
//		public SelectTabResult(int tab) {
//			this.tab = tab;
//		}
//		
//		public void run() {
//			Composite parent = (Composite) getWidget();
//			ControlFinder cf = new ControlFinder();
//
//			Matcher<Composite> matcher = new IsInstanceOf<Composite>(
//					Composite.class);
//
//			List<Composite> findControls = cf.findControls(parent, matcher, true);
//			List<Composite> tabs = new ArrayList<Composite>();
//			for (int i = 0; i < findControls.size(); i++) {
//				Composite c = findControls.get(i);
//				String className = SWTUtils.toString(c);
//				if (className.startsWith("TabbedPropertyList$ListElement")) {
//					tabs.add(c);
//				}
//			}
//
//			Composite foundTab = tabs.get(tab);
//			CompositeControl mw = new CompositeControl(foundTab, matcher);
//			mw.click();
//		}
//		
//	}
}

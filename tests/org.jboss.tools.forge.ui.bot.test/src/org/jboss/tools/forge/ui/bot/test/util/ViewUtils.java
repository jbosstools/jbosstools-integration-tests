package org.jboss.tools.forge.ui.bot.test.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarPushButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarRadioButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarSeparatorButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarToggleButton;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPartReference;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;


public class ViewUtils {

	public static List<SWTBotToolbarButton> getToolbarButtons() {
		return UIThreadRunnable.syncExec(new ListResult<SWTBotToolbarButton>() {

			public List<SWTBotToolbarButton> run() {
				SWTBotView view = ForgeTest.openForgeView();
				IWorkbenchPart obj = ((WorkbenchPartReference) view.getReference()).getPart(false);
				ToolBar toolbar = null;
				IToolBarManager t = ((IViewSite)obj.getSite()).getActionBars().getToolBarManager();
				if (t instanceof ToolBarManager) {
				    toolbar = ((ToolBarManager)t).getControl();
				}

				final List<SWTBotToolbarButton> l = new ArrayList<SWTBotToolbarButton>();

				if (toolbar == null)
					return l;

				ToolItem[] items = toolbar.getItems();
				for (int i = 0; i < items.length; i++) {
					try {
						if (SWTUtils.hasStyle(items[i], SWT.PUSH))
							l.add(new SWTBotToolbarPushButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.CHECK))
							l.add(new SWTBotToolbarToggleButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.RADIO))
							l.add(new SWTBotToolbarRadioButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.DROP_DOWN))
							l.add(new SWTBotToolbarDropDownButton(items[i]));
						else if(SWTUtils.hasStyle(items[i], SWT.SEPARATOR))
							l.add(new SWTBotToolbarSeparatorButton(items[i]));
					} catch (WidgetNotFoundException e) {
						e.printStackTrace();
					}
				}
				return l;
			}
		});
	}
	
	public static SWTBotToolbarButton getToolbarButton(String tooltip){
		
		List<SWTBotToolbarButton> btns = ViewUtils.getToolbarButtons();
		for (SWTBotToolbarButton btn : btns){
			if(btn.getToolTipText().equals(tooltip)){
				return btn;
			}
		}
		return null;
	}
	
}

package org.jboss.tools.ui.bot.ext.view;

import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GeneralErrorLog;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;

/**
 * Error Log view SWTBot Extensions
 * 
 * @author jpeterka
 * 
 */
public class ErrorLogView extends ViewBase {

	public ErrorLogView() {
		viewObject = GeneralErrorLog.LABEL;
	}
	
	/**
	 * logs all error log messages into a logger
	 */
	public void logMessages() {
		SWTBotTreeItem[] items = getView().bot().tree().getAllItems();
		for (SWTBotTreeItem i : items)
			passTree(i);
	}
	
	public SWTBotTreeItem[] getMessages() {
		return getView().bot().tree().getAllItems();
	}

	private void passTree(SWTBotTreeItem item) {
		int i = 0;
		item.expand();
		while (!item.getNodes().isEmpty() && i < item.getNodes().size()) {
			passTree(item.getNode(i));
			i++;
		}
		log.info(item.getText());
	}

	/**
	 * clear error log (temporarily)
	 */
	public void clear() {
		ContextMenuHelper.clickContextMenu(getView().bot().tree(),
				"Clear Log Viewer");
		
		log.info("Error log cleared");
	}
	
	/**
	 * deletes error log permanently
	 */
	public void delete() {

		try {
			ContextMenuHelper.clickContextMenu(getView().bot().tree(),
					"Delete Log");
			
			SWTBotShell shell = bot.shell("Confirm Delete").activate();
			shell.bot().button("OK").click();
			bot.waitUntil(Conditions.shellCloses(shell));
			log.info("Error log content deleted");
		} catch (WidgetNotFoundException e) {
			log.warn("Error log is empty or can't be deleted");
		} catch (RuntimeException ex){
			if (ex.getCause() instanceof NotEnabledException){
				log.warn("Error log is empty or can't be deleted");
			}else{
				throw ex;
			}
		}
	}

	private SWTBotView getView() {
		SWTBotView view = open.viewOpen(ActionItem.View.GeneralErrorLog.LABEL);
		return view;
	}

	/**
	 * 
	 * @return all item count in the error log tree
	 */
	public int getRecordCount() {
		int count = getView().bot().tree().getAllItems().length;
		return count;
	}

}

package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;

public class SQLResultView extends WorkbenchView {

	public SQLResultView() {
		super("Data Management", "SQL Results");
	}

	public SQLResult getByOperation(String operation) {
		SWTBotTreeItem found = null;

		SWTBotTreeItem[] items = new SWTWorkbenchBot().tree(1).getAllItems();
		for (SWTBotTreeItem item : items) {
			if (item.cell(1).trim().equals(operation)) {
				found = item;
				break;
			}
		}

		if (found == null) {
			throw new WidgetNotFoundException("Cannot find sql result for operation " + operation);
		}

		found.click();
		return new SQLResult(found);
	}
}

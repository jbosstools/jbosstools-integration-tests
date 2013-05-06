package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.swt.util.Bot;

public class SQLResult {

	public static final String STATUS_SUCCEEDED = "Succeeded";
	
	private SWTBotTreeItem resultRow;
	
	public SQLResult(SWTBotTreeItem resultRow) {
		this.resultRow = resultRow;
	}

	public String getStatus(){
		return resultRow.cell(0);
	}
	
	public int getCount(){
		Bot.get().cTabItem("Result1").activate();
		return Bot.get().table().rowCount();
	}
	
}

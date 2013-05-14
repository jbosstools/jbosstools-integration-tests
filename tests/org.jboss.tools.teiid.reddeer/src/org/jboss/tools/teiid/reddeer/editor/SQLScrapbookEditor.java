package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.util.Bot;



public class SQLScrapbookEditor extends SWTBotEditor {

	public SQLScrapbookEditor() {
		this("SQL Scrapbook 0");
	}
	
	public SQLScrapbookEditor(String name) {
		super(Bot.get().editorByTitle(name).getReference(), Bot.get());
	}

	public void setDatabase(String dbName){
		Bot.get().comboBoxWithLabel("Database:").setSelection(dbName);
	}
	
	public void setText(String text){
		Bot.get().styledText().setText(text);
	}
	
	public void executeAll(){
		Bot.get().styledText().contextMenu("Execute All").click();
		
		SWTBotShell shell = Bot.get().shell("SQL Statement Execution");
		Bot.get().waitUntil(Conditions.shellCloses(shell), 60 * 1000);
	}
}

package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.impl.button.PushButton;

public class CriteriaBuilder {

	private SWTBotShell shell;
	
	public CriteriaBuilder(SWTBotShell shell) {
		this.shell = shell;
	}
	
	public void selectLeftAttribute(String table, String attribute){
		shell.bot().tree(1).setFocus();
		shell.bot().tree(1).expandNode(table).select(getAttributeName(table, attribute));
	}
	
	public void selectRightAttribute(String table, String attribute){
		shell.bot().tree(2).setFocus();
		shell.bot().tree(2).expandNode(table).select(getAttributeName(table, attribute));
	}
	
	public void apply(){
		shell.bot().button("Apply").click();
	}
	
	public void finish(){
		new PushButton("OK").click();
	}
	
	private String getAttributeName(String table, String attribute){
		return table + "." + attribute;
	}
}

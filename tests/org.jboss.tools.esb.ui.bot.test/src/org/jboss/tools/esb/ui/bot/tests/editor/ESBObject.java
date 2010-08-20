package org.jboss.tools.esb.ui.bot.tests.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public abstract class ESBObject {

	protected static final SWTBotExt bot = new SWTBotExt();
	protected final String uiName;
	public final String xmlName;
	
	public ESBObject(String uiName, String xmlName) {
		this.uiName=uiName;
		this.xmlName=xmlName;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			return this.uiName.equals(((ESBObject)obj).uiName);
		}
		return super.equals(obj);
	}
	public String getBaseXPath() {
		return "//jbossesb/";
	}
	public String getShellTitle() {
		return "Add "+this.uiName;
	}
	public String getMenuLabel() {
		return this.uiName+"...";
	}
	public String getSectionTitle() {
		return this.uiName;
	}
	public String getXpath() {
		return this.xmlName+"[@name='"+this.uiName+"']";
	}
	public String getFinishButton() {
		return IDELabel.Button.FINISH;
	}
	
	protected abstract void doFillForm(SWTBotShell shell);
	protected abstract void doEditing(SWTBotEditor editor, String... path);
	public abstract void create(SWTBotEditor editor, String... path);
	
	protected void editTextProperty(SWTBotEditor editor,SWTBot ebot,String withLabel, String name, String value) {
		ebot.textWithLabel(withLabel).setText(value);
		editor.save();
		String text = editor.toTextEditor().getText();
		String xpath = "count("+getBaseXPath()+getXpath()+"/property[@name='"+name+"' and @value='"+value+"'])=1";
		Assertions.assertXmlContentBool(text, xpath);
	}
	
	
	protected SWTBotShell openForm(SWTBotEditor editor, String... path) {
		editor.show();
		SWTBotTreeItem provItem = SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		ContextMenuHelper.prepareTreeItemForContextMenu(editor.bot().tree(), provItem);
		ContextMenuHelper.clickContextMenu(editor.bot().tree(), IDELabel.Menu.NEW,getMenuLabel());	
		return new SWTBot().shell(getShellTitle());
	}
	
	public static String[] arrayAppend(String[] array, String... items) {
		String[] ret = new String[array.length+items.length];
		int i=0;
		for (i=0;i<array.length;i++) {
			ret[i] = array[i];
		}
		for (int j=0;j<items.length;j++) {
			ret[i+j] = items[j];
		}				
		return ret;
	}
	
	


}

package org.jboss.tools.esb.ui.bot.tests.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;

public class ESBListener extends ESBObject {

	private String service;
	
	public ESBListener(String uiName, String xmlName) {
		super(uiName, xmlName);
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	@Override
	public String getBaseXPath() {
		return "//jbossesb/services/service[@name='"+getService()+"']/listeners/";
	}
	protected void doFillForm(SWTBotShell shell) {
			Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
			shell.bot().text(0).setText(this.uiName);
			Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
	}
	protected void doEditing(SWTBotEditor editor, String... path) {
		 
	}
	
	public void edit(SWTBotEditor editor, String... path) {
		editor.show();		
		SWTEclipseExt.selectTreeLocation(editor.bot(), path);
		doEditing(editor, (String[])null);
		editor.bot().sleep(5000);
		editor.save();
	}
	
	public void create(SWTBotEditor editor,  String... path) {
		SWTBotShell shell = openForm(editor, path);
		doFillForm(shell);
		shell.bot().button(getFinishButton()).click();
		String text = editor.toTextEditor().getText();
		String xpath="count("+getBaseXPath()+getXpath()+")=1";
		Assertions.assertXmlContentBool(text, xpath);
		editor.save();
	}

}

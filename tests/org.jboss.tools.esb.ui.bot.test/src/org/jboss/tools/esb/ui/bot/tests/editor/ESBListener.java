package org.jboss.tools.esb.ui.bot.tests.editor;

import static junit.framework.Assert.assertTrue;

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
		
		
		/* Added test for - https://issues.jboss.org/browse/JBQA-6527 - Add support for gateway messaging priority in the ESB editor  */
		if (this.uiName.contains("Gateway")) {
//			org.jboss.tools.ui.bot.ext.SWTUtilExt.displayAllBotWidgets(bot);
			assertTrue (editor.bot().comboBoxWithLabel("Message Flow Priority:").selectionIndex() == -1);			
			assertTrue (bot.comboBoxWithLabel("Message Flow Priority:").itemCount() == 11);
			String [] theItems = bot.comboBoxWithLabel("Message Flow Priority:").items();
			assertTrue (theItems.length == 11);
						
			assertTrue (theItems[1].equals("0"));
			assertTrue (theItems[2].equals("1"));
			assertTrue (theItems[3].equals("2"));
			assertTrue (theItems[4].equals("3"));
			assertTrue (theItems[5].equals("4"));
			assertTrue (theItems[6].equals("5"));
			assertTrue (theItems[7].equals("6"));
			assertTrue (theItems[8].equals("7"));
			assertTrue (theItems[9].equals("8"));
			assertTrue (theItems[10].equals("9"));
			bot.comboBoxWithLabel("Message Flow Priority:").setSelection(8);
					
			assertTrue (bot.comboBoxWithLabel("Message Flow Priority:").getText().equals("7"));
			editor.save();
		}
		
	}

}

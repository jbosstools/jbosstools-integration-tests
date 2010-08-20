package org.jboss.tools.esb.ui.bot.tests.editor;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class ESBProvider extends ESBObject {

	public ESBProvider(String uiName, String xmlName) {
		super(uiName, xmlName);
	}

	protected void doFillForm(SWTBotShell shell) {
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), false);
		shell.bot().text(0).setText(this.uiName);
		Assertions.assertButtonEnabled(shell.bot().button(IDELabel.Button.NEXT), true);
		shell.bot().button(IDELabel.Button.NEXT).click();
		Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), false);
		shell.bot().text(0).setText(this.uiName);
		Assertions.assertButtonEnabled(shell.bot().button(getFinishButton()), true);
	}
	
	protected void doEditing(SWTBotEditor editor, String... path) {
		 
	}
	
	public void create(SWTBotEditor editor, String... path) {
		SWTBotShell shell = openForm(editor, path);
		doFillForm(shell);
		shell.bot().button(getFinishButton()).click();
		String text = editor.toTextEditor().getText();
		String xpath="count(//jbossesb/providers/"+getXpath()+")=1";
		Assertions.assertXmlContentBool(text, xpath);
		editor.save();
	}

}

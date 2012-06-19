package org.jboss.ide.eclipse.as.ui.bot.test.wizard;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;

public class NewFileWizard {

	private SWTBotNewObjectWizard wizard = new SWTBotNewObjectWizard();
	
	private String[] path;
	
	private String fileName;
	
	private String text;
	
	public void execute(){
		wizard.open(ActionItem.NewObject.GeneralFile.LABEL.getName(), ActionItem.NewObject.GeneralFile.LABEL.getGroupPath().get(0));
		SWTEclipseExt.selectTreeLocation(SWTBotFactory.getBot(), path);
		SWTBotFactory.getBot().textWithLabel("File name:").setText(fileName);
		wizard.finishWithWait();
		
		if (text != null){
			SWTBotEditor editor = SWTBotFactory.getBot().editorByTitle(fileName);
			editor.toTextEditor().setText(text);
			editor.saveAndClose();
		}
	}
	
	public void setPath(String... path) {
		this.path = path;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}

package org.jboss.tools.jst.reddeer.web.ui;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class NewXHTMLFileWizardPage extends WizardPage{
	
	public void setFileName(String fileName){
		new LabeledText("File name:").setText(fileName);
	}
	
	public void selectParentFolder(String... path ){
		new DefaultTreeItem(path).select();
	}
	
	public void setParentFolder(String path){
		new DefaultText(0).setText(path);
	}
	
	public String getFileName(){
		return new LabeledText("File name:").getText();
	}
	
	public String getParentFolder(){
		return new DefaultText(0).getText();
	}
	
	

}

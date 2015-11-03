package org.jboss.tools.jst.reddeer.wst.html.ui.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard page for creating a HTML File.
 * @author vpakan
 */
public class NewHTMLFileWizardHTMLPage extends WizardPage {
	/**
	 * Sets a given file name.
	 * 
	 * @param name Name
	 */
	public void setFileName(String fileName){
		new LabeledText("File Name:").setText(fileName);
	}
	
	/**
	 * Gets a file name.
	 * 
	 */
	public String getFileName(){
		return new LabeledText("File Name:").getText();
	}
	/**
	 * Selects parent folder
	 * @param path
	 */
	public void selectParentFolder (String... path){
		new DefaultTreeItem(path).select();
	}
	/**
	 * Sets parent folder
	 * @param path
	 */
	public void setParentFolder(String path){
		new DefaultText(0).setText(path);
	}
	/**
	 * Gets parent folder
	 * @return
	 */
	public String getParentFolder(){
		return new DefaultText(0).getText();
	}
}

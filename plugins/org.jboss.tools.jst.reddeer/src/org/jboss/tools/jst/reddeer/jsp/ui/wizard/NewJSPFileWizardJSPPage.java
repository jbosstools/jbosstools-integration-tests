package org.jboss.tools.jst.reddeer.jsp.ui.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard page for creating a JSP File.
 * @author vpakan
 */
public class NewJSPFileWizardJSPPage extends WizardPage {
	/**
	 * Sets a given file name.
	 * 
	 * @param name Name
	 */
	public void setFileName(String fileName){
		new DefaultText(1).setText(fileName);
	}
	
	/**
	 * Gets a file name.
	 * 
	 */
	public String getFileName(){
		return new DefaultText(1).getText();
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

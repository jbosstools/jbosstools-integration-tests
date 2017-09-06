package org.jboss.tools.jst.reddeer.jsp.ui.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard page for creating a JSP File.
 * @author vpakan
 */
public class NewJSPFileWizardJSPPage extends WizardPage {
	
	public NewJSPFileWizardJSPPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}
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

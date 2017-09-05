package org.jboss.tools.jst.reddeer.jsp.ui.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;

/**
 * Wizard page for selecting template for new JSP File.
 * @author vpakan
 */
public class NewJSPFileWizardJSPTemplatePage extends WizardPage {
	
	public NewJSPFileWizardJSPTemplatePage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}
	/**
	 * Sets a given template.
	 * 
	 * @param name Name
	 */
	public void setTemplate(String template){
		new DefaultTable().select(template);
	}	
	/**
	 * Sets Use JSP template checkbox
	 * @param checked
	 */
	public void setUseJSPTemplate (boolean checked){
		new CheckBox("Use JSP Template").toggle(checked);
	}
	/**
	 * Gets Use XHTML template checkbox
	 */
	public boolean getUseJSPTemplate (){
		return new CheckBox("Use JSP Template").isChecked();
	}
}

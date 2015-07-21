package org.jboss.tools.jst.reddeer.wst.html.ui.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * Wizard page for selecting template for new HTML File.
 * @author vpakan
 */
public class NewHTMLFileWizardHTMLTemplatePage extends WizardPage {
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
		return new CheckBox("Use HTML Template").isChecked();
	}
}

package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard Page for New JavaJSF/Seam Portlet Wizard
 * 
 * @author psuchy
 * 
 */
public class NewJSFSeamPortletWizardPage extends WizardPage {

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}
	
	public void setDisplayName(String name) {
		new LabeledText("Display name:").setText(name);
	}
	
	public String getDisplayName() {
		return new LabeledText("Display name:").getText();
	}
	
	public void setTitle(String name) {
		new LabeledText("Title:").setText(name);
	}
	
	public String getTitle() {
		return new LabeledText("Title:").getText();
	}

	public void setProject(String projectName) {
		new LabeledCombo("Project:").setSelection(projectName);
	}

}
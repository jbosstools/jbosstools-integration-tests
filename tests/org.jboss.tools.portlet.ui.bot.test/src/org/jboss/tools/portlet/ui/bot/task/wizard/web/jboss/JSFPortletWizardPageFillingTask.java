package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

/**
 * JSF portlet specific information in Create Portlet wizard. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JSFPortletWizardPageFillingTask extends AbstractSWTTask implements WizardPageFillingTask {

	private String name;

	@Override
	public void perform() {
		if (name != null){
			getBot().textWithLabel("Name:").setText(name);
		}
	}

	public void setName(String name) {
		this.name = name;
	}
}
package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

/**
 * Fills the JBoss portlet specific wizard page (for selection of 
 * how the portlet libraries should be configured)
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossPortletCapabilitiesWizardPageFillingTask extends
		AbstractSWTTask implements WizardPageFillingTask {

	public enum Type {
		DISABLED("Disable Library Configuration"), 
		USER("User library"), 
		RUNTIME_PROVIDER("Portlet Target Runtime Provider");

		private String desc;

		private Type(String s) {
			desc = s;
		}

		@Override
		public String toString() {
			return desc;
		}
	}

	private Type type;

	public JBossPortletCapabilitiesWizardPageFillingTask(Type type) {
		super();
		this.type = type;
	}

	@Override
	public void perform() {
		getBot().comboBoxWithLabel("Type:").setSelection(type.toString());
	}
}

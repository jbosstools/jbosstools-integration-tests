package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

/**
 * Fills the JBoss JSF portlet specific wizard page (for selection of 
 * how the portlet libraries should be configured). 
 * 
 * This task tries to fill the value of portlet bridge runtime only if the field is visible, 
 * otherwise it is skipped (the wizard page shows field for portlet bridge selection only if 
 * it cannot determine it automatically from the server) 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossJSFPortletCapabilitiesWizardPageFillingTask extends
		AbstractSWTTask implements WizardPageFillingTask {

	public enum Type {
		DISABLED("Disable Library Configuration"), 
		USER("User library"), 
		RUNTIME_PROVIDER("JSF Portletbridge Runtime Provider");

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
	
	private String portletBridge;
	
	public JBossJSFPortletCapabilitiesWizardPageFillingTask(Type type) {
		super();
		this.type = type;
	}
	
	public JBossJSFPortletCapabilitiesWizardPageFillingTask(Type type, String portletBridge) {
		super();
		this.type = type;
		this.portletBridge = portletBridge;
	}

	@Override
	public void perform() {
		getBot().comboBoxWithLabel("Type:").setSelection(type.toString());
		
		try {
			getBot().textInGroup("Portletbridge Runtime").setText(portletBridge);
		} catch (WidgetNotFoundException e) {
			// ok, the portlet bridge is recognized in the server location
		}
	}
}

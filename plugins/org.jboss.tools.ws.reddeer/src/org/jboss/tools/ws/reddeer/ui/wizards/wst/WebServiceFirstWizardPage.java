package org.jboss.tools.ws.reddeer.ui.wizards.wst;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;

/**
 * First {@link WebServiceWizard} page.
 *
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public class WebServiceFirstWizardPage extends WebServiceWizardPageBase {

	/**
	 * There are two types of web service (top-down and bottom-up)
	 */
	public enum ServiceType {
		BOTTOM_UP, TOP_DOWN;

		String getDescription() {
			String prefix = "";
			switch (this) {
			case BOTTOM_UP:
				prefix += "Bottom up";
				break;
			case TOP_DOWN:
				prefix += "Top down";
				break;
			default:
				throw new IllegalArgumentException("Unknown type: " + this);
			}
			return prefix + " Java bean Web Service";
		}
	}

	/**
	 * Sets type of the service.
	 *
	 * @param type type of the service to be set
	 */
	public void setServiceType(ServiceType type) {
		getWebServiceTypeCombo().setSelection(type.getDescription());
	}

	/**
	 * Returns type of service.
	 */
	public ServiceType getServiceType() {
    	String type = getWebServiceTypeCombo().getText();
		return type.startsWith("Bottom up") ? ServiceType.BOTTOM_UP : ServiceType.TOP_DOWN;
	}

	/**
	 * Sets target service project.
	 *
	 * @param serviceProjectName
	 *            name of the project to be set as target service project
	 */
	public void setServiceProject(String serviceProjectName) {
		setTargetProject("Service project:", serviceProjectName);
	}

	/**
	 * Sets target service EAR project.
	 *
	 * @param serviceEARProjectName
	 * 				name of the project to be set as target service EAR project
	 */
	public void setServiceEARProject(String serviceEARProjectName) {
		setTargetProject("Service EAR project:", serviceEARProjectName);
	}

	/**
	 * Sets level of the service generation.
	 *
	 * @param level level of service generation
	 */
	public void setServiceSlider(SliderLevel level) {
		if (SliderLevel.NO_CLIENT == level) {
			throw new IllegalArgumentException("Unsupported level: " + level);
		}
		setSlider(level, 0);
	}

	/**
	 * Sets level of the client generation.
	 *
	 * @param level level of client generation
	 */
	public void setClientSlider(SliderLevel level) {
		setSlider(level, 1);
	}

	/**
	 * Returns <code>true</code> if the client is enabled, otherwise returns false
	 */
	public boolean isClientEnabled() {
		return isScaleEnabled(1);
	}

	private Combo getWebServiceTypeCombo() {
		return new LabeledCombo("Web service type:");
	}

	@Override
	protected String getSourceComboLabel() {
		String s = null;
		switch (getServiceType()) {
		case BOTTOM_UP:
			s = "Service implementation:";
			break;
		case TOP_DOWN:
			s = "Service definition:";
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + this);
		}
		return s;
	}
}

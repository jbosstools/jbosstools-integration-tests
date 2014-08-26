package org.jboss.tools.ws.reddeer.ui.wizards.wst;

/**
 * Wizard page of {@link WebServiceClientWizard}.
 *
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public class WebServiceClientWizardPage extends WebServiceWizardPageBase {

	/**
	 * Sets the client project.
	 *
	 * @param projectName name of the client project.
	 */
	public void setClientProject(String projectName) {
		setTargetProject("Client project:", projectName);//, "Specify Client Project Settings"
	}

	/**
	 * Sets the client EAR project.
	 *
	 * @param clientEARProjectName name of the client EAR project
	 */
	public void setClientEARProject(String clientEARProjectName) {
		setTargetProject("Client EAR project:", clientEARProjectName);
	}

	/**
	 * Sets level of the client generation.
	 *
	 * @param level level of client generation
	 */
	public void setClientSlider(SliderLevel level) {
		setSlider(level, 0);
	}

	@Override
	public String getSourceComboLabel() {
		return "Service definition:";
	}
}

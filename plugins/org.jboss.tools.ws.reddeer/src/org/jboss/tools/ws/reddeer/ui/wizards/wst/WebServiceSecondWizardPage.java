package org.jboss.tools.ws.reddeer.ui.wizards.wst;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Second {@link WebServiceWizard} page.
 *
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public class WebServiceSecondWizardPage extends WizardPage {

	/**
	 * Sets package name.
	 * 
	 * @param pkgName package name
	 */
	public void setPackageName(String pkgName) {
		getPackageNameText().setText(pkgName);
	}

	/**
	 * Return package name.
	 */
	public String getPackageName() {
		return getPackageNameText().getText();
	}

	private Text getPackageNameText() {
		return new LabeledText("Package name");
	}
}

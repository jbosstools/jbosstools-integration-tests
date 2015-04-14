package org.jboss.tools.ws.reddeer.ui.wizards.wst;

import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.core.matcher.WithLabelMatcher;

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
		return new DefaultText(new WithLabelMatcher(AnyOf.anyOf(Is.is("Package name"), 
				Is.is("Package Name:"))));
	}
}

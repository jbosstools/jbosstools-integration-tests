package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class JPAFacetWizardPage extends WizardPage {
	
	public void setPlatform(JpaPlatform platform) {
		new DefaultCombo(0).setSelection(platform.toString());
	}	

	public void setJpaImplementation(JpaImplementation impl) {
		new DefaultCombo(1).setSelection(impl.toString());
	}
}

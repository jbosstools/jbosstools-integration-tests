package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class JPAProjectWizardFirstPage extends NewJavaProjectWizardPage {
	
	public void setJPAVersion(JpaVersion version) {
		new DefaultCombo(1).setSelection(version.toString());
	}
}

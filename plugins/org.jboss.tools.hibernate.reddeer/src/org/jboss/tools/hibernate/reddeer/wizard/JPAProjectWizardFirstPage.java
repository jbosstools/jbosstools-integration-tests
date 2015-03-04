package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * JPA Project wizard page
 * @author jpeterka
 *
 */
public class JPAProjectWizardFirstPage extends NewJavaProjectWizardPage {
	
	/**
	 * Select JPA version
	 * @param version given JPA version
	 */
	public void setJPAVersion(JpaVersion version) {
		new DefaultCombo(1).setSelection(version.toString());
	}
}

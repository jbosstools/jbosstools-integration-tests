package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;


/**
 * Hibernate XML mapping file preview page
 * User can see preview of generated code
 * @author jpeterka
 *
 */
public class NewHibernateMappingPreviewPage extends WizardPage {
	
	/**
	 * Gets preview page text for hbm xml file wizard
	 * @return preview text
	 */
	public String getPreviewText() {
		String ret = new DefaultText().getText();
		return ret;
	}
}

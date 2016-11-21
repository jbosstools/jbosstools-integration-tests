package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * New Hibernate xml mapping file wizard
 * @author Jiri Peterka
 *
 */
public class NewHibernateMappingFileWizard extends NewWizardDialog {

	/**
	 * Initializes wizard
	 */
	public NewHibernateMappingFileWizard() {
		super("Hibernate", "Hibernate XML Mapping file (hbm.xml)");
	}
}

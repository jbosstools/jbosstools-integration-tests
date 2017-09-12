package org.jboss.tools.forge.reddeer.ui.wizard;

import java.util.List;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Reddeer implementation of forge Entities from tables wizard first page
 * @author jrichter
 *
 */
public class EntitiesFromTablesWizardFirstPage extends WizardPage {
	
	public EntitiesFromTablesWizardFirstPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	/**
	 * Fill in the Target package field with set value
	 * @param pkg package name to be set
	 */
	public void setPackage(String pkg) {
		new LabeledText("Target package:").setText(pkg);
	}
	
	/**
	 * Select a connection profile 
	 * @param profileName name of the profile to be selected
	 */
	public void setConnectionProfile(String profileName) {
		new DefaultCombo(0).setSelection(profileName);
	}
	
	/**
	 * Fill in the Connection Profile Password field
	 * @param password
	 */
	public void setPassword(String password) {
		new LabeledText("Connection Profile Password:").setText(password);
	}
	
	/**
	 * Get all saved profile names
	 * @return List of all profile names
	 */
	public List<String> getAllProfiles() {
		return new DefaultCombo(0).getItems();
	}
}

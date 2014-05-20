package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

public class NewHibernateConfigurationWizard extends NewWizardDialog{

	public NewHibernateConfigurationWizard() {
		super("Hibernate","Hibernate Configuration File (cfg.xml)");
	}

	@Override
	public WizardPage getFirstPage() {
		return getWizardPage();
	}
	
	@Override
	public WizardPage getWizardPage() {
		int index = getPageIndex();
		
		switch (index) {
		case 0: 
			return new NewConfigurationLocationPage();
		case 1: 
			return new NewConfigurationSettingPage();
		default:
			throw new UnsupportedOperationException("page is not implemented");
		}
	}
}

package org.jboss.tools.cdi.reddeer.xhtml;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class NewXHTMLWizard extends NewWizardDialog{
	
	public static final String CATEGORY="JBoss Tools Web";
	public static final String NAME="XHTML Page";
	
	public NewXHTMLWizard(){
		super(CATEGORY,NAME);
		addWizardPage(new NewXHTMLTemplatesWizardPage(), 1);
	}
	
	
	
	@Override
	public NewXHTMLFileWizardPage getFirstPage() {
		return new NewXHTMLFileWizardPage();
	}

}

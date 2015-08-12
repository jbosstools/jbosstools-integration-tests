package org.jboss.tools.cdi.reddeer.xhtml;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;


public class NewXHTMLWizard extends NewWizardDialog{
	
	public static final String CATEGORY="JBoss Tools Web";
	public static final String NAME="XHTML Page";
	
	public NewXHTMLWizard(){
		super(CATEGORY,NAME);
	}

}

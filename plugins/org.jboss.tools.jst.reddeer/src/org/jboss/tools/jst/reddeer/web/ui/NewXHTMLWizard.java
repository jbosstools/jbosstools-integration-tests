package org.jboss.tools.jst.reddeer.web.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

public class NewXHTMLWizard extends NewWizardDialog{
	
	private static final String CATEGORY = "JBoss Tools Web";
	private static final String ITEM = "XHTML Page";
	
	public NewXHTMLWizard(){
		super(CATEGORY,ITEM);
		addWizardPage(new NewXHTMLFileWizardPage(),0);
	}
	
	

}

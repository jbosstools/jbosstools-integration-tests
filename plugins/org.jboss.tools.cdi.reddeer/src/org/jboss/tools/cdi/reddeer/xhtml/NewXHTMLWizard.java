package org.jboss.tools.cdi.reddeer.xhtml;

import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;


public class NewXHTMLWizard extends NewMenuWizard{
	
	public static final String CATEGORY="JBoss Tools Web";
	public static final String NAME="XHTML Page";
	public static final String SHELL_TEXT = "New XHTML Page";
	
	public NewXHTMLWizard(){
		super(SHELL_TEXT,CATEGORY,NAME);
	}

}

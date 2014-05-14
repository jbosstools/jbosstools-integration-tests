package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class NewBeansXMLCreationWizard extends NewWizardDialog{
	
	public static final String CATEGORY="CDI (Context and Dependency Injection)";
	public static final String NAME="File beans.xml";
	
	public NewBeansXMLCreationWizard(){
		super(CATEGORY,NAME);
	}
	
	public void setSourceFolder(String... path){
		new DefaultTreeItem(path).select();
	}

}

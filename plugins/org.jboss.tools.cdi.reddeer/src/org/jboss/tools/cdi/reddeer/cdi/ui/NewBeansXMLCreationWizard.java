package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class NewBeansXMLCreationWizard extends NewWizardDialog{
	
	public static final String NAME="File beans.xml";
	
	public NewBeansXMLCreationWizard(){
		super(CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setSourceFolder(String... path){
		new DefaultTreeItem(path).select();
	}

}

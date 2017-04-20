package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class NewBeansXMLCreationWizard extends NewMenuWizard{
	
	public static final String NAME="beans.xml File";
	public static final String SHELL_TEXT = "New beans.xml File";
	
	public NewBeansXMLCreationWizard(){
		super(SHELL_TEXT, CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setSourceFolder(String... path){
		new DefaultTreeItem(path).select();
	}

}

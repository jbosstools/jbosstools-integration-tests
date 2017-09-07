package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class NewQualifierCreationWizard extends NewMenuWizard{
	
	public static final String NAME="Qualifier Annotation";
	public static final String SHELL_TEXT = "New Qualifier";
	
	public NewQualifierCreationWizard(){
		super(SHELL_TEXT, CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setPackage(String packageName){
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public void setInherited(boolean inherited){
		new CheckBox("Add @Inherited").toggle(inherited);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
}

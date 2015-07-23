package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class NewScopeCreationWizard extends NewWizardDialog{
	
	public static final String NAME="Scope Annotation";
	
	public NewScopeCreationWizard(){
		super(CDIConstants.CDI_GROUP,NAME);
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
	
	public void setNormalScope(boolean scope){
		new CheckBox("is normal scope").toggle(scope);
	}
	
	public void setPassivating(boolean passivating){
		new CheckBox("is passivating").toggle(passivating);
	}

}

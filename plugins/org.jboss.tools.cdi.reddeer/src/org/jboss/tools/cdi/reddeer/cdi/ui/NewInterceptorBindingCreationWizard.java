package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewInterceptorBindingCreationWizard extends NewWizardDialog{
	
	public static final String CATEGORY="CDI (Context and Dependency Injection)";
	public static final String NAME="Interceptor Binding Annotation";
	
	public NewInterceptorBindingCreationWizard(){
		super(CATEGORY,NAME);
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
	
	public void setTarget(String target){
		new LabeledCombo("Target:").setSelection(target);
	}
	
	public void addInterceptorBindings(String bindings){
		new PushButton("Add").click();
		new DefaultShell("Select Interceptor Binding Annotation Type");
		new DefaultText(0).setText(bindings);
		new PushButton("OK").click();
		new DefaultShell("New Interceptor Binding");
	}

}

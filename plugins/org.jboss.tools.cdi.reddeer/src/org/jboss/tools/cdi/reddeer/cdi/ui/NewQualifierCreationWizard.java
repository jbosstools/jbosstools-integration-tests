package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
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
	
	public void finish() {
		String shellText = new DefaultShell().getText();
		Button button = new PushButton("Finish");
		button.click();
		new WaitWhile(new ShellIsActive(shellText), TimePeriod.LONG);
	}

}

package org.jboss.tools.seam.reddeer.preferences;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

public class SeamPreferencePage extends WorkbenchPreferencePage{
	
	public SeamPreferencePage(){
		super("JBoss Tools","Web","Seam");
	}
	
	public void addRuntime(String name, String seamPath, String seamVersion){
		for(int i=0; i< new DefaultTable().rowCount(); i++){
			if(new DefaultTable().getItem(i).getText(3).equals(seamPath)){
				return; //seam runtime already exists
			}
		}
		new PushButton("Add").click();
		new DefaultShell("New Seam Runtime");
		new LabeledText("Home Folder:").setText(seamPath);
		new LabeledText("Name:").setText(name);
		new LabeledCombo("Version:").setSelection(seamVersion);
		new PushButton("Finish").click();
		new WaitUntil(new ShellWithTextIsActive("Preferences"),TimePeriod.NORMAL);
	}
}

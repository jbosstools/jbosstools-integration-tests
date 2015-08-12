package org.jboss.tools.seam.reddeer.preferences;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class SeamPreferencePage extends PreferencePage{
	
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

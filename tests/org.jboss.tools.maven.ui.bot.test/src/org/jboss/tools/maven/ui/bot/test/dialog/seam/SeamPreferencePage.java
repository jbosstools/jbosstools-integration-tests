package org.jboss.tools.maven.ui.bot.test.dialog.seam;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ButtonIsEnabled;

public class SeamPreferencePage extends PreferencePage{
	
	public SeamPreferencePage(){
		super("JBoss Tools","Web","Seam");
	}
	
	public void addRuntime(String name, String seamPath, String seamVersion){
		for(int i=0; i< new DefaultTable().rowCount(); i++){
			if(new DefaultTable().cell(i, 3).equals(seamPath)){
				return; //seam runtime already exists
			}
		}
		new PushButton("Add").click();
		new WaitUntil(new ShellWithTextIsActive("New Seam Runtime"),TimePeriod.NORMAL);
		new LabeledText("Home Folder:").setText(seamPath);
		new LabeledText("Name:").setText(name);
		new DefaultCombo("Version:").setSelection(seamVersion);
		new WaitUntil(new ButtonIsEnabled("Finish"));
		new PushButton("Finish").click();
		new WaitUntil(new ShellWithTextIsActive("Preferences"),TimePeriod.NORMAL);
	}
}

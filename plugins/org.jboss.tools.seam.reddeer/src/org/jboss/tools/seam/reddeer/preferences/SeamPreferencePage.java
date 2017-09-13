package org.jboss.tools.seam.reddeer.preferences;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class SeamPreferencePage extends PreferencePage{
	
	public SeamPreferencePage(ReferencedComposite referencedComposite){
		super(referencedComposite, "JBoss Tools","Web","Seam");
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
		new WaitUntil(new ShellIsAvailable("Preferences"),TimePeriod.DEFAULT);
	}
}

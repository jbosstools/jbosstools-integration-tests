package org.jboss.tools.maven.reddeer.preferences;

import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

public class MavenUserPreferencePage extends WorkbenchPreferencePage{
	
	public MavenUserPreferencePage(){
		super("Maven","User Settings");
	}
	
	public void setUserSettings(String pathToSettings){
		Text text = null;
		for(int i=0;i<10;i++){
			text = new DefaultText(i);
			if(text.getText().contains("settings") && text.getText().contains(".xml")){
				break;
			}
		}
		if(!text.getText().equals(pathToSettings)){
			text.setText(pathToSettings);
			Button button = new PushButton("Update Settings");
			button.click();
			new WaitUntil(new JobIsRunning(),TimePeriod.NORMAL,false);
			new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		}
	}
	
	public String getUserSettings(){
		Text text = null;
		for(int i=0;i<10;i++){
			text = new DefaultText(i);
			if(text.getText().contains("settings") && text.getText().contains(".xml")){
				break;
			}
		}
		return text.getText();
	}
	
	@Override
	public void ok(){
		new PushButton("Apply").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		super.ok();
	}

}

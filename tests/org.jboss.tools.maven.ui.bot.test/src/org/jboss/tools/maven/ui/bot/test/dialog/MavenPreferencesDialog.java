package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class MavenPreferencesDialog extends PreferencePage{
	
	public MavenPreferencesDialog(){
		super("Maven","User Settings");
	}
	
	public void setUserSettings(String pathToSettings){
		Text text = new DefaultText(1);
		text.setText(pathToSettings);
		Button button = new PushButton("Update Settings");
		button.click();
	}

}

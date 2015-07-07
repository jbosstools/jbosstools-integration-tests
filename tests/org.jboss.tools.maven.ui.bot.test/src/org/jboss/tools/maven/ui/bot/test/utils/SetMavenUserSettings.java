package org.jboss.tools.maven.ui.bot.test.utils;

import java.io.File;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.extensionpoint.IBeforeTest;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;

public class SetMavenUserSettings implements IBeforeTest {
	
	private static final Logger log = Logger.getLogger(SetMavenUserSettings.class);
	public static final String USER_SETTINGS = new File("target/classes/settings.xml").getAbsolutePath(); 

	@Override
	public void runBeforeTest() {
		if (hasToRun()) {
			setUserSettings();
		}
		
	}

	@Override
	public boolean hasToRun() {
		return true;
	}
	
	private void setUserSettings(){
		log.debug("Setting maven user settings to "+USER_SETTINGS);
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenUserPreferencePage mpreferences = new MavenUserPreferencePage();
		preferenceDialog.select(mpreferences);
		if(!mpreferences.getUserSettings().equals(USER_SETTINGS)){
			mpreferences.setUserSettings(USER_SETTINGS);
			mpreferences.ok();
		} else {
			preferenceDialog.ok();
		}
	}

}

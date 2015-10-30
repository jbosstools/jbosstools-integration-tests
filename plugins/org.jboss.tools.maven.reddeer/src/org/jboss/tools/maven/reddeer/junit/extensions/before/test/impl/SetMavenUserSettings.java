package org.jboss.tools.maven.reddeer.junit.extensions.before.test.impl;

import java.io.File;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.extensionpoint.IBeforeTest;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * This extensions sets defined Maven settings.xml in Maven -> User Settings property page
 * 
 * available properties:
 * maven.settings.path - path to settings.xml which should be set. If this property is not set this class wont run
 * @author rawagner
 *
 */
public class SetMavenUserSettings implements IBeforeTest {
	
	private static final Logger log = Logger.getLogger(SetMavenUserSettings.class);
	
	@Override
	public void runBeforeTestClass(String config, TestClass testClass) {
		setUserSettings(getMavenSettingsPath());
	}
	@Override
	public void runBeforeTest(String config, Object target, FrameworkMethod method) {
		// do nothing because all logic is in runBeforeTestClass() method	
	}
	@Override
	public boolean hasToRun() {
		return System.getProperty("maven.settings.path") != null;
	}
	
	private String getMavenSettingsPath(){
		File userSettings = new File(System.getProperty("maven.settings.path"));
		if(!userSettings.exists()){
			log.warn("Maven settings.xml does not exist");
		}
		return userSettings.getAbsolutePath();
	}
	
	private void setUserSettings(String mavenSettingsPath){
		log.debug("Setting maven user settings to "+mavenSettingsPath);
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenUserPreferencePage mpreferences = new MavenUserPreferencePage();
		preferenceDialog.select(mpreferences);
		if(!mpreferences.getUserSettings().equals(mavenSettingsPath)){
			mpreferences.setUserSettings(mavenSettingsPath);
			mpreferences.apply();
		} 
		preferenceDialog.ok();
	}

}

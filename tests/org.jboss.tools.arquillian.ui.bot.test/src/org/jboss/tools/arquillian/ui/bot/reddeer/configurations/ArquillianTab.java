package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.reddeer.profiles.SelectProfilesDialog;

/**
 * Arquillian tab in JUnit configuration
 * 
 * @author Lucia Jelinkova
 *
 */
public class ArquillianTab extends RunConfigurationTab {

	private static final String MAVEN_DIALOG_TITLE = "Select Maven profiles";
	
	public ArquillianTab() {
		super("Arquillian");
	}

	public void selectMavenProfile(String profile){
		activate();
		new PushButton("Select Maven Profiles").click();
		new WaitUntil(new ShellWithTextIsActive(MAVEN_DIALOG_TITLE));
		
		SelectProfilesDialog dialog = new SelectProfilesDialog(null);
		dialog.activateProfile(profile);
		new PushButton("OK").click();
		
		new WaitWhile(new ShellWithTextIsActive(MAVEN_DIALOG_TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}

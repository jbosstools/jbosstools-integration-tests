package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
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
		new DefaultShell(MAVEN_DIALOG_TITLE);
		
		SelectProfilesDialog dialog = new SelectProfilesDialog(null);
		dialog.activateProfile(profile);
		new PushButton("OK").click();
		
		new WaitWhile(new ShellWithTextIsActive(MAVEN_DIALOG_TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}

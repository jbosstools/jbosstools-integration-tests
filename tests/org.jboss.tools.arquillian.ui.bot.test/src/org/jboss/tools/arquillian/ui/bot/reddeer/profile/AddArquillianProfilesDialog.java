package org.jboss.tools.arquillian.ui.bot.reddeer.profile;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Dialog for adding Arquillian profiles.
 * 
 * @author Lucia Jelinkova
 *
 */
public class AddArquillianProfilesDialog {
	
	private static final String NAME = "Add Arquillian Profiles";
	
	public void open(Project project){
		project.select();
		new ContextMenu("Configure","Add Arquillian Profiles...").select();
	}
	
	public void ok(){
		activate();
		String shellText = new DefaultShell().getText();
		Button button = new PushButton("OK");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void selectProfile(String profile){
		activate();
		new DefaultTableItem(new WithTextMatcher(profile)).setChecked(true);
	}
	
	private void activate(){
		// sets focus
		new DefaultShell(NAME);
	}
}

package org.jboss.tools.arquillian.ui.bot.reddeer.support;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Dialog shown after user selects on Maven project Configure -> Add Arquillian Support 
 * 
 * @author Lucia Jelinkova
 *
 */
public class AddArquillianSupportDialog {
	
	private static final String NAME = "Add Arquillian support";
	
	public void open(Project project){
		project.select();
		new ContextMenu("Configure","Add Arquillian Support...").select();
	}

	public void ok(){
		activate();
		String shellText = new DefaultShell().getText();
		Button button = new PushButton("OK");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void activate(){
		// sets focus
		new DefaultShell(NAME);
	}
}

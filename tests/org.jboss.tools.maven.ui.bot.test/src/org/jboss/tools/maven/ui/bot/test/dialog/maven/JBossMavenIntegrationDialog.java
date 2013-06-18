package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class JBossMavenIntegrationDialog extends PreferencePage{
	
	public JBossMavenIntegrationDialog(){
		super("JBoss Tools","JBoss Maven Integration");
	}
	
	public MavenRepositoriesDialog modifyRepositories(){
		MavenRepositoriesDialog mr = new MavenRepositoriesDialog();
		mr.open();
		return mr;
	}
	
	@Override
	public void ok(){
		new PushButton("Apply").click();
		try{
			new WaitUntil(new JobIsRunning(),TimePeriod.NORMAL);
		} catch(TimeoutException ex){
			
		}
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		super.ok();
	}

}

package org.jboss.tools.usercase.ticketmonster.ui.bot.test.forge;

import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public class ForgeConsole extends WorkbenchView{
	
	private StyledText styledText;
	
	public ForgeConsole(){
		super("Forge","Forge Console");
	}
	
	public void initStyledText(){
		styledText = new DefaultStyledText();
	}
	
	public void typeCommand(String command){
		styledText.setText(command+"\n");
		new WaitUntil(new JobIsRunning());
	}
	
	public String getText(){
		return styledText.getText();
	}
	
	

}

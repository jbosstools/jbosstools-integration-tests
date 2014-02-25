package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;


import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;

public class JBossRuntimeDetectionPreferencePage extends PreferencePage{
	
	public JBossRuntimeDetectionPreferencePage(){
		super("JBoss Tools","JBoss Runtime Detection");
	}
	
	public String getName(){
		return new DefaultCLabel().getText();
	}
}

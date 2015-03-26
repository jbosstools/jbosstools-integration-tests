package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Test tab in JUnit configuration
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestTab extends RunConfigurationTab {

	public JUnitTestTab() {
		super("Test");
	}
	
	public void setProject(String text){
		activate();
		new LabeledText("Project:").setText(text);
	}
	
	public void setTestClass(String text){
		activate();
		new LabeledText("Test class:").setText(text);
	}
}

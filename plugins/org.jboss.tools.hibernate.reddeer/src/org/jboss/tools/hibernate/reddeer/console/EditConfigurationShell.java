package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class EditConfigurationShell extends DefaultShell {

	public EditConfigurationShell() {
		super("Edit Configuration");
	}
	
	public EditConfigurationMainPage getMainPage() {
		new DefaultCTabItem("Main").activate();
		return new EditConfigurationMainPage();
	}
	
	public EditConfigurationOptionsPage getOptionsPage() {
		new DefaultTabItem("Options").activate();
		return new EditConfigurationOptionsPage();
	}
	
	public EditorConfigurationClassPathPage getClassPathPage() {
		new DefaultTabItem("ClassPath").activate();
		return new EditorConfigurationClassPathPage();
	}
	
	public EditConfigurationMappingsPage getCommonPage() {
		new DefaultTabItem("Mappings").activate();
		return new EditConfigurationMappingsPage();
	}

	public void ok() {
		String title = getText();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
		new WaitWhile(new JobIsRunning());		
	}

	public void setName(String name) {
		new LabeledText("Name:").setText(name);		
	}
	
}

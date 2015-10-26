package org.jboss.tools.openshift.reddeer.wizard.v3;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;

/**
 * Creator allows to create a new OpenShift 3 application in simplified way. It's required to provide mostly
 * only server, username and project together with template name or location on a file system.
 * 
 * @author mlabuda@redhat.com
 */
public class TemplatesCreator {

	private NewOpenShift3ApplicationWizard wizard;
	
	/**
	 * Initiates a new template creator to create a new OpenShift 3 application based on template.
	 * Wizard is opened from OpenShift explorer if openFromShellMenu is false. Otherwise wizard is 
	 * opened via shell menu.
	 * 
	 * @param server OpenShift 3 server
	 * @param username user name
	 * @param project project
	 * @param openFromShellMenu open wizard from shell menu, if false, opens it from OpenShift explorer
	 */
	public TemplatesCreator(String server, String username, String project, boolean openFromShellMenu) {
		wizard = new NewOpenShift3ApplicationWizard(server, username, project);
		
		if (openFromShellMenu) {
			wizard.openWizardFromShellMenu();
		} else {
			wizard.openWizardFromExplorer();
		}
	}

	/**
	 * Initiates a new template creator to create a new OpenShift 3 application based on template.
	 * Wizard is opened from OpenShift explorer view.
	 * 
	 * @param server OpenShift 3 server
	 * @param username user name
	 * @param project project
	 */
	public TemplatesCreator(String server, String username, String project) {
		this(server, username, project, false);
	}
	
	/**
	 * Creates new OpenShift 3 application from a server template with matching name.
	 * 
	 * @param templateName name of a server template
	 */
	public void createOpenShiftApplicationBasedOnServerTemplate(String templateName) {
		new DefaultTree().selectItems(new DefaultTreeItem(templateName));
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL);
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		new WaitWhile(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SUMMARY), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SUMMARY);
		
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION));
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		new FinishButton().click();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CHEATSHEET), TimePeriod.LONG);
			
			new DefaultShell(OpenShiftLabel.Shell.CHEATSHEET);
			new CheckBox(0).click();
			new NoButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CHEATSHEET));
		} catch (WaitTimeoutExpiredException ex) {
			// do nothing if cheat sheet is not provided
		}
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}

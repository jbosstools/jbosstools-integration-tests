package org.jboss.tools.openshift.reddeer.wizard.v3;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.NewOpenShiftApplicationWizard;

/**
 * To create a new OpenShift 3 application there are several steps:
 * - open new OpenShift 3 application wizard
 * - select template either from local file system or server
 * - proceed through wizard and set up missing remaining parameters if there are any
 * 
 * @author mlabuda@redhat.com
 */
public class NewOpenShift3ApplicationWizard extends NewOpenShiftApplicationWizard {

	private String project;
	
	public NewOpenShift3ApplicationWizard(String server, String username, String project) {
		super(server, username);
		this.project = project;
	}
	
	/**
	 * Opens a new OpenShift 3 application wizard from OpenShift Explorer view.
	 */
	public void openWizardFromExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		explorer.getOpenShift3Connection(username, server).getProject(project).select();
	
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS3_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD));
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
	}
}

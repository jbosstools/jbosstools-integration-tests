package org.jboss.tools.openshift.ui.wizard.application;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.internal.FirstWizardPage;
import org.jboss.tools.openshift.ui.wizard.application.internal.FourthWizardPage;
import org.jboss.tools.openshift.ui.wizard.application.internal.SecondWizardPage;
import org.jboss.tools.openshift.ui.wizard.application.internal.ThirdWizardPage;

/**
 * Creating application consist of 3 required steps:
 * - opening new application wizard. See {@link OpenNewApplicationWizard}
 * - proceed through wizard and set up details
 * - post create steps (accept ssh host key, embedded cartridges dialog...)
 * 
 * @author mlabuda@redhat.com
 *
 */
public class NewApplicationWizard {	
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	public NewApplicationWizard() {
	}
	
	public void importExistingApplication(String domain, String existingAppName,
			boolean createAdapter, String gitDestination) {
		FirstWizardPage firstPage = new FirstWizardPage();
		firstPage.importExistingApplication(domain, existingAppName);

		next();
		
		ThirdWizardPage thirdPage = new ThirdWizardPage();
		thirdPage.configureProjectAndServerAdapter(true, null, false);
		
		next();
		
		FourthWizardPage fourthPage = new FourthWizardPage();
		fourthPage.setGitDestination(gitDestination);
		
		finish();
	}
	
	public void createNewApplicationOnBasicCartridge(String cartridge, String domain, String appName, 
			boolean scalable, boolean smallGear, boolean createEnvironmentVariable, boolean disableMvnBuild,
			String sourceCodeURL, String embeddedCartridgeURL, boolean createAdapter,
			String deployProject, String gitDestination, String gitRemoteName, 
			String... embeddedCartridges) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationOnBasicCartridge(cartridge);
		
		next();
		
		proceedThroughWizard(domain, appName, scalable, smallGear, createEnvironmentVariable, 
				sourceCodeURL, embeddedCartridgeURL, createAdapter, deployProject, disableMvnBuild, 
				gitDestination, gitRemoteName, embeddedCartridges);
	}
	
	public void createQuickstart(String quickstart, String domain, String appName,
			boolean scalable, boolean smallGear, boolean createAdapter, 
			String gitDestination) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createQuickstart(quickstart);
		
		next();
		
		proceedThroughWizard(domain, appName, scalable, smallGear, false, 
				null, null, createAdapter, null, false, 
				gitDestination, null, (String[]) null);
	}
	
	public void createNewApplicationOnDownloadableCartridge(String URL, String domain, String appName, 
			boolean scalable, boolean smallGear, boolean createEnvironmentVariable, boolean disableMvnBuild,
			String sourceCodeURL, String embeddedCartridgeURL, boolean createAdapter,
			String deployProject, String gitDestination, String gitRemoteName, 
			String... embeddedCartridges) {

		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationFromDownloadableCartridge(URL);
	
		next();

		proceedThroughWizard(domain, appName, scalable, smallGear, createEnvironmentVariable, 
				sourceCodeURL, embeddedCartridgeURL, createAdapter, deployProject, disableMvnBuild, 
				gitDestination, gitRemoteName, embeddedCartridges);
	}
	
	
	private void proceedThroughWizard(String domain, String appName, boolean scalable, boolean smallGear, 
			boolean createEnvironmentVariable, String sourceCodeURL, String embeddedCartridgeURL, 
			boolean createAdapter, String deployProject, boolean disableMvnBuild, String gitDestination, 
			String gitRemoteName, String... embeddedCartridges) {
		
		SecondWizardPage secondPage = new SecondWizardPage();
		secondPage.fillApplicationDetails(domain, appName, scalable, smallGear, createEnvironmentVariable, 
				sourceCodeURL);
		secondPage.addCartridges(embeddedCartridges);
		secondPage.addCodeAnythingCartridge(embeddedCartridgeURL);
		
		next();
		
		ThirdWizardPage thirdPage = new ThirdWizardPage();
		thirdPage.configureProjectAndServerAdapter(createAdapter, deployProject, disableMvnBuild);
		
		next();
		
		FourthWizardPage fourthPage = new FourthWizardPage();
		fourthPage.setGitDestination(gitDestination);
		fourthPage.setGitRemoteName(gitRemoteName);
		
		finish();
	}
	
	/**
	 * @param cartridgeIsEmbedded true if embeddable cartridges is added, false otherwise
	 * @param acceptHostKey set true for accepting SSH host key, false otherwise
	 */
	public void postCreateSteps(boolean cartridgeIsEmbedded) {
		if (cartridgeIsEmbedded) {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), 
					TimePeriod.VERY_LONG);
		
			new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
			new OkButton().click();
		}
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
			
	/**
	 * Verify that application exists on OpenShift and that there is a server 
	 * adapter for application.
	 * 
	 * @param username user name
	 * @param domain domain name
	 * @param appName application name
	 * @param project project deployed on OpenShift shown in ServersView
	 */
	public void verifyApplication(String username, String domain, String appName, String project) {
		// Verify in OpenShift explorer
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		assertTrue("Application " + appName + 
				" has not been created", explorer.applicationExists(username, domain, appName));
		
		// Verify in servers view
		ServersView serversView = new ServersView();
		serversView.open();
		
		try {
			treeViewerHandler.getTreeItem(new DefaultTree(), appName + " at OpenShift");
			// pass
		} catch (JFaceLayerException ex) {
			fail("There is no server adapter for application " + appName + " and project " + project);
		}
	}	
	
	//////////////////////////////////////
	////////// WIZARD NAVIGATE ///////////
    //////////////////////////////////////
	
	/**
	 * Waits and clicks Back button.
	 */
	public void back() {
		new WaitUntil(new ButtonWithTextIsActive(new BackButton()), TimePeriod.LONG);
		
		new BackButton().click();
	}
	
	/**
	 * Waits and clicks Next button.
	 */
	public void next() {
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
	}
	

	/**
	 * Waits and clicks Cancel button .
	 */
	public void cancel() {
		new WaitUntil(new ButtonWithTextIsActive(new CancelButton()), TimePeriod.LONG);
		
		new CancelButton().click();
	}
	
	/**
	 * Waits and clicks Finish button.
	 */
	public void finish() {
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
	}
}

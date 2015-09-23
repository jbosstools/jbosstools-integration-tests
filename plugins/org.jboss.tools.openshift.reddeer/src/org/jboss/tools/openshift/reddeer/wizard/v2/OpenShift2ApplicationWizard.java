package org.jboss.tools.openshift.reddeer.wizard.v2;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.FirstWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.FourthWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.SecondWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.ThirdWizardPage;

/**
 * Creating application consist of 3 required steps:
 * - opening new application wizard. See {@link OpenNewApplicationWizard}
 * - proceed through wizard and set up details
 * - post create steps (accept ssh host key, embedded cartridges dialog...)
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenShift2ApplicationWizard {	
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	private String username;
	private String server;
	private String domain;
	
	public OpenShift2ApplicationWizard(String username, String server, String domain) {
		this.username = username;
		this.server = server;
		this.domain = domain;
	}
	
	/**
	 * Opens new application wizard from OpenShift explorer.
	 */
	public void openWizardFromExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		// workaround
		explorer.reopen();

		explorer.getOpenShift2Connection(username, server).getDomain(domain).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);

		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	/**
	 * Opens new application wizard via shell menu. There has to be 
	 * existing connection in OpenShift explorer, otherwise method fails.
	 */
	public void openWizardFromShellMenu() {
		new WorkbenchShell().setFocus();
		
		new ShellMenu("File", "New", "Other...").select();
		
		new DefaultShell("New").setFocus();
		
		new DefaultTreeItem("OpenShift", "OpenShift Application").select();
		
		new NextButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(username) && comboItem.contains(server)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	public void openWizardFromCentral() {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		new InternalBrowser().execute(OpenShiftLabel.Others.OPENSHIFT_CENTRAL_SCRIPT);
	
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(username)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	public void importExistingApplication(String existingAppName,
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
	
	public void createNewApplicationOnBasicCartridge(String cartridge, String appName, 
			boolean scalable, boolean smallGear, boolean createEnvironmentVariable, boolean disableMvnBuild,
			String sourceCodeURL, String embeddedCartridgeURL, boolean createAdapter,
			String deployProject, String gitDestination, String gitRemoteName, 
			String... embeddedCartridges) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationOnBasicCartridge(cartridge);
		
		next();
		
		proceedThroughWizard(appName, scalable, smallGear, createEnvironmentVariable, 
				sourceCodeURL, embeddedCartridgeURL, createAdapter, deployProject, disableMvnBuild, 
				gitDestination, gitRemoteName, embeddedCartridges);
	}
	
	public void createQuickstart(String quickstart, String appName,
			boolean scalable, boolean smallGear, boolean createAdapter, 
			String gitDestination) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createQuickstart(quickstart);
		
		next();
		
		proceedThroughWizard(appName, scalable, smallGear, false, 
				null, null, createAdapter, null, false, 
				gitDestination, null, (String[]) null);
	}
	
	public void createNewApplicationOnDownloadableCartridge(String URL, String appName, 
			boolean scalable, boolean smallGear, boolean createEnvironmentVariable, boolean disableMvnBuild,
			String sourceCodeURL, String embeddedCartridgeURL, boolean createAdapter,
			String deployProject, String gitDestination, String gitRemoteName, 
			String... embeddedCartridges) {

		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationFromDownloadableCartridge(URL);
	
		next();

		proceedThroughWizard(appName, scalable, smallGear, createEnvironmentVariable, 
				sourceCodeURL, embeddedCartridgeURL, createAdapter, deployProject, disableMvnBuild, 
				gitDestination, gitRemoteName, embeddedCartridges);
	}
	
	
	private void proceedThroughWizard(String appName, boolean scalable, boolean smallGear, 
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
	 * @param appName application name
	 * @param project project deployed on OpenShift shown in ServersView
	 */
	public void verifyApplication(String appName, String project) {
		try {
			new WaitUntil(new OpenShiftApplicationExists(username, server, domain, appName), TimePeriod.NORMAL);
		} catch (WaitTimeoutExpiredException ex) {
			fail("OpenShift 2 application has not been created successfully, or it is not at least shown"
					+ " in OpenShift explorer view under specific domain.");
		}		
	}	
	
	public void verifyServerAdapter(String appName, String project) {
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
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new BackButton().click();
	}
	
	/**
	 * Waits and clicks Next button.
	 */
	public void next() {
		new WaitUntil(new ButtonWithTextIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
	}
	

	/**
	 * Waits and clicks Cancel button .
	 */
	public void cancel() {
		new WaitUntil(new ButtonWithTextIsEnabled(new CancelButton()), TimePeriod.LONG);
		
		new CancelButton().click();
	}
	
	/**
	 * Waits and clicks Finish button.
	 */
	public void finish() {
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
	}
}

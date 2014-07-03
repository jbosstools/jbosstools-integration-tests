package org.jboss.tools.openshift.ui.bot.test.application.wizard;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.page.FirstWizardPage;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.page.FourthWizardPage;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.page.SecondWizardPage;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.page.ThirdWizardPage;
import org.jboss.tools.openshift.ui.bot.test.condition.ConsoleHasText;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * Creating application consist of 3 required steps and 1 optional:
 * - call openWizardFrom* method
 * - call createNewApplication* method
 * - call postCreateSteps* method
 * - call verify* method
 * 
 * @author mlabuda@redhat.com
 *
 */
public class NewApplicationWizard {	

	// First occurrence of the Identify Yourself shell
	private static boolean IDENTIFY_SHOWN = false;
	
	public NewApplicationWizard() {
	}
	
	public void importExistingApplication(String domain, String existingAppName,
			String appName, boolean scalable, boolean smallGear, boolean createEnvironmentVariable,
			String sourceCodeURL, String embeddedURL, boolean createAdapter,
			String deployProject, String gitDest, String gitRemoteName, 
			String... embeddedCartridges) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.importExistingApplication(domain, existingAppName);
		
		next();
		
		processWizard(appName, scalable, smallGear, createEnvironmentVariable,
				sourceCodeURL, embeddedURL, createAdapter, deployProject, gitDest,
				gitRemoteName, embeddedCartridges);
	}
	
	public void createNewApplicationDefaultCartridge(String cartridge, String appName, 
			boolean scalable, boolean smallGear, boolean createEnvironmentVariable,
			String sourceCodeURL, String embeddedURL, boolean createAdapter,
			String deployProject, String gitDest, String gitRemoteName, 
			String... embeddedCartridges) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationOnBasicCartridge(cartridge);
		
		next();
		
		processWizard(appName, scalable, smallGear, createEnvironmentVariable,
				sourceCodeURL, embeddedURL, createAdapter, deployProject, gitDest,
				gitRemoteName, embeddedCartridges);
	}
	
	public void createNewApplicationQuickstart(String quickstart, String appName,
			boolean scalable, boolean smallGear, boolean createAdapter, 
			String gitDest, String gitRemoteName) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationFromQuickstart(quickstart);
		
		next();
		
		processWizard(appName, scalable, smallGear, false,
				null, null, createAdapter, null, gitDest,
				gitRemoteName, (String[]) null);
	}
	
	public void createNewApplicationDownloadableCartridge(String URL, String appName, 
			boolean scalable, boolean smallGear, boolean createEnvironmentVariable,
			String sourceCodeURL, String embeddedURL, boolean createAdapter,
			String deployProject, String gitDest, String gitRemoteName, 
			String... embeddedCartridges) {
		
		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationFromDownloadableCartridge(URL);

		next();
		
		processWizard(appName, scalable, smallGear, createEnvironmentVariable,
				sourceCodeURL, embeddedURL, createAdapter, deployProject, gitDest,
				gitRemoteName, embeddedCartridges);
	}
		
	protected void processWizard(String appName, boolean scalable, boolean smallGear, boolean createEnvironmentVariable,
			String sourceCodeURL, String embeddedURL, boolean createAdapter,
			String deployProject, String gitDest, String gitRemoteName, 
			String... embeddedCartridges) {
		
		SecondWizardPage second = new SecondWizardPage();
		second.fillApplicationDetails(appName, scalable, smallGear, 
				createEnvironmentVariable, sourceCodeURL);
		second.addCodeAnythingCartridge(embeddedURL);
		second.addCartridges(embeddedCartridges);
		
		next();
		
		ThirdWizardPage third = new ThirdWizardPage();
		third.configureProjectAndServerAdapter(createAdapter, deployProject);
		
		next();
		
		FourthWizardPage fourth = new FourthWizardPage();
		fourth.setGitDestination(gitDest);
		fourth.setGitRemoteName(gitRemoteName);
		
		finish();
	}
	
	public void postCreateSteps(String localAppName, boolean isEmbeddedCart, 
			boolean isCreatedAdapter) {
		postCreateSteps(localAppName, isEmbeddedCart, isCreatedAdapter, false, false);
	}
	
	/**
	 * 
	 * @param localAppName project name if local app is deployed, otherwise remote app name
	 * @param isEmbeddedCart true if embeddable cartridges was added or app is DIY/Jenkins, false otherwise
	 * @param isCreatedAdapter true if adapter is gonna be created, false otherwise
	 * @param pushForced true if there will be force dialog (e.g. deploy existing project), false otherwise
	 * @param enabledOpenShiftOnApp if local app is deployed an OpenShift should be enabled on it (add mvn profile)
	 */
	public void postCreateSteps(String localAppName, boolean isEmbeddedCart, 
			boolean isCreatedAdapter, boolean pushForced, boolean enabledOpenShiftOnApp) {
		if (isEmbeddedCart) {
			new WaitUntil(new ShellWithTextIsAvailable("Embedded Cartridges"), TimePeriod.VERY_LONG);
			
			new DefaultShell("Embedded Cartridges");
			new PushButton(OpenShiftLabel.Button.OK).click();
		}
		
		if (enabledOpenShiftOnApp) {
			new WaitUntil(new ShellWithTextIsAvailable("Import OpenShift Application "), 
					TimePeriod.VERY_LONG);
			
			new DefaultShell("Import OpenShift Application ").setFocus();
			
			new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.OK)),
					TimePeriod.NORMAL);
			new PushButton(OpenShiftLabel.Button.OK).click();			
		}
		
		new WaitUntil(new ShellWithTextIsAvailable("Question"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Question").setFocus();
		
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		if (isCreatedAdapter) {
			try {
				if (!IDENTIFY_SHOWN) {
					new WaitUntil(new ShellWithTextIsAvailable("Identify Yourself"), 
							TimePeriod.VERY_LONG);
					
					new DefaultShell("Identify Yourself");
					new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.OK)),
							TimePeriod.NORMAL);
					new PushButton(OpenShiftLabel.Button.OK).click();
					
					IDENTIFY_SHOWN = true;
				}
			} catch(RedDeerException ex) {
			}
			
			new WaitUntil(new ShellWithTextIsAvailable("Commit Changes"), TimePeriod.VERY_LONG);
			
			new DefaultShell("Commit Changes");
			new DefaultStyledText(0).setText("git ignore merged");
			
			Iterator<TreeItem> iterator = new DefaultTree(0).getItems().iterator();
			while (iterator.hasNext()) {
				TreeItem item = iterator.next();
				if (!item.isChecked()) {
					item.setChecked(true);
				}
			}
			
			new WaitUntil(new ButtonWithTextIsActive(new PushButton("Commit and Publish")), TimePeriod.NORMAL);
			
			new PushButton("Commit and Publish").click();
		}

		if (pushForced) {
			new WaitUntil(new ShellWithTextIsAvailable("Attempt push force ?"), TimePeriod.VERY_LONG);
			
			new DefaultShell("Attempt push force ?").setFocus();
			
			new PushButton(OpenShiftLabel.Button.YES).click();
			new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(500));
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
			
	public void verifyApplication(String appName) {
		// Verify in OpenShift explorer
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		assertTrue("Application " + appName + 
				" has not been created", explorer.applicationExists(appName));
		
		// Verify in console
		try {
			new WaitUntil(new ConsoleHasText(), TimePeriod.getCustom(30));
			// pass
		} catch (WaitTimeoutExpiredException ex) {
			fail();
		}
		
		// Verify in servers view
		ServersView serverView = new ServersView();
		serverView.open();
		assertTrue("Adapter does not exists", 
				serverView.getServer(appName + " at OpenShift") != null);
	}
	
	//////////////////////////////////////
	////////// WIZARD NAVIGATE ///////////
    //////////////////////////////////////
	
	/**
	 * Click Next button.
	 */
	public void next() {
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.NEXT)), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
	}
	

	/**
	 * Click Cancel button 
	 */
	public void cancel() {
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.CANCEL)), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.CANCEL).click();
	}
	
	/**
	 * Click Finish button
	 */
	public void finish() {
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.FINISH)), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.FINISH).click();
	}
}

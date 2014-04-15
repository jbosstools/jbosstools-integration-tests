package org.jboss.tools.openshift.ui.bot.test.application.wizard;

import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * Template are easy to use. It is not required to use 3 required steps to create an application
 * and there are less parameters. It is enough to decide where to open wizard from and 
 * what kind of application should be created. Verification step is included as well.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class NewApplicationTemplates {
	
	/**
	 * Initiate new application creation by opening new application wizard from the 
	 * given location - shell menu or explorer
	 * 
	 * @param openFromShellMenu true if wizard should be opened from shell menu File, New
	 * false otherwise (opened from OpenShift explorer)
	 */
	public NewApplicationTemplates(boolean openFromShellMenu) {
		if (openFromShellMenu) {
			OpenNewApplicationWizard.openWizardFromShellMenu();
		} else {
			OpenNewApplicationWizard.openWizardFromExplorer();
		}
	}
	
	/**
	 * Simple application is based on any default (available) cartridge.
	 */
	public void createSimpleApplicationWithoutCartridges(String baseCartridge, 
			String appName, boolean scalable, boolean smallGears, boolean createAdapter) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationDefaultCartridge(baseCartridge, appName, scalable, 
				smallGears, false, null, null, createAdapter, null, null, null, (String[]) null);
		
		boolean isEmbeddedDialog = baseCartridge.equals(OpenShiftLabel.AppType.DIY) ||
				baseCartridge.equals(OpenShiftLabel.AppType.JENKINS);
		wizard.postCreateSteps(appName, isEmbeddedDialog, createAdapter);
		
		if (createAdapter) {
			wizard.verifyApplication(appName);
		}
	}
	
	/**
	 * Create an application from a github template (provided by URL).
	 */
	public void createApplicationFromGithubTemplate(String baseCartridge,
			String appName, String URL, String remoteName, String... cartridges) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationDefaultCartridge(baseCartridge, appName, false, true,
				false, URL, null, true, null, null, remoteName, cartridges);
		
		boolean isEmbeddedDialog = baseCartridge.equals(OpenShiftLabel.AppType.DIY) ||
				baseCartridge.equals(OpenShiftLabel.AppType.JENKINS) ||
				cartridges != null;
		wizard.postCreateSteps(appName, isEmbeddedDialog, true);
		
		wizard.verifyApplication(appName);
	}
	
	/**
	 * Create an application on a downloadable cartridge. Cartridge URL must be valid
	 * manifest.yml URL of a downloadable cartridge. BEWARE - it is required to find out
	 * whether downloadable cartridge show embedded cartridge post creation dialog (e.g. 
	 * as in DIY or Jenkins apps)
	 */
	public void createApplicationOnDownloadableCartridge(String cartridgeURL,
			String appName, boolean scalable, boolean isEmbeddedDialogShown,
			String... cartridges) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationDownloadableCartridge(cartridgeURL, appName, scalable, true, 
				false, null, null, true, null, null, null, cartridges);
	
		wizard.postCreateSteps(appName, isEmbeddedDialogShown, true);
	
		wizard.verifyApplication(appName);
	}
	
	/**
	 * Deploy existing project, if project is git hub project, you can specify remote name
	 * otherwise pass null argument
	 */
	public void deployExistingProject(String cartridge, String appName, String project,
			String gitRemote) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationDefaultCartridge(cartridge, appName, false, true, false, 
				null, null, true, project, null, gitRemote, (String[]) null);
		
		boolean isEmbeddedDialog = cartridge.equals(OpenShiftLabel.AppType.DIY) ||
				cartridge.equals(OpenShiftLabel.AppType.JENKINS);
		
		wizard.postCreateSteps(project, isEmbeddedDialog, true, true, true);
		
		wizard.verifyApplication(appName);
	}
}

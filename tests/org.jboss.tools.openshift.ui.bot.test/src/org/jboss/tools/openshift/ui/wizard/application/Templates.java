package org.jboss.tools.openshift.ui.wizard.application;

import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;

/**
 * Template are easy to use. It is not required to use 3 required steps to create an application
 * and there are less parameters. It is enough to decide how to open new application wizard and 
 * what kind of an application should be created. Verification step is included as well.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class Templates {
	
	private String username;
	private String domain;
	
	/**
	 * Initiates new application creation by opening new application wizard from the 
	 * given location - shell menu or explorer
	 * 
	 * @param openFromShellMenu true if wizard should be opened from shell menu File, New
	 * false otherwise (opened from OpenShift explorer)
	 */
	public Templates(String username, String domain, boolean openFromShellMenu) {
		this.username = username;
		this.domain = domain;
		
		if (openFromShellMenu) {
			OpenNewApplicationWizard.openWizardFromShellMenu(username);
		} else {
			OpenNewApplicationWizard.openWizardFromExplorer(username, domain);
		}
	}
	
	/**
	 * Simple application is based on any basic cartridge.
	 */
	public void createSimpleApplicationOnBasicCartridges(String basicCartridge,  
			String appName, boolean scalable, boolean smallGear, boolean createAdapter) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		
		wizard.createNewApplicationOnBasicCartridge(basicCartridge, domain, appName, scalable, smallGear,
				false, false, null, null, createAdapter, null, null, null, (String[]) null);
		
		boolean isEmbeddedCartridgeDialogShown = basicCartridge.equals(OpenShiftLabel.Cartridge.DIY) ||
				basicCartridge.equals(OpenShiftLabel.Cartridge.JENKINS);
		
		wizard.postCreateSteps(isEmbeddedCartridgeDialogShown);
		
		if (createAdapter) {
			wizard.verifyApplication(username, domain, appName, appName);
		}
	}
	
	/**
	 * Creates quickstart.
	 */
	public void createQuickstart(String quickstart, String appName, boolean scalable, 
			boolean smallGear, boolean createAdapter) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		
		wizard.createQuickstart(quickstart, domain, appName, scalable, smallGear, createAdapter, null);
		wizard.postCreateSteps(scalable);
		
		if (createAdapter) {
			wizard.verifyApplication(username, domain, appName, appName);
		}
	}
	
	/**
	 * Creates an application on a downloadable cartridge.
	 */
	public void createApplicationOnDownloadableCartridge(String cartridgeURL,
			String appName, boolean scalable, boolean isEmbeddedDialogShown, boolean createAdapter,
			String deployProject, String... embeddedCartridges) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		
		wizard.createNewApplicationOnDownloadableCartridge(cartridgeURL, domain, appName, scalable, true,
				false, false, null, null, createAdapter, deployProject, null, null, (String[]) embeddedCartridges);
		wizard.postCreateSteps(isEmbeddedDialogShown);
	
		String project = (deployProject == null) ? appName : deployProject;
		wizard.verifyApplication(username, domain, appName, project);
	}
	
	/**
	 * Deploys existing project on basic cartridge. If project is git-based project, you can specify remote name
	 * otherwise pass null argument.
	 */
	public void deployExistingProject(String cartridge, String appName, String project,
			String gitRemote) {
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		
		wizard.createNewApplicationOnBasicCartridge(cartridge, domain, appName, false, true, false,
				false, null, null, true, project, null, gitRemote, (String[]) null);
		
		boolean isEmbeddedCartridgeDialogShown = cartridge.equals(OpenShiftLabel.Cartridge.DIY) ||
				cartridge.equals(OpenShiftLabel.Cartridge.JENKINS);
		
		wizard.postCreateSteps(isEmbeddedCartridgeDialogShown);
		
		wizard.verifyApplication(username, domain, appName, project);
	}	
}

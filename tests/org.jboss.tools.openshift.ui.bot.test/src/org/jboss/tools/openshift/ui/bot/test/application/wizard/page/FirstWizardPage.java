package org.jboss.tools.openshift.ui.bot.test.application.wizard.page;

import java.util.List;

import org.jboss.reddeer.eclipse.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * 
 * First wizard page of a New application wizard. There are possible to either
 * create a new applicatiom from existing one or create a new application on 
 * some cartridge (basic, downloadable). Also there is possibility to create a 
 * quickstart
 *  
 * @author mlabuda@redhat.com
 *
 */
public class FirstWizardPage {

	public static final String DOWNLOADABLE_CARTRIDGE = "Code Anything Downloadable cartridge";
	
	public FirstWizardPage() {
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	/**
	 * Import existing application one on the given connection.
	 * 
	 * @param domain of application
	 * @param applicationName
	 */
	public void importExistingApplication(String domain, String applicationName) {
		if (!(new RadioButton(0).isSelected())) {
			new RadioButton(0).click();
		}
		
		new PushButton(OpenShiftLabel.Button.BROWSE).click();
		
		new DefaultShell("Select Existing Application");
		
		List<TreeItem> domains = new DefaultTree().getItems();
		
		for (TreeItem domainItem: domains) {
			if (domainItem.getText().equals(domain)) {
				domainItem.select();
				
				// workaround for tree issues
				domainItem.expand();
				domainItem.collapse();
				domainItem.expand();
				
				domainItem.getItem(applicationName).select();
				
				new WaitUntil(new ButtonWithTextIsActive(new PushButton(
						OpenShiftLabel.Button.OK)), TimePeriod.NORMAL);
				
				new PushButton(OpenShiftLabel.Button.OK).click();
				
				break;
			}
		}
	}
	
	/**
	 * Create new application with the given cartridge.
	 * 
	 * @param base cartridge of application
	 */
	public void createNewApplicationOnBasicCartridge(String cartridge) {
		createNewApplication(true, cartridge);
	}
	
	public void createNewApplicationFromQuickstart(String quickstart) {
		createNewApplication(false, quickstart);
	}
	
	// basic cartridge is true in case of basic cartridge, false in case of quickstart
	// cartridge is either application platform cartridge or quickstart cartridge
	private void createNewApplication(boolean basicCartridge, String cartridge) {
		if (!(new RadioButton(1).isSelected())) {
			new RadioButton(1).click();
		}
		
		// basic cart or quickstart
		TreeItem category;
		if (basicCartridge) {
			category = new DefaultTreeItem("Basic Cartridges");
		} else {
			category = new DefaultTreeItem("Quickstarts");
		}
		
		category.select();
		category.expand();
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		treeViewerHandler.getTreeItem(category, cartridge).select();
	}
	
	/**
	 * Create new application as downloadable cartridge. 
	 * URL must be valid HTTP URL cartridge manifest - manifest.yml
	 * @param URL of downloadable cartridge manifest
	 */
	public void createNewApplicationFromDownloadableCartridge(String URL) {
		if (!(new RadioButton(1).isSelected())) {
			new RadioButton(1).click();
		}
		
		new DefaultTreeItem(DOWNLOADABLE_CARTRIDGE).select();
		
		new LabeledText("Cartridge URL:").setText(URL);
	}
}

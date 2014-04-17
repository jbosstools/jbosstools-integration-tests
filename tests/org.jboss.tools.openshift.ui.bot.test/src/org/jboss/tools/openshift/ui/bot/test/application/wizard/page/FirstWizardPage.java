package org.jboss.tools.openshift.ui.bot.test.application.wizard.page;

import java.util.Iterator;
import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

public class FirstWizardPage {

	public static final String DOWNLOADABLE_CARTRIDGE = "Code Anything Downloadable Cartridge";
	
	public FirstWizardPage() {
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	/**
	 * Create application from existing one on the given connection.
	 * 
	 * @param domain of application
	 * @param applicationName
	 */
	public void createFromExisting(String domain, String applicationName) {
		if (!(new RadioButton(0).isSelected())) {
			new RadioButton(0).click();
		}
		
		new PushButton(OpenShiftLabel.Button.BROWSE).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Select Existing Application"), 
				TimePeriod.NORMAL);
		
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
	public void createNewApplication(String cartridge) {
		if (!(new RadioButton(1).isSelected())) {
			new RadioButton(1).click();
		}
		
		new DefaultTreeItem(cartridge).select();
	}
	
	/**
	 * Create new application as downloadable cartridge. 
	 * URL must be valid HTTP URL cartridge manifest - manifest.yml
	 * @param URL of downloadable cartridge manifest
	 */
	public void createNewApplicationFromDownloadableCartridge(String URL) {
		createNewApplication(DOWNLOADABLE_CARTRIDGE);
		
		new LabeledText("Cartridge URL:").setText(URL);
	}
}

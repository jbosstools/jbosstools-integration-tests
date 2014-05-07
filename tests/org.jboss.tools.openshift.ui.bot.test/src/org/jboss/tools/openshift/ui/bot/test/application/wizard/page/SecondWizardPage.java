package org.jboss.tools.openshift.ui.bot.test.application.wizard.page;

import java.util.List;

import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * Second wizard page of a New application wizard. This page is responsible for
 * application details - domain, application name, gears, scalability, 
 * embeddable cartridges, environment variables and source code.s
 * 
 * @author mlabuda@redhat.com
 *
 */
public class SecondWizardPage {
	
	public SecondWizardPage() {
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
		
		// Wait until data are processed - there is no other way currently
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(
				OpenShiftLabel.Button.BACK)), TimePeriod.LONG);
	}
	
	/**
	 * Fill in application details
	 * @param appName is application name 
	 * @param scalable set true if application is scalable, false otherwise
	 * @param smallGear set true if application should use small gear, false for medium
	 * @param URL if git URL is provided application will be created from template
	 */
	public void fillApplicationDetails(String appName, boolean scalable, boolean smallGear,
			boolean createEnvironmentVariable, String URL) {
		new LabeledText("Name:").setText(appName);

		if (smallGear == false) {
			List<String> gears = new LabeledCombo("Gear profile:").getItems();
			for (String gear: gears) {
				if (gear.equals("int_general_medium") || gear.equals("medium")) {
					new LabeledCombo("Gear profile:").setSelection(gear);
				}
				
			}
		}
			
		if (scalable) {
			new CheckBox(0).click();
		}
		
		// Set URL of source code or environment variables for application
		if (URL != null || createEnvironmentVariable == true) {
			new PushButton(OpenShiftLabel.Button.ADVANCED).click();
		
			if (URL != null) {
				new CheckBox(1).click();
				new LabeledText("Source code:").setText(URL);
			}
			
			if (createEnvironmentVariable) {
				new PushButton(OpenShiftLabel.Button.ENV_VAR).click();
				
				new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ENV_VARS),
						TimePeriod.LONG);
				
				new DefaultShell(OpenShiftLabel.Shell.ENV_VARS).setFocus();
				new PushButton(OpenShiftLabel.Button.ADD).click();
				
				new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_ENV_VAR),
						TimePeriod.LONG);
				
				new DefaultShell(OpenShiftLabel.Shell.EDIT_ENV_VAR).setFocus();
				new DefaultText(0).setText("varname");
				new DefaultText(1).setText("varvalue");
				
				new WaitUntil(new ButtonWithTextIsActive(new PushButton(
						OpenShiftLabel.Button.OK)), TimePeriod.NORMAL);
				
				new PushButton(OpenShiftLabel.Button.OK).click();
				
				new DefaultShell(OpenShiftLabel.Shell.ENV_VARS).setFocus();
				
				new WaitUntil(new ButtonWithTextIsActive(new PushButton(
						OpenShiftLabel.Button.OK)), TimePeriod.NORMAL);
				
				new PushButton(OpenShiftLabel.Button.OK).click();
				
				new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
			}
		}
	}
		
	/**
	 * Add cartridges to new application. Beware! Cartridges can show warning dialog 
	 * in case of incompatible/missing cartridges. Use this wisely.
	 * 
	 * @param cartridges
	 */
	public void addCartridges(String... cartridges) {
		if (cartridges != null) {
			new PushButton(OpenShiftLabel.Button.ADD).click();
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADD_CARTRIDGES),
					TimePeriod.LONG);
			
			for (String cartridge: cartridges) {
				new DefaultTable().getItem(cartridge).select();
				new DefaultTable().getItem(cartridge).setChecked(true);
			}
			
			new WaitUntil(new ButtonWithTextIsActive(new PushButton(
					OpenShiftLabel.Button.OK)), TimePeriod.NORMAL);
			
			new PushButton(OpenShiftLabel.Button.OK).click();
			
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
		}
	}
	
	/** 
	 * Add custom cartridge to application. Beware - URL must be valid URL link.
	 * @param URL of custom cartridge
	 */
	public void addCodeAnythingCartridge(String URL) {
		if (URL != null) {
			new PushButton(OpenShiftLabel.Button.ADD).click();
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADD_CARTRIDGES),
					TimePeriod.LONG);
			
			new DefaultTable().getItem("Code Anything").select();
			new DefaultTable().getItem("Code Anything").setChecked(true);
			
			new LabeledText("Cartridge URL:").setText(URL);
			
			new WaitUntil(new ButtonWithTextIsActive(new PushButton(
					OpenShiftLabel.Button.OK)), TimePeriod.NORMAL);
			
			new PushButton(OpenShiftLabel.Button.OK).click();
			
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
		}
	}
}

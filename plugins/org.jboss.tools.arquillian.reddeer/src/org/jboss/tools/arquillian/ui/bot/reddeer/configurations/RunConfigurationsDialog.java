/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/

package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.condition.WidgetIsFound;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.core.matcher.WithLabelMatcher;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.swt.widgets.Shell;

/**
 * Represents the Run Configurations dialog
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunConfigurationsDialog {

	public static final String DIALOG_TITLE = "Run Configurations";
	
	private static final Logger log = Logger.getLogger(RunConfigurationsDialog.class);
	
	/**
	 * Open the dialog using top menu
	 */
	public void open() {
		// if the dialog is not open, open it
		log.info("Open Run configurations dialog");

		if (isOpen()){
			log.debug("Run configurations dialog has already been open.");
		}
		else{
			log.debug("Run configurations dialog has not been opened yet. Opening via menu.");
			ShellMenuItem menu = new ShellMenuItem("Run", "Run Configurations...");
			menu.select();
		}
		
		new DefaultShell(DIALOG_TITLE);
	}
	
	/**
	 * Select the run configuration
	 * 
	 * @param configuration
	 */
	public void select(RunConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException("Run configuration can't be null");
		}
		TreeItem t = new DefaultTreeItem(configuration.getCategory(), configuration.getName());
		t.select();
		
		new WaitUntil(new WidgetIsFound(org.eclipse.swt.custom.CLabel.class,new WithTextMatcher(configuration.getName())),TimePeriod.DEFAULT, false);
	}

	/**
	 * Create new configuration according the data filled in {@link RunConfiguration}
	 * @param configuration
	 */
	public void createConfiguration(RunConfiguration configuration){
		TreeItem t = new DefaultTreeItem(configuration.getCategory());
		t.select();
				
		new ContextMenuItem("New").select();
		
		/* Added to make test more reliable - intermittent timing-related 
		 * failures (https://issues.jboss.org/browse/JBIDE-22866) were being seen */
		new WaitUntil(new WidgetIsFound(org.eclipse.swt.widgets.Text.class, new WithLabelMatcher("Name:")),TimePeriod.VERY_LONG);
		
		new LabeledText("Name:").setText(configuration.getName());
	}
	
	/**
	 * Run the selected run configuration
	 */
	public void run(){
		log.info("Run the launch configuration");

		String shellText = new DefaultShell().getText();
		Button button = new PushButton("Run");
		button.click();

		new WaitWhile(new ShellIsAvailable(shellText), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);			
	}

	/**
	 * Close the dialog
	 */
	public void close(){
		log.info("Close the launch configuration");

		String shellText = new DefaultShell().getText();
		Button button = new PushButton("Close");
		button.click();

		new WaitWhile(new ShellIsAvailable(shellText));
		new WaitWhile(new JobIsRunning());
	}

	/**
	 * Apply the current configuration
	 */
	public void apply(){
		log.info("Apply the launch configuration");

		Button button = new PushButton("Apply");
		button.click();

		new WaitWhile(new JobIsRunning());
	}

	/**
	 * Revert the launch configuration
	 */
	public void revert(){
		log.info("Revert the launch configuration");

		Button button = new PushButton("Revert");
		button.click();

		new WaitWhile(new JobIsRunning());
	}
	
	/**
	 * Checks if the dialog is open
	 * @return true if the dialog is open, false otherwise
	 */
	public boolean isOpen() {
		try {
			ShellLookup.getInstance().getShell(DIALOG_TITLE, TimePeriod.SHORT);
		} catch (CoreLayerException ex) {
			return false;
		}
		return true;
	}
}

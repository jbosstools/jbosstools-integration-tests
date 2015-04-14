package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.eclipse.swt.widgets.Shell;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.CLabelWithTextIsAvailable;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

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
			Menu menu = new ShellMenu("Run", "Run Configurations...");
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
		
		new WaitUntil(new CLabelWithTextIsAvailable(configuration.getName()), TimePeriod.NORMAL, false);
	}

	/**
	 * Create new configuration according the data filled in {@link RunConfiguration}
	 * @param configuration
	 */
	public void createConfiguration(RunConfiguration configuration){
		TreeItem t = new DefaultTreeItem(configuration.getCategory());
		t.select();
		
		new ContextMenu("New").select();;
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

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.VERY_LONG);
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

		new WaitWhile(new ShellWithTextIsActive(shellText));
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
		Shell shell = ShellLookup.getInstance().getShell(DIALOG_TITLE,TimePeriod.SHORT);
		return (shell != null);		
	}
}

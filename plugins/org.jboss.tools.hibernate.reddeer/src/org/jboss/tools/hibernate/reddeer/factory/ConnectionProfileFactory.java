package org.jboss.tools.hibernate.reddeer.factory;


import org.jboss.reddeer.eclipse.datatools.ui.DatabaseProfile;
import org.jboss.reddeer.eclipse.datatools.ui.view.DataSourceExplorer;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.ConnectionProfileWizard;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.TreeItemRegexMatcher;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * Driver Definition Factory helps to create driver definition based on 
 * database configuration
 * 
 * @author Jiri Peterka
 *
 */
public class ConnectionProfileFactory {
	/**
	 * Creates Connection profile based on DatabaseRequirement configuration
	 * @param conf given database requirement configuration
	 */
	public static void createConnectionProfile(DatabaseConfiguration cfg) {

		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setDatabase(cfg.getProfileName());
		dbProfile.setDriverDefinition(DriverDefinitionFactory.getDriverDefinition(cfg));
		dbProfile.setHostname(cfg.getJdbcString());
		dbProfile.setName(cfg.getProfileName());
		dbProfile.setPassword(cfg.getPassword());
		dbProfile.setUsername(cfg.getUsername());
		dbProfile.setVendor(cfg.getDriverVendor());

		// Driver Definition creation
		ConnectionProfileWizard cpw = new ConnectionProfileWizard();
		cpw.open();
		cpw.createDatabaseProfile(dbProfile);
	}
	
	/**
	 * Deletes connection profile 
	 * @param profileName profile name to delete
	 */
	@SuppressWarnings("unchecked")
	public static void deleteConnectionProfile(String profileName) {
		DataSourceExplorer explorer = new DataSourceExplorer();
		explorer.open();
		new DefaultTreeItem(new TreeItemRegexMatcher("Database Connections"), new TreeItemRegexMatcher(profileName + ".*")).select();
		new ContextMenu("Delete").select();
		String deleteConfirmation = "Delete confirmation";
		RegexMatcher withRegexMatcher = new RegexMatcher(".*" + deleteConfirmation + ".*"); 
		new WaitUntil(new ShellWithTextIsActive(withRegexMatcher));
		new DefaultShell(deleteConfirmation);
		new YesButton().click();
	}
	
}
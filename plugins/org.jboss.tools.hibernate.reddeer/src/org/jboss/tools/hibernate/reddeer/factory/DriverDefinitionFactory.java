package org.jboss.tools.hibernate.reddeer.factory;

import java.util.List;

import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionWizard;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Driver Definition Factory helps to create driver definition based on 
 * database configuration
 * 
 * @author Jiri Peterka
 *
 */
public class DriverDefinitionFactory {
	/**
	 * Creates Driver definition based on DatabaseRequirement configuration
	 * @param conf given database requirement configuration
	 */
	public static void createDatabaseDriverDefinition(DatabaseConfiguration cfg) {

		DriverTemplate dt = getDriverTemplate(cfg);
		DriverDefinition dd = getDriverDefinition(cfg);
		 
		// Driver Definition creation
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		DriverDefinitionPreferencePage preferencePage = new DriverDefinitionPreferencePage();
		preferenceDialog.select(preferencePage);
		
		List<TableItem> items = new DefaultTable().getItems();
		for (int i = 0; i < items.size(); i++) {
			new DefaultTableItem(0).select();
			new PushButton("Remove").click();
			new WaitUntil(new ShellWithTextIsAvailable("Confirm Driver Removal"));
			new YesButton().click();
			new WaitWhile(new ShellWithTextIsAvailable("Confirm Driver Removal"));
			new DefaultShell("Preferences");
		}
		
		
		DriverDefinitionWizard ddw = preferencePage.addDriverDefinition();
		DriverDefinitionPage page = new DriverDefinitionPage();
		page.selectDriverTemplate(dt.getType(),dt.getVersion());
		page.setName(cfg.getDriverName());
		page.addDriverLibrary(dd.getDriverLibrary());
		page.setDriverClass(cfg.getDriverClass());

		ddw.finish();
		preferenceDialog.ok();
	}

	/**
	 * Returns Driver Template instance based on configuration
	 * @param cfg given configuration
	 * @return driver template
	 */
	public static DriverTemplate getDriverTemplate(DatabaseConfiguration cfg) {
		DriverTemplate dt = new DriverTemplate(cfg.getDriverType(),cfg.getDriverTypeVersion());          
		return dt;
	}

	/**
	 * Returns Driver Definition instance based on configuration
	 * @param cfg given configuration
	 * @return driver definition
	 */
	public static DriverDefinition getDriverDefinition(DatabaseConfiguration cfg) {
		// Driver definition      
		DriverDefinition dd = new DriverDefinition();
		dd.setDriverClass(cfg.getDriverClass());
		dd.setDriverLibrary(cfg.getDriverPath());
		dd.setDriverName(cfg.getDriverName());
		dd.setDriverTemplate(getDriverTemplate(cfg));

		return dd;
	}
}

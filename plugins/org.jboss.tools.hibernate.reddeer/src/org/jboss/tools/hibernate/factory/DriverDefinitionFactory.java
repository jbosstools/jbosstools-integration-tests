package org.jboss.tools.hibernate.factory;

import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionWizard;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;

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
	public static void createDatabaseDefinition(DatabaseConfiguration cfg) {

		DriverTemplate dt = getDriverTemplate(cfg);
		DriverDefinition dd = getDriverDefinition(cfg);

		 
		// Driver Definition creation
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		DriverDefinitionPreferencePage preferencePage = new DriverDefinitionPreferencePage();
		preferenceDialog.select(preferencePage);
		DriverDefinitionWizard ddw = preferencePage.addDriverDefinition();
		DriverDefinitionPage page = new DriverDefinitionPage();
		page.selectDriverTemplate(dt.getType(),dt.getVersion());
		page.setName(cfg.getDriverName());
		page.addDriverLibrary(dd.getDriverLibrary());
		page.setDriverClass(cfg.getDriverClass());
		ddw.finish();
		preferencePage.ok();
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

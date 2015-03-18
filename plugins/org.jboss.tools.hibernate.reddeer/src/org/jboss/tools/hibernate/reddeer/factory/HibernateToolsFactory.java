package org.jboss.tools.hibernate.reddeer.factory;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.console.KnownConfigurationsView;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationLocationPage;
import org.jboss.tools.hibernate.reddeer.console.NewConfigurationSettingPage;
import org.jboss.tools.hibernate.reddeer.console.NewHibernateConfigurationWizard;

/**
 * Factory for common hibernate tools tasks
 * @author Jiri Peterka
 *
 */
public class HibernateToolsFactory {

	
	/**
	 * Create Hibernate Configuration file 
	 * @param cfg configuration
	 * @param project project name
	 * @param cfgFile hibernate configuration file
	 * @param generateConsole when true hibernate console configuration is generated
	 */
	public static void testCreateConfigurationFile(DatabaseConfiguration cfg, String project, String cfgFile, boolean generateConsole) {		
		NewHibernateConfigurationWizard wizard = new NewHibernateConfigurationWizard();
		wizard.open();
		NewConfigurationLocationPage p1 = new NewConfigurationLocationPage();
		p1.setLocation(project,"src");		
		wizard.next();

		NewConfigurationSettingPage p2 = new NewConfigurationSettingPage();
		p2.setDatabaseDialect("H2");
		p2.setDriverClass(cfg.getDriverClass());
		p2.setConnectionURL(cfg.getJdbcString());
		p2.setUsername(cfg.getUsername());		
		
		if (generateConsole) {
			p2.setCreateConsoleConfiguration(generateConsole);
		}
		
		wizard.finish();
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem ti = new DefaultTreeItem(project,"src",cfgFile);
		ti.doubleClick();
		new DefaultEditor(cfgFile);
		
	}

	/**
	 * Sets hibernate version to given console
	 * @param consoleName console name
	 * @param hibernateVersion hibernate version 
	 */
	public static void setHibernateVersion(String consoleName, String hbVersion) {
		KnownConfigurationsView v = new KnownConfigurationsView();
		v.open();
		v.openConsoleConfiguration(consoleName);
		new LabeledCombo("Hibernate Version:").setSelection(hbVersion);
		new OkButton().click();	
	}
}

package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.datatools.ui.view.DataSourceExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.forge.reddeer.ui.wizard.ConnectionProfileWizardPage;
import org.jboss.tools.forge.ui.bot.test.util.DatabaseUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for connection profile creation tests
 * 
 * @author Jan Richter
 *
 */
public class ConnectionProfileWizardTest extends WizardTestBase {

	private static final String CONNECTION_NAME = "sakila";
	private static final String SAKILA_URL = "jdbc:h2:tcp://localhost/sakila";
	private static final String SAKILA_USERNAME = "sa";
	private static final String SAKILA_H2_DRIVER = "h2-1.3.161.jar";
	private String dbFolder = System.getProperty("database.path");

	@Before
	public void prepare() {
		assertNotNull("Property 'database.path' is not defined!", dbFolder);
		DatabaseUtils.runSakilaDB(dbFolder);
	}

	@Test
	public void testCreateProfile() {
		String path = null;
		try {
			path = new File(dbFolder).getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull("Database path is null", path);

		WizardDialog dialog = getWizardDialog("Connection: Create Profile", "(Connection: Create Profile).*");
		ConnectionProfileWizardPage page = new ConnectionProfileWizardPage();
		page.setConnectionName(CONNECTION_NAME);
		page.setJdbcUrl(SAKILA_URL);
		page.setUserName(SAKILA_USERNAME);
		page.setDriverLocation(path + File.separator + SAKILA_H2_DRIVER);
		page.setHibernateDialect("H2 Database : org.hibernate.dialect.H2Dialect");
		page.toggleVerifyConnection(true);
		dialog.finish(TimePeriod.LONG);

		DataSourceExplorer dsExplorer = new DataSourceExplorer();
		dsExplorer.open();
		assertTrue("Missing database connection for " + CONNECTION_NAME,
				dsExplorer.getDatabaseConnections().contains(CONNECTION_NAME));

		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select("Data Management", "Connectivity", "Driver Definitions");

		Table table = new DefaultTable();
		assertTrue("Driver definition for " + CONNECTION_NAME + " is missing", table.containsItem(CONNECTION_NAME));
	}

	@Override
	public void cleanup() {
		super.cleanup();
		DatabaseUtils.stopSakilaDB();
	}
}

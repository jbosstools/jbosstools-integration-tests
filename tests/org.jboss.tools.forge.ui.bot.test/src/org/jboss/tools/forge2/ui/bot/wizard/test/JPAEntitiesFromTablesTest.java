package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.tools.forge.reddeer.ui.wizard.ConnectionProfileWizardPage;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardFirstPage;
import org.jboss.tools.forge.reddeer.ui.wizard.EntitiesFromTablesWizardSecondPage;
import org.jboss.tools.forge.ui.bot.test.util.DatabaseUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing JPA entity generation from database tables
 * 
 * @author Jan Richter
 *
 */
public class JPAEntitiesFromTablesTest extends WizardTestBase {

	private String dbFolder = System.getProperty("database.path");
	private List<String> tableNames = new ArrayList<String>();

	private static final String H2_DIALECT = "H2 Database : org.hibernate.dialect.H2Dialect";
	private static final String PACKAGE = GROUPID + ".model";
	private static final String PROFILE_NAME = "sakila";
	private static final String SAKILA_URL = "jdbc:h2:tcp://localhost/sakila";
	private static final String SAKILA_USERNAME = "sa";
	private static final String SAKILA_H2_DRIVER = "h2-1.3.161.jar";

	@Before
	public void prepare() {
		assertNotNull(dbFolder);
		DatabaseUtils.runSakilaDB(dbFolder);
		newProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);

		WizardDialog dialog = getWizardDialog("Connection: Create Profile", "(Connection: Create Profile).*");
		ConnectionProfileWizardPage page = new ConnectionProfileWizardPage();
		page.setConnectionName(PROFILE_NAME);
		page.setJdbcUrl(SAKILA_URL);
		page.setUserName(SAKILA_USERNAME);
		page.setDriverLocation(dbFolder + "/" + SAKILA_H2_DRIVER);
		page.setHibernateDialect(H2_DIALECT);
		dialog.finish(TimePeriod.LONG);
	}

	@Test
	public void testGenerateEntities() {
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		WizardDialog dialog = getWizardDialog("JPA: Generate Entities From Tables",
				"(JPA: Generate Entities From Tables).*");
		EntitiesFromTablesWizardFirstPage firstPage = new EntitiesFromTablesWizardFirstPage();
		firstPage.setPackage(PACKAGE);
		assertTrue("Missing connection profile selection", firstPage.getAllProfiles().contains(PROFILE_NAME));
		firstPage.setConnectionProfile(PROFILE_NAME);
		dialog.next();

		EntitiesFromTablesWizardSecondPage secondPage = new EntitiesFromTablesWizardSecondPage();
		List<TableItem> tables = secondPage.getAllTables();
		assertFalse("No database tables found", tables.isEmpty());
		for (TableItem item : tables) {
			tableNames.add(item.getText());
		}
		secondPage.selectAll();
		dialog.finish();

		checkEntityClasses();
	}

	@Override
	public void cleanup() {
		super.cleanup();
		DatabaseUtils.stopSakilaDB();
	}

	private void checkEntityClasses() {
		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		project.refresh();
		ProjectItem model = project.getProjectItem("Java Resources", "src/main/java", PACKAGE);

		for (String name : tableNames) {
			// replace UNDERSCORES with UpperCamelCase
			String entityName = "";
			String[] nameParts = name.split("_");
			for (int i = 0; i < nameParts.length; i++) {
				entityName += nameParts[i].charAt(0) + nameParts[i].substring(1).toLowerCase();
			}

			assertTrue("Class for entity " + entityName + " is missing", model.containsItem(entityName + ".java"));
		}
	}
}
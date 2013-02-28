package org.jboss.tools.hb.ui.bot.test.console;

import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.hb.ui.bot.common.ConfigurationFile;
import org.jboss.tools.hb.ui.bot.common.Tree;
import org.jboss.tools.hb.ui.bot.test.HibernateBaseTest;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfiguration;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfiguration.DatabaseConnection;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfigurationView;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.junit.Test;

/**
 * Create Hibernate Console UI Bot Test
 * @author jpeterka
 *
 */
@Require(db = @DB, clearProjects = true)
public class CreateConsoleConfigurationTest extends HibernateBaseTest {

	final String prjName = "configurationtest";
	
	@Test
	public void createConsoleConfigurationTest() {
		emptyErrorLog();
		importTestProject("/resources/prj/" + prjName);
		createConfigurationFile();
		createHibernateConsole();
		expandDatabaseInConsole();
		checkErrorLog();
	}
	
	private void createConfigurationFile() {
		ConfigurationFile.create(new String[]{prjName,"src"},"hibernate.cfg.xml",false);		
	}

	private void createHibernateConsole() {
		
		HibernateConfiguration hc = new HibernateConfiguration();
		hc.setName(prjName);
		hc.setDatabaseConnection(DatabaseConnection.hibernateConfiguredConection);
		hc.setProject(prjName);
		hc.setConfigurationFile(prjName + "/src/hibernate.cfg.xml");
		
		HibernateConfigurationView hcView = new HibernateConfigurationView();
		hcView.addConfiguration(hc);
	}
	
	private void expandDatabaseInConsole() {
		SWTBot viewBot = open.viewOpen(ActionItem.View.HibernateHibernateConfigurations.LABEL).bot();
		SWTBotTreeItem db = Tree.select(viewBot, prjName,"Database");
		db.expand();

		// expand PUBLIC node or whatever
		SWTBotTreeItem pub = db.getItems()[0];
		final int limit = 10; // 10s
		final int sleep = 1000;
		int counter = 0;
				
		while(counter < limit) {
			if (pub.widget.isDisposed()) {
				pub = db.getItems()[0];
				bot.sleep(sleep);
			}
			if (pub.getText().equals("<Reading schema error: Getting database metadata >")) {
				fail("Can't load data, DB not accessible");
			}		
			if (pub.getText().equals("Pending...") &&  counter < limit) {
				bot.sleep(sleep);
				counter++;
				log.info("Waiting for database loading...");
			}
			else
			{
				log.info("DB loaded");
				break;
			}
		}
		
		List<String> tables = Tree.getSubNodes(viewBot, pub);
		assertTrue("Table must contain tables", tables.size()> 0);
		for (String s : tables) {
			log.info("Table:" + s);
		}
	}
}

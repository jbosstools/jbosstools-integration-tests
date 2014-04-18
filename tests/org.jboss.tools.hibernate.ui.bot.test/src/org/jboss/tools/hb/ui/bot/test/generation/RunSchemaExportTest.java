package org.jboss.tools.hb.ui.bot.test.generation;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Run schema export from hibernate configuration context menu test
 * 
 * @author jpeterka
 * 
 */
public class RunSchemaExportTest extends HibernateRedDeerTest {


	final String hc = "pre-hibernate40";
	
	@Test
	public void runSchemaExportTest() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/hibernate40");
		runShemaExport();
	}

	private void runShemaExport() {
		DefaultTreeItem ti = new DefaultTreeItem(hc, "Configuration");
		ti.select();
		new ContextMenu("Run SchemaExport").select();
		
		String title = "Run SchemaExport";
		new WaitUntil(new ShellWithTextIsActive(title));
		new DefaultShell(title);
	}
}

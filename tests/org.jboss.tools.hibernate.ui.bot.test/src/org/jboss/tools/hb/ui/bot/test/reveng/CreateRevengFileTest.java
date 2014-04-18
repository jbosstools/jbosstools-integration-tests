package org.jboss.tools.hb.ui.bot.test.reveng;


import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate create reveng file ui bot test
 * 
 * @author jpeterka
 * 
 */

public class CreateRevengFileTest extends HibernateRedDeerTest {
	
	final String prj = "hibernate35";
	final String pkg = "org.reveng";
	
	@Test
	public void createRevengFileTest() {
		importProject("/resources/prj/hibernate35");
		createRevengFilesFromPackage();
	}

	private void createRevengFilesFromPackage() {
		// Select Both classes
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultTreeItem defaultTreeItem = new DefaultTreeItem(prj,"src",pkg,"hibernate.reveng.xml");
		defaultTreeItem.select();
	}
}

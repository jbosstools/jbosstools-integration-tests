package org.jboss.tools.hb.ui.bot.test.mappingfile;


import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate create mapping files ui bot test
 * 
 * @author jpeterka
 * 
 */
public class CreateMappingFileTest extends HibernateRedDeerTest {
	
	final String prj = "hibernate35";
	final String pkg = "org.mapping";
	
	@Test
	public void createMappingFileTest() {
		importProject("/resources/prj/hibernate35");
		createMappingFilesFromPackage();
	}

	private void createMappingFilesFromPackage() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "src", pkg );		
						
		// TODO Create mapping files 
	}
}

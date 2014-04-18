package org.jboss.tools.hb.ui.bot.test.mappingfile;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate perspective ui bot test
 * 
 * @author jpeterka
 * 
 */
public class EditMappingFileTest extends HibernateRedDeerTest {
		
	final String prj = "hibernate35";
	final String pkg = "org.mapping.edit";
	final String file1 = "Customer.hbm.xml";
	
	@Test
	public void editMappingFile() {
		importProject("/resources/prj/hibernate35");
		editFile();
	}

	private void editFile() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "src", pkg, file1 );
			
		DefaultEditor editor = new DefaultEditor(file1);
		editor.save();
	}
}

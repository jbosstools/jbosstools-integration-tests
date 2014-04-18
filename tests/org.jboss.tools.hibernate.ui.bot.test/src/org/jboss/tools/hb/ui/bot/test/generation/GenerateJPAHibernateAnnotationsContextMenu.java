package org.jboss.tools.hb.ui.bot.test.generation;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hb.ui.bot.common.StringHelper;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

public class GenerateJPAHibernateAnnotationsContextMenu extends HibernateRedDeerTest {
	
	final String prj = "configurationtest";
	final String out = "src";
	final String hbcfg = "hibernate.cfg.xml";
	final String pckg = "org.test.generation.annotation";
	

	
	@Test
	public void testGenerateJPAHibernateAnnotationsContextMenu() {
		importProject("/resources/prj/" + prj);
		generateJPAHibernateAnnotationsContextMenu();
	}

	
	private void generateJPAHibernateAnnotationsContextMenu() {
		PackageExplorer pe = new PackageExplorer();
		pe.selectProjects(prj);		
		
		TreeItem item = new DefaultTreeItem(prj,"src",pckg,"Dog.java");
		item.select();
		
		Menu menu = new ContextMenu("Source","Generate Hibernate/JPA annotations");
		menu.select();
		
		new DefaultShell("Hibernate: add JPA annotations");
		new PushButton("Next >").click();
		new PushButton("Finish").click();
		
		new WorkbenchShell();
		new DefaultTreeItem(prj,"src",pckg,"Dog.java");
		item.doubleClick();
		
		TextEditor editor = new TextEditor("Dog.java");
		StringHelper sh = new StringHelper(editor.getText());
		sh.getPositionAfter("@Entity");		
	}
}
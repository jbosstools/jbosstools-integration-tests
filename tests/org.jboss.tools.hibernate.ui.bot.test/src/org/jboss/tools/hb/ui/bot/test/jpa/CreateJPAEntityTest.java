package org.jboss.tools.hb.ui.bot.test.jpa;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hb.ui.bot.common.StringHelper;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Create JPA Entity ui bot test
 */
public class CreateJPAEntityTest extends HibernateRedDeerTest {
	
	final String prj = "jpatest35";
	final String pkg = "org.newentity";
	final String ent = "NewEntity"; 
	final String ext = ".java";
	
	@Test
	public void createJPAProject() {
		importProject("/resources/prj/" + prj);
		importProject("/resources/prj/" + "hibernatelib");
		createJPAEntity();
		addIdIntoEntity();
	}

	private void addIdIntoEntity() {
		TextEditor editor = new TextEditor(ent + ext);
		StringHelper sh = new StringHelper(editor.getText());
		sh.getPositionBefore("private static final long serialVersionUID");
		//editor.selectRange(p.y, p.x, 0);
		//editor.insertText("@Id\nprivate long id;\n");
		editor.save();
		//editor.selectRange(0, 0, editor.getText().length());
		new ShellMenu("Source","Format").select();		
		editor.save();		
	}

	private void createJPAEntity() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(prj);
		
		DefaultTreeItem i  = new DefaultTreeItem(prj,"src", pkg,ent + ext);
		i.select();
	}
}

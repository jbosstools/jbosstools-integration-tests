package org.jboss.tools.hb.ui.bot.test.view;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hb.ui.bot.common.StringHelper;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.junit.Test;

public class PackageInfoTest extends HibernateRedDeerTest {
	final String prj = "jpatest40";
	final String pkg = "org.packageinfo";
	final String pkginfo = "package-info.java";
	final String entity = "Table.java";
	
	@Test
	public void jpaDetailsViewTest() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/jpatest40");
		
		checkGeneratorInPackageInJPADetailsView();
	}

	private void checkGeneratorInPackageInJPADetailsView() {
	

		JPADetailsView v = new JPADetailsView();
		v.open();
		ProjectExplorer p = new ProjectExplorer();
		p.open();
		DefaultTreeItem i = new DefaultTreeItem(prj, "src", pkg, pkginfo);
		i.select();
		
		
		TextEditor editor = new TextEditor(pkginfo);
		StringHelper sh = new StringHelper(editor.getText());
		
		String str = "@GenericGenerator";
		sh.getPositionBefore(str);
		// editor..selectRange(pos.y, pos.x, 0);					
		
		final String genname = "myuuidgen";
		final String strategy = "uuid";
		DefaultTable table = new DefaultTable();
		assertTrue(table.containsItem(genname));
		table.getItem(genname).select();
		
		assertTrue(new LabeledText("Name:").getText().equals(genname));
		assertTrue(new LabeledCombo("Strategy:").getText().equals(strategy));
	}
	
}

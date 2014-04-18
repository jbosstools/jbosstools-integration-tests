package org.jboss.tools.hb.ui.bot.test.view;


import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.junit.Test;

/**
 * JPA View ui bot test
 * - Hibernate JPA annotation can be edited in JPA Detail view
 * @author jpeterka
 * 
 */
public class JPADetailViewTest extends HibernateRedDeerTest {
		

	final String prj = "jpatest40";
	final String pkg = "org.jpadetails";
	final String entity = "Machine.java";
	
	@Test
	public void jpaDetailsViewTest() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/jpatest40");
		
		addNativeQueryViaJPAView();
		modifyNativeQueryAndCheckJPAView();
	}


	private void addNativeQueryViaJPAView() {
		
		JPADetailsView jpaDetailsView = new JPADetailsView();
		jpaDetailsView.open();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "src", pkg, entity).select();

		
		TextEditor editor = new TextEditor(entity);
		editor.save();	
	}
	
	private void modifyNativeQueryAndCheckJPAView() {
		TextEditor editor = new TextEditor(entity);
		editor.save();

		/*
		String str = "SELECT * FROM MACHINE";
		Point pos = sh.getPositionAfter(str);
		editor.toTextEditor().selectRange(pos.y, pos.x + 1, 0);
		editor.toTextEditor().insertText(" ORDER BY name");
		editor.save();
		*/
	}
}

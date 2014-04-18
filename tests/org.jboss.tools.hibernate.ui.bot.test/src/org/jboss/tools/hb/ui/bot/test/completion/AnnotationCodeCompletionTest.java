package org.jboss.tools.hb.ui.bot.test.completion;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate annotation test. Annotation code completion functionality is checked in Entity class
 */
public class AnnotationCodeCompletionTest extends HibernateRedDeerTest {
	
	final String prj = "jpatest35";
	final String pkg = "org.test.completion";
	final String ent = "CodeCompletionEntity.java";

	final String ext = ".java";
	
	@Test
	public void annotationCodeCompletionTest() {
		importProject("/resources/prj/" + prj);
		importProject("/resources/prj/" + "hibernatelib");
		openJPAEntity();
		tryCodeCompletion();
		
	}

	private void tryCodeCompletion() {		
		new TextEditor(ent);
		
		/*
		Point p = sh.getPositionAfter("@Entity");
		editor.toTextEditor().selectRange(p.y, p.x + 1, 0);
		String annoStart = "@Tab";
		editor.toTextEditor().insertText("\n" + annoStart);
		sh = new StringHelper(editor.toTextEditor().getText());
		p = sh.getPositionAfter(annoStart);
		editor.selectRange(p.y,p.x + 1,0);
		*/
	}

	private void openJPAEntity() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "src", pkg, ent);
	}
}

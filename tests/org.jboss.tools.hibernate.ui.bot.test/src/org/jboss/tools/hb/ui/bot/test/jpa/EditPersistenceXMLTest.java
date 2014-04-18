package org.jboss.tools.hb.ui.bot.test.jpa;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hb.ui.bot.common.StringHelper;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Create JPA Project ui bot test
 * Hibernate details can be edited in persistene.xml editor
 * @author jpeterka
 * 
 */
public class EditPersistenceXMLTest extends HibernateRedDeerTest {
	
	final String prj = "jpatest35";
	
	@Test
	public void editPersistenceXMLTest() {
		importProject("/resources/prj/" + prj);
		openPersistenceXML();
		editPersistenceXMLHibernatePage();
		checkCAInConfigurationEditorXML();
	}

	private void openPersistenceXML() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultTreeItem i = new DefaultTreeItem(prj,"JPA Content","persistence.xml");
		i.select();
	}

	private void editPersistenceXMLHibernatePage() {
		TextEditor editor = new TextEditor("persistence.xml");
		
		new LabeledText("Username:").setText("sa"); 
		editor.save();
		
		// Check xml content
		String text = editor.getText();
		StringHelper helper = new StringHelper(text);		
		String str  =  "<property name=\"hibernate.dialect\" value=\"org.hibernate.dialect.HSQLDialect\"/>";
		helper.getPositionBefore(str);
		str  =  "<property name=\"hibernate.connection.driver_class\" value=\"org.hsqldb.jdbcDriver\"/>";
		helper.getPositionBefore(str);	
	}
	
	private void checkCAInConfigurationEditorXML() {
		TextEditor editor = new TextEditor("persistence.xml");		
				
		// Code completion
		String text = editor.getText();
		StringHelper helper = new StringHelper(text);
		helper.getPositionBefore("</persistence-unit>");
		//editor.toTextEditor().selectRange(p.y, p.x, 0);
		editor.save();
		
		// CA bot problem, need to investigage, not a JBT bug
		//ContentAssistBot ca = new ContentAssistBot(editorExt);
		//ca.useProposal("class");
		
		editor.save();
	}
}

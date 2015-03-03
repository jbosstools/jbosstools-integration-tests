package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hb.ui.bot.common.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test edits persistenceXML File 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
public class PersistenceXMLFileTest extends HibernateRedDeerTest {

	private final String PRJ = "jpa21-hibernate-project"; 
    
    @Before
	public void testConnectionProfile() {
    	importProject(PRJ);
	}
    
    @Test
    public void editPersistenceXMLFile()
    {    	    
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultTreeItem i = new DefaultTreeItem(PRJ,"JPA Content","persistence.xml");
		i.doubleClick();
		DefaultEditor editor = new DefaultEditor("persistence.xml");
		new DefaultCTabItem("Hibernate").activate();
		new LabeledText("Username:").setText("sa");
		new LabeledCombo("Database dialect:").setSelection("H2");
		editor.save();
		
		// Check Persistence XML source
		new DefaultCTabItem("Source").activate();
		DefaultStyledText dst = new DefaultStyledText();		
		String text = dst.getText();
		StringHelper helper = new StringHelper(text);		
		String str  =  "<property name=\"hibernate.dialect\" value=\"org.hibernate.dialect.H2Dialect\"/>";
		helper.getPositionBefore(str);
		str  =  "<property name=\"hibernate.connection.username\" value=\"sa\"/>";
		helper.getPositionBefore(str);	
	}
	
    
	@After
	public void cleanUp() {
	}
}
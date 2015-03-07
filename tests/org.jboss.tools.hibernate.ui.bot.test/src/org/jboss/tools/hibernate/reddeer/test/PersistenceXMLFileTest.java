package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.common.StringHelper;
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

	private String prj = "ecl-jpa21"; 
    
    
	private void prepare() {
    	importProject(prj);
	}
	
	@Test 
	public void editPersistenceXMLFileJPA10() {
		prj = "ecl-jpa10";
		editPersistenceXMLFile();
	}
	
	@Test 
	public void editPersistenceXMLFileJPA20() {
		prj = "ecl-jpa20";
		editPersistenceXMLFile();
	}

	@Test 
	public void editPersistenceXMLFileJPA21() {
		prj = "ecl-jpa21";
		editPersistenceXMLFile();
	}
    
    private void editPersistenceXMLFile()
    {    	    
    	prepare();
    	
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultTreeItem i = new DefaultTreeItem(prj,"JPA Content","persistence.xml");
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
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
}
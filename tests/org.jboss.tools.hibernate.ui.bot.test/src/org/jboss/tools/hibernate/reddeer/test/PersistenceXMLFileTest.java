package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.common.StringHelper;
import org.jboss.tools.hibernate.reddeer.editor.JpaXmlEditor;
import org.junit.After;
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
		
		JpaXmlEditor pexml = new JpaXmlEditor();
		pexml.setHibernateUsername("sa");
		pexml.setHibernateDialect("H2");
		pexml.save();
		
		// Check Persistence XML source
		String text = pexml.getSourceText();
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
package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.common.XPathHelper;
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
	private static final Logger log = Logger.getLogger(PersistenceXMLFileTest.class);
    
	@After
	public void cleanUp() {
		deleteAllProjects();
	}
	
	private void prepare() {
		log.step("Import JPA Project");
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
    	
    	log.step("Open persistence xml file");
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		DefaultTreeItem i = new DefaultTreeItem(prj,"JPA Content","persistence.xml");
		i.doubleClick();
		
    	log.step("In editor set some hibernate properties on hibernate tab");
		JpaXmlEditor pexml = new JpaXmlEditor();
		pexml.setHibernateUsername("sa");
		pexml.setHibernateDialect("H2");
		log.step("Save the file");
		pexml.save();

		String usernameProp = "hibernate.connection.username";
		String dialectProp = "hibernate.dialect";		

		String usernameExpected = "sa";
		String dialectExpected = "org.hibernate.dialect.H2Dialect";		

		XPathHelper xh = XPathHelper.getInstance();
		String text = pexml.getSourceText();
		String usrnameVal = xh.getPersistencePropertyValue(usernameProp,text);
		assertTrue("sa value is expected",usrnameVal.equals(usernameExpected));
		String dialectVal = xh.getPersistencePropertyValue(dialectProp, text);
		assertTrue("H2 value is expected",dialectVal.equals(dialectExpected));
	}
}
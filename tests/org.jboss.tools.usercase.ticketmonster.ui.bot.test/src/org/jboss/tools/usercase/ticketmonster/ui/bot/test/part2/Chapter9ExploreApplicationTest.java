package org.jboss.tools.usercase.ticketmonster.ui.bot.test.part2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Chapter9ExploreApplicationTest extends AbstractPart2Test{

	private Project ticketMonsterProject;
	
	@Before
	public void importProject(){
		createTicketMonsterEAP6();
		ProjectExplorer pe = new ProjectExplorer();
		ticketMonsterProject = pe.getProject(TICKET_MONSTER_NAME);
		ticketMonsterProject.select();
		ticketMonsterProject.getTreeItem().doubleClick(); //expand does not work
	}
	
	@Test
	public void explorePom(){
		assertTrue("Pom.xml is missing in Ticket Monster project",ticketMonsterProject.containsItem("pom.xml"));
		ticketMonsterProject.getProjectItem("pom.xml").open();
		new DefaultEditor("pom.xml");
		new DefaultCTabItem("Overview").activate();
		new DefaultCTabItem("Dependencies").activate();
		new DefaultCTabItem("Dependency Hierarchy").activate();
		new DefaultCTabItem("Effective POM").activate();
		new DefaultCTabItem("pom.xml").activate();
		checkBomVersionInPom(new DefaultStyledText().getText());
	}
	
	@Test
	public void explorePackages(){
		assertTrue("sources are missing in Ticket Monster project",ticketMonsterProject.containsItem("Java Resources","src/main/java"));
		TreeItem projectItem= ticketMonsterProject.getTreeItem();
		TreeItem sources = projectItem.getItem("Java Resources").getItem("src/main/java");
		assertTrue(sources.getItems().size() == 6);
		assertTrue(sources.getItems().get(0).getText().equals(TICKET_MONSTER_PACKAGE+"."+PACKAGE_CONTROLLER));
		assertTrue(sources.getItems().get(1).getText().equals(TICKET_MONSTER_PACKAGE+"."+PACKAGE_DATA));
		assertTrue(sources.getItems().get(2).getText().equals(TICKET_MONSTER_PACKAGE+"."+PACKAGE_MODEL));
		assertTrue(sources.getItems().get(3).getText().equals(TICKET_MONSTER_PACKAGE+"."+PACKAGE_REST));
		assertTrue(sources.getItems().get(4).getText().equals(TICKET_MONSTER_PACKAGE+"."+PACKAGE_SERVICE));
		assertTrue(sources.getItems().get(5).getText().equals(TICKET_MONSTER_PACKAGE+"."+PACKAGE_UTIL));
	}
	
	@Test
	public void exploreResources(){
		assertTrue("resources are missing in Ticket Monster project",ticketMonsterProject.containsItem("Java Resources","src/main/resources"));
		TreeItem projectItem= ticketMonsterProject.getTreeItem();
		TreeItem resources = projectItem.getItem("Java Resources").getItem("src/main/resources");
		assertTrue(resources.getItems().size() == 2);
		assertTrue(resources.getItem("META-INF").getItems().size() == 1);
		assertTrue(resources.getItem("META-INF").getItems().get(0).getText().equals("persistence.xml"));
		assertTrue(resources.getItems().get(1).getText().equals("import.sql"));
	}
	
	@Test
	public void exploreTests(){
		assertTrue("tests are missing in Ticket Monster project",ticketMonsterProject.containsItem("Java Resources","src/test/java"));
		TreeItem projectItem= ticketMonsterProject.getTreeItem();
		TreeItem tests = projectItem.getItem("Java Resources").getItem("src/test/java");
		assertTrue(tests.getItems().size() == 1);
		assertTrue(tests.getItem(TICKET_MONSTER_PACKAGE+".test").getItems().size() == 1);
		assertTrue(tests.getItem(TICKET_MONSTER_PACKAGE+".test").getItems().get(0).getText().equals("MemberRegistrationTest.java"));
	}
	
	//@Test
	public void exploreWebFiles(){
		assertTrue("web sources are missing in Ticket Monster project",ticketMonsterProject.containsItem("src","main","webapp"));
		TreeItem projectItem= ticketMonsterProject.getTreeItem();
		TreeItem webResources = projectItem.getItem("src").getItem("main").getItem("webapp");
		assertTrue(webResources.getItems().size() == 4);
		webResources.getItem("index.xhtml").doubleClick();
		new DefaultEditor("index.xhtml");
		new DefaultCTabItem("Visual/Source").activate();
		new DefaultCTabItem("Source").activate();
		new DefaultCTabItem("Preview").activate();
	}
	
	@Test
	public void exploreConfigFiles(){
		assertTrue("web sources are missing in Ticket Monster project",ticketMonsterProject.containsItem("src","main","webapp"));
		TreeItem projectItem= ticketMonsterProject.getTreeItem();
		TreeItem configResources = projectItem.getItem("src").getItem("main").getItem("webapp").getItem("WEB-INF");
		checkResources(configResources);
	}
	
	private void checkResources(TreeItem configResources){
		assertEquals(4,configResources.getItems().size());
		List<String> items = new ArrayList<String>();
		for(TreeItem item: configResources.getItems()){
			items.add(item.getText());
		}
		assertTrue(items.contains("beans.xml"));
		assertTrue(items.contains("faces-config.xml"));
		assertTrue(items.contains("templates"));
		assertTrue(items.contains(TICKET_MONSTER_NAME+"-ds.xml"));
		
	}
	
	private void checkBomVersionInPom(String xml){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		String bom_version = null;
		try {
			expr = xpath.compile("/project/properties/version.jboss.bom.eap");
			bom_version = (String) expr.evaluate(doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("jboss bom version in pom differs from the one given by wizard",version,bom_version);
		
	}
}

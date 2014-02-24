package org.jboss.tools.maven.ui.bot.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MavenConversionTest extends AbstractMavenSWTBotTest{
	
	public static final String WEB_PROJECT_NAME = "Web Project";
	
	@BeforeClass
	public static void setup(){
		setPerspective("Java EE");
	}
	
	@After
	public void clean(){
		deleteProjects(true, true);
	}
	
	@Test
	public void deleteDependenciesAfterConversion(){
		createWithRuntime();
		new CheckBox("Delete original references from project").toggle(true);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+WEB_PROJECT_NAME);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();
		List<TreeItem> it = new DefaultTree(1).getItems();
		assertTrue("project contains more libraries than expected",it.size()==2);
		assertTrue("No JRE in build path after conversion",it.get(0).getText().contains("JRE"));
		assertTrue("No maven dependencies after conversion",it.get(1).getText().contains("Maven Dependencies"));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+WEB_PROJECT_NAME),TimePeriod.NORMAL);
	}
	
	@Test
	public void keepDependenciesAfterConversion(){
		createWithRuntime();
		new CheckBox("Delete original references from project").toggle(false);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+WEB_PROJECT_NAME);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();
		List<TreeItem> it = new DefaultTree(1).getItems();
		assertTrue("project contains more libraries than expected",it.size()==3);
		assertTrue("No JRE in build path after conversion",it.get(1).getText().contains("JRE"));
		assertTrue("No maven dependencies after conversion",it.get(2).getText().contains("Maven Dependencies"));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+WEB_PROJECT_NAME),TimePeriod.NORMAL);
	}
	
	@Test
	public void changeIdentifiedDependency(){
		createWithRuntime();
		new DefaultTable().getItem(1).doubleClick(2);
		
		new DefaultShell("Edit dependency");
		new LabeledText("Group Id:").setText("maven.conversion.test.groupID");
		new LabeledText("Artifact Id").setText("maven.conversion.test.artifactID");
		new LabeledText("Version:").setText("1.0.0");
		new LabeledText("Classifier:").setText("b3");
		new DefaultCombo("Type:").setSelection("rar");
		new DefaultCombo("Scope:").setSelection("provided");
		new CheckBox("Optional").toggle(true);
		new PushButton("OK").click();
		new DefaultShell("Convert to Maven Dependencies");
		new WaitUntil(new WidgetIsEnabled(new PushButton("Finish")));
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		Map<Object,Object> toCheck = new HashMap<Object, Object>();
		toCheck.put("groupId", "maven.conversion.test.groupID");
		toCheck.put("artifactId", "maven.conversion.test.artifactID");
		toCheck.put("version","1.0.0");
		toCheck.put("classifier","b3");
		toCheck.put("type","rar");
		toCheck.put("scope","provided");
		toCheck.put("optional","true");
		checkDependency(WEB_PROJECT_NAME, toCheck);
		
	}
	
	@Test
	public void testAddRepositoryLinkInConversionWizard(){
		createWithRuntime();
		new DefaultTable().getItem(1).doubleClick(2);
		
		new DefaultShell("Edit dependency");
		new LabeledText("Group Id:").setText("antlr");
		new LabeledText("Artifact Id").setText("antlr");
		new LabeledText("Version:").setText("2.7.7-redhat-2");
		new DefaultCombo("Type:").setText("jar");
		new PushButton("OK").click();
		new DefaultShell("Convert to Maven Dependencies");
		new DefaultLink(0).click();
		boolean shellIsOpened = true;
		try{
			new DefaultShell("Maven Repositories");
		} catch (SWTLayerException ex){
			shellIsOpened = false;
		}
		if(shellIsOpened){
			new PushButton("Cancel").click();
		}
		new PushButton("Cancel").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		if(!shellIsOpened){
			fail("Shell Maven Repositories was not opened after clicking on 'here' link");
		}

	}
	
	
	@Test
	public void testRemoteRepositoriesLinkInConversionWizard(){
		createWithRuntime();
		new DefaultLink("remote repositories").click();
		boolean shellIsOpened = true;
		try{
			new DefaultShell("Preferences (Filtered)");
		} catch (SWTLayerException ex){
			shellIsOpened = false;
		}
		if(shellIsOpened){
			new PushButton("Cancel").click();
		}
		new PushButton("Cancel").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		if(!shellIsOpened){
			fail("Shell Preferences was not opened after clicking on 'remote repositories' link");
		}

	}
	
	private void createWithRuntime(){
		createWebProject(WEB_PROJECT_NAME, runtimeName, false);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(WEB_PROJECT_NAME).select();
		new ContextMenu("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		new PushButton("Finish").click();
		new DefaultShell("Convert to Maven Dependencies");
		new WaitUntil(new WidgetIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
	}
		
	
	private void checkDependency(String projectName, Map<Object,Object> valuesToCheck){
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = docBuilder.parse(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getFile("pom.xml").getLocation().toFile());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String groupId = (String)valuesToCheck.get("groupId");

		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = null;
		try {
			nodes = (NodeList)xPath.evaluate("/project/dependencies/dependency[groupId='"+groupId+"']", doc.getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			fail("unable to find dependency with groupId "+groupId);
		}
		NodeList nList = nodes.item(0).getChildNodes();
		for(int i=0;i<nList.getLength();i++){
			String name = nList.item(i).getNodeName();
			if(!name.startsWith("#")){
				String value = nList.item(i).getTextContent();
				assertTrue(name +" value is "+ value + " but expected is "+valuesToCheck.get(name),valuesToCheck.get(name).equals(value));
			}
			
		}
		
		
	}

}

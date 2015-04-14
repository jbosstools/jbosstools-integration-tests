/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test;
/**
 * @author Rastislav Wagner
 * 
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.reddeer.eclipse.condition.ProjectExists;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectThirdPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatchers;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.jboss.tools.maven.reddeer.preferences.MavenPreferencePage;

public abstract class AbstractMavenSWTBotTest{

	public static final String USER_SETTINGS = "target/classes/settings.xml"; 
	
	@BeforeClass 
	public static void beforeClass(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenPreferencePage mpreferencesp = new MavenPreferencePage();
		preferenceDialog.select(mpreferencesp);
		mpreferencesp.updateIndexesOnStartup(false);
		preferenceDialog.ok();
		
		preferenceDialog.open();
		MavenUserPreferencePage mpreferences = new MavenUserPreferencePage();
		preferenceDialog.select(mpreferences);
		mpreferences.setUserSettings(new File(USER_SETTINGS).getAbsolutePath());
		mpreferences.ok();
		
		setGit();
	}
	
	public boolean hasNature(String projectName, String version, String... natureID){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		boolean result = new DefaultTreeItem(1,natureID).isChecked();
		if(version!=null){
			result = result && new DefaultTreeItem(1,natureID).getCell(1).equals(version);
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		return result;
	}
	
	protected void addDependency(String projectName, String groupId, String artifactId, String version){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Maven","Add Dependency").select();
		new WaitUntil(new ShellWithTextIsActive("Add Dependency"), TimePeriod.NORMAL);
		new LabeledText("Group Id:").setText(groupId);
		new LabeledText("Artifact Id:").setText(artifactId);
		new LabeledText("Version: ").setText(version);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
	}
	
	public void addPlugin(String projectName, String groupId, String artifactId, String version){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Maven","Add Plugin").select();
		new WaitUntil(new ShellWithTextIsActive("Add Plugin"), TimePeriod.NORMAL);
		new LabeledText("Group Id:").setText(groupId);
		new LabeledText("Artifact Id:").setText(artifactId);
		new LabeledText("Version: ").setText(version);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected static void updateConf(String projectName){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Maven","Update Project...").select();
		new WaitUntil(new ShellWithTextIsActive("Update Maven Project"),TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Update Maven Project"),TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
	
	public void buildProject(String projectName, String mavenBuild, String goals, boolean shouldBuild){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		RegexMatcher rm1 = new RegexMatcher("Run As");
		RegexMatcher rm2 = new RegexMatcher(mavenBuild);
		WithTextMatchers m = new WithTextMatchers(rm1,rm2);
		new ContextMenu(m.getMatchers()).select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"),TimePeriod.NORMAL);
		new LabeledText("Goals:").setText(goals);
		new PushButton("Run").click();
		ProjectIsBuilt pb = new ProjectIsBuilt();
		new WaitUntil(pb,TimePeriod.VERY_LONG);
		if(shouldBuild){
			assertTrue(pb.isBuildSuccesfull());
		}else {
			assertFalse(pb.isBuildSuccesfull());
		}
	}
	
	public static void deleteProjects(boolean fromSystem){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.deleteAllProjects(fromSystem);
	}
	
	public void checkWebTarget(String projectName, String finalName){
	    PackageExplorer pe = new PackageExplorer();
	    pe.open();
	    pe.getProject(projectName).select();
	    new ContextMenu("Refresh").select();
	    new WaitWhile(new JobIsRunning());
	    assertTrue(pe.getProject(projectName).containsItem("target",finalName+".war"));
	}
	
	public void convertToMavenProject(String projectName, String defaultPackaging, boolean withDependencies){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		new WaitUntil(new ProjectExists(projectName));
		pexplorer.getProject(projectName).select();
		new ContextMenu("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		assertEquals("Project " +projectName+" packaging should be set to "+defaultPackaging, defaultPackaging, new LabeledCombo(new DefaultGroup("Artifact"),"Packaging:").getText());
		new PushButton("Finish").click();
		try{
		    new DefaultShell("Convert to Maven Dependencies");
		    new WaitUntil(new WidgetIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
		    if(withDependencies){
		        new PushButton("Finish").click();
		    } else {
		        new PushButton("Skip Dependency Conversion").click();
		    }
	        new WaitWhile(new ShellWithTextIsAvailable("Convert to Maven Dependencies"));
		} catch (SWTLayerException ex){
		    
		} finally {
 		    new WaitWhile(new ShellWithTextIsAvailable("Create new POM"));
	        new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		}
	}
	
	//TODO editor is missing in Reddeer...and check the packaging
	public void checkPackaging(String projectName, String packaging){
		PackageExplorer pExplorer = new PackageExplorer();
		pExplorer.open();
		//new ViewTreeItem(projectName,"pom.xml").select();
		//new ContextMenu("Open").select();
	}
	

	public void createWebProject(String name,String runtime, boolean webxml){
		WebProjectWizard dw = new WebProjectWizard();
		dw.open();
		WebProjectFirstPage dfp = (WebProjectFirstPage)dw.getWizardPage(0);
		dfp.setProjectName(name);
		if(runtime == null){
			dfp.setTargetRuntime("<None>");
		} else {
			dfp.setTargetRuntime(runtime);
		}
		WebProjectThirdPage dtp = (WebProjectThirdPage)dw.getWizardPage(2);
		dtp.setGenerateWebXmlDeploymentDescriptor(webxml);
		dw.finish();
	}
	
	public static void setGit(){
		new ShellMenu("Window","Preferences").select();
		new WaitUntil(new ShellWithTextIsActive("Preferences"),TimePeriod.NORMAL);
		new DefaultTreeItem("Team","Git","Label Decorations").select();
		new DefaultTabItem("Text Decorations").activate();
		if(!new LabeledText("Projects:").getText().equals("{name}")){
			new LabeledText("Projects:").setText("{name}");
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Preferences"),TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		
	}
	
	public static void enableSnapshots(String repositoryID){
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
			doc = docBuilder.parse(USER_SETTINGS);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = null;
		try {
			//nodes = (NodeList)xPath.evaluate("/settings/profiles/profile/id", doc.getDocumentElement(), XPathConstants.NODESET);
			nodes = (NodeList)xPath.evaluate("/settings/profiles/profile[id='"+repositoryID+"']/repositories/repository/snapshots/enabled", doc.getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nodes.item(0).setTextContent("true");
		
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Result output = new StreamResult(new File(USER_SETTINGS));
		Source input = new DOMSource(doc);

		try {
			transformer.transform(input, output);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
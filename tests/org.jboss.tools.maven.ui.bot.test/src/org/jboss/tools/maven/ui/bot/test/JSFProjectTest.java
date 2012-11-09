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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class JSFProjectTest extends AbstractMavenSWTBotTest{
	public static final String JBOSS7_AS_HOME=System.getProperty("jbosstools.test.jboss.home.7.1");
	public static final String POM_FILE = "pom.xml";
	public static final String PROJECT_NAME7="JSFProject7";
	public static final String PROJECT_NAME7_v1="JSFProject7_1.2";
	public static final String SERVER_RUNTIME7="JBoss 7.1 Runtime";
	public static final String SERVER7="JBoss AS 7.1";
	public static final String GROUPID ="javax.faces";
	public static final String ARTIFACTID ="jsf-api";
	public static final String JSF_VERSION_1_1_02 ="1.1.02";
	public static final String JSF_VERSION_1_2 ="2.0";
	public static final String JSF_VERSION_2 ="2.0";

	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	
	@BeforeClass
	public final static void beforeClass(){
		setPerspective("Web Development");
	}
	
	@Test
	public void createJSFProjectTest_AS7_JSFv2() throws InterruptedException, CoreException, ParserConfigurationException, SAXException, IOException, TransformerException{
		createJSFProject(SERVER_RUNTIME7, SERVER7, JBOSS7_AS_HOME,"JSF 2.0", PROJECT_NAME7);
		activateMavenFacet(PROJECT_NAME7);
		addDependencies(PROJECT_NAME7, JSF_VERSION_2);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME7);
		assertNoErrors(project);
		buildProject(PROJECT_NAME7);
	}
	
	@Test
	public void createJSFProjectTest_AS7_JSFv1() throws InterruptedException, CoreException, ParserConfigurationException, SAXException, IOException, TransformerException{
		createJSFProject(SERVER_RUNTIME7, SERVER7, JBOSS7_AS_HOME,"JSF 1.2", PROJECT_NAME7_v1);
		activateMavenFacet(PROJECT_NAME7_v1);
		addDependencies(PROJECT_NAME7_v1, JSF_VERSION_1_2);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME7_v1);
		assertNoErrors(project);
		buildProject(PROJECT_NAME7_v1);
	}
	
	
	private void createJSFProject(String serverRuntime, String server, String serverHome, String jsfVersion, String projectName) throws InterruptedException, CoreException{
		new ShellMenu("File","New","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new DefaultTreeItem("JBoss Tools Web","JSF","JSF Project").select();
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("New JSF Project"),TimePeriod.NORMAL);
		new LabeledText("Project Name*").setText(projectName);
		new DefaultCombo(0).setSelection(jsfVersion);
		new DefaultCombo(1).setSelection("JSFKickStartWithoutLibs");
		new PushButton("Next >").click();
		new PushButton("New...").click();
		new WaitUntil(new ShellWithTextIsActive("New Server Runtime"),TimePeriod.NORMAL);
		new DefaultTreeItem("JBoss Community",serverRuntime).select();
		new PushButton("Next >").click();
		new LabeledText("Home Directory").setText(serverHome);
		new PushButton("Finish").click();
		new WaitUntil(new ShellWithTextIsActive("New JSF Project"),TimePeriod.NORMAL);
		new PushButton(1).click();
		new WaitUntil(new ShellWithTextIsActive("New Server"),TimePeriod.NORMAL);
		new DefaultTreeItem("JBoss Community",server).select();
		new PushButton("Finish").click();
		new WaitUntil(new ShellWithTextIsActive("New JSF Project"),TimePeriod.NORMAL);
		new PushButton("Finish").click();
		botUtil.waitForAll(Long.MAX_VALUE);
	}
	
	
	private void addDependencies(String projectName, String jsfVersion) throws ParserConfigurationException, SAXException, IOException, CoreException, TransformerException, InterruptedException{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = factory.newDocumentBuilder();
	    Document docPom = docBuilder.parse(project.getFile("pom.xml").getContents());
	    Element dependenciesElement = docPom.createElement("dependencies");
	    Element dependencyElement = docPom.createElement("dependency");    
	    Element groupIdElement = docPom.createElement("groupId");  
	    Element artifactIdElement = docPom.createElement("artifactId");	    
	    Element versionElement = docPom.createElement("version");
	    groupIdElement.setTextContent(GROUPID);
	    artifactIdElement.setTextContent(ARTIFACTID);
	    versionElement.setTextContent(jsfVersion);
	    dependencyElement.appendChild(groupIdElement);
	    dependencyElement.appendChild(versionElement);
	    dependencyElement.appendChild(artifactIdElement);
	    dependenciesElement.appendChild(dependencyElement);
	    Element root = docPom.getDocumentElement();
	    root.appendChild(dependenciesElement);    
	    //save pom
	    TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		StringWriter xmlAsWriter = new StringWriter(); 
		StreamResult result = new StreamResult(xmlAsWriter);
		DOMSource source = new DOMSource(docPom);
		trans.transform(source, result);
		project.getFile("pom.xml").setContents(new ByteArrayInputStream(xmlAsWriter.toString().getBytes("UTF-8")), 0, null);
		botUtil.waitForAll(Long.MAX_VALUE);
	}
	
	private void activateMavenFacet(String projectName) throws InterruptedException, CoreException{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName),TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		new DefaultTreeItem(1, "JBoss Maven Integration").setChecked(true);
	    botUtil.waitForAll();
	    bot.hyperlink("Further configuration required...").click();
		new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"),TimePeriod.NORMAL);
	    new PushButton("OK").click();
	    new PushButton("OK").click();
		botUtil.waitForAll(Long.MAX_VALUE);
	    assertTrue(projectName+ " doesn't have maven nature", isMavenProject(projectName));
	}
	
	private void buildProject(String projectName) throws CoreException{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Run As","5 Maven build...");
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"),TimePeriod.NORMAL);
		new LabeledText("Goals:").setText("clean package");
		new PushButton("Run").click();
	    new WaitUntil(new ProjectIsBuilt(), TimePeriod.LONG);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	    project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		project.getFolder("target").refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		IFolder warFolder = project.getFolder("target/" + projectName + "-0.0.1-SNAPSHOT");
		assertTrue(warFolder +" is missing ", warFolder.exists());
		IPath webInfPath = new Path("WEB-INF");
		assertFalse(warFolder.getFolder(webInfPath.append("src")).exists());
		assertFalse(warFolder.getFolder(webInfPath.append("dev")).exists());
		assertTrue(warFolder.getFolder(webInfPath.append("lib")).exists());
	}
}
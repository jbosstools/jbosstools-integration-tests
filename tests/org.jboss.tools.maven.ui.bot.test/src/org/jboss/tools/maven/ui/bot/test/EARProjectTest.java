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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Rastislav Wagner
 * 
 */
public class EARProjectTest extends AbstractMavenSWTBotTest{
	
	public static final String WAR_PROJECT_NAME="earWeb";
	public static final String EJB_PROJECT_NAME="earEJB";
	public static final String EAR_PROJECT_NAME="ear";

	private SWTUtilExt botUtil= new SWTUtilExt(bot);

	@BeforeClass
	public final static void beforeClass(){
		setPerspective("Java EE");
	}
	
	
	@Test
	public void createEARProject() throws Exception{
		createWarProject(WAR_PROJECT_NAME);
		createEJBProject(EJB_PROJECT_NAME);
		
		new ShellMenu("File","New","Enterprise Application Project").select();
		new LabeledText("Project name:").setText(EAR_PROJECT_NAME);
		new PushButton("Modify...").click();
		new ShellTreeItem("JBoss Maven Integration").setChecked(true);
		new PushButton("OK").click();
		new PushButton("Next >").click();
		new PushButton("Select All").click();
		new PushButton("Next >").click();
		new DefaultCombo("Packaging:").setSelection("ear");
		new PushButton("Finish").click();
		botUtil.waitForAll();
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		assertTrue(pexplorer.containsProject(EAR_PROJECT_NAME));
		assertTrue("EAR project isn't maven project", isMavenProject(EAR_PROJECT_NAME));
		installProject(WAR_PROJECT_NAME);
		installProject(EJB_PROJECT_NAME);
		addDependencies(EAR_PROJECT_NAME, "org.jboss.tools", WAR_PROJECT_NAME, "0.0.1-SNAPSHOT", "war");
		addDependencies(EAR_PROJECT_NAME, "org.jboss.tools", EJB_PROJECT_NAME, "0.0.1-SNAPSHOT", "ejb");
		confEarMavenPlugn(EAR_PROJECT_NAME);
		pexplorer.open();
		pexplorer.getProject(EAR_PROJECT_NAME).select();
		new ContextMenu("Run As","3 Maven build...").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"), TimePeriod.NORMAL);
		new LabeledText("Goals:").setText("clean install");
		new PushButton("Run").click();
		new WaitUntil(new ProjectIsBuilt(), TimePeriod.NORMAL);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(EAR_PROJECT_NAME);
		project.getFolder("target").refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		IFolder earFolder = project.getFolder("target/" + EAR_PROJECT_NAME + "-0.0.1-SNAPSHOT");
		assertTrue(earFolder +" is missing ", earFolder.exists());
		assertTrue(WAR_PROJECT_NAME+ ".war is missing in ear",project.getFile("target/" +EAR_PROJECT_NAME+ "-0.0.1-SNAPSHOT/" +WAR_PROJECT_NAME+ "-0.0.1-SNAPSHOT.war").exists());
		assertTrue(EJB_PROJECT_NAME+ ".jar is missing in ear",project.getFile("target/" +EAR_PROJECT_NAME+ "-0.0.1-SNAPSHOT/" +EJB_PROJECT_NAME+ "-0.0.1-SNAPSHOT.jar").exists());
	}
	
	private void createWarProject(String projectName) throws CoreException{
		new ShellMenu("File","New","Dynamic Web Project").select();
		new LabeledText("Project name:").setText(projectName);
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new CheckBox("Generate web.xml deployment descriptor").click();
		new PushButton("Finish").click();
		botUtil.waitForAll(Long.MAX_VALUE);
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new ShellTreeItem("Project Facets").select();
		new ShellTreeItem(1,"JBoss Maven Integration").setChecked(true);
		bot.hyperlink("Further configuration required...").click();
		new PushButton("OK").click();
		new PushButton("OK").click();
		botUtil.waitForAll(Long.MAX_VALUE);
		new WaitWhile(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		assertTrue("Web project doesn't have maven nature",isMavenProject(projectName));
	}
	
	private void createEJBProject(String projectName) throws CoreException{
		new ShellMenu("File","New","EJB Project").select();
		new LabeledText("Project name:").setText(projectName);
		new PushButton("Modify...").click();
		new ShellTreeItem("JBoss Maven Integration").setChecked(true);
		new PushButton("OK").click();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new DefaultCombo("Packaging:").setSelection("ejb");
		new PushButton("Finish").click();
		botUtil.waitForAll(Long.MAX_VALUE);
		assertTrue("EJB project doesn't have maven nature", isMavenProject(projectName));
	}
	
	private void installProject(String projectName){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		assertTrue(pexplorer.containsProject(projectName));
		pexplorer.getProject(projectName).select();
		new ContextMenu("Run As","4 Maven build").select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"), TimePeriod.NORMAL);
		new LabeledText("Goals:").setText("clean install");
		new PushButton("Run").click();
		new WaitUntil(new ProjectIsBuilt(), TimePeriod.LONG);
	}
	
	private void confEarMavenPlugn(String projectName) throws ParserConfigurationException, SAXException, IOException, CoreException, TransformerException{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = factory.newDocumentBuilder();
	    Document docPom = docBuilder.parse(project.getProject().getFile("pom.xml").getContents());
	    Element configurationElement = (Element) docPom.getElementsByTagName("configuration").item(0);
	    Element modulesElement = docPom.createElement("modules");
	    Element warModuleElement = docPom.createElement("webModule");  
	    Element ejbModuleElement = docPom.createElement("ejbModule");	    
	    Element groupIdWarElement = docPom.createElement("groupId");  
	    Element artifactIdWarElement = docPom.createElement("artifactId");	
	    Element groupIdEJBElement = docPom.createElement("groupId");  
	    Element artifactIdEJBElement = docPom.createElement("artifactId");	

	    groupIdWarElement.setTextContent("org.jboss.tools");
	    groupIdEJBElement.setTextContent("org.jboss.tools");
	    artifactIdWarElement.setTextContent(WAR_PROJECT_NAME);
	    artifactIdEJBElement.setTextContent(EJB_PROJECT_NAME);
	    
	    warModuleElement.appendChild(groupIdWarElement);
	    warModuleElement.appendChild(artifactIdWarElement);
	    ejbModuleElement.appendChild(groupIdEJBElement);
	    ejbModuleElement.appendChild(artifactIdEJBElement);
	    modulesElement.appendChild(warModuleElement);
	    modulesElement.appendChild(ejbModuleElement);
	    configurationElement.appendChild(modulesElement);
	    
	    TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		StringWriter xmlAsWriter = new StringWriter(); 
		StreamResult result = new StreamResult(xmlAsWriter);
		DOMSource source = new DOMSource(docPom);
		trans.transform(source, result);
		project.getProject().getFile("pom.xml").setContents(new ByteArrayInputStream(xmlAsWriter.toString().getBytes("UTF-8")), 0, null);
	}
	
	
}
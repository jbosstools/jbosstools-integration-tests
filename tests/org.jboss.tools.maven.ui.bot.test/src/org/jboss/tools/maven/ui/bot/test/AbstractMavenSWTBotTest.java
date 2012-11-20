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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.m2e.tests.common.AbstractMavenProjectTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.ui.bot.test.dialog.MavenPreferencesDialog;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public abstract class AbstractMavenSWTBotTest extends AbstractMavenProjectTestCase {
	

	protected SWTBotExt bot = new SWTBotExt();
	
	@BeforeClass 
	public static void beforeClass() throws Exception {
		MavenPreferencesDialog mpreferences = new MavenPreferencesDialog();
		mpreferences.open();
		mpreferences.setUserSettings(new File("usersettings/settings.xml").getAbsolutePath());
		mpreferences.ok();
	}
	
	@AfterClass
	public static void afterClass(){
		PackageExplorer pexplorer = new PackageExplorer();
		for(Project p: pexplorer.getProjects()){
			p.delete(true);
		}
		SWTBotExt bot = new SWTBotExt();
		SWTUtilExt botUtil = new SWTUtilExt(bot);
		botUtil.waitForAll(Long.MAX_VALUE);
	}

	public static void setPerspective(String perspective){
		new ShellMenu("Window","Open Perspective","Other...").select();
		new DefaultTable().select(perspective);
		new PushButton("OK").click();
	}
	
	public boolean isMavenProject(String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return project.hasNature(IMavenConstants.NATURE_ID);
	}
	
	public boolean hasNature(String projectName, String natureID){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		new ShellTreeItem("Project Facets").select();
		boolean result = new ShellTreeItem(1,natureID).isChecked();
		new PushButton("OK").click();
		return result;
	}
	
	protected void addDependencies(String projectName, String groupId, String artifactId, String version, String type) throws Exception{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = factory.newDocumentBuilder();
	    Document docPom = docBuilder.parse(project.getProject().getFile("pom.xml").getContents());
	    Element dependenciesElement = null;
	    if(docPom.getElementsByTagName("dependencies").item(0)==null){
	    	dependenciesElement = docPom.createElement("dependencies");
	    } else {
	    	dependenciesElement = (Element) docPom.getElementsByTagName("dependencies").item(0);
	    }
	    Element dependencyElement = docPom.createElement("dependency");
	    Element groupIdElement = docPom.createElement("groupId");  
	    Element artifactIdElement = docPom.createElement("artifactId");	    
	    Element versionElement = docPom.createElement("version");
	    Element typeElement = docPom.createElement("type");
	    
	    groupIdElement.setTextContent(groupId);
	    artifactIdElement.setTextContent(artifactId);
	    versionElement.setTextContent(version);
	    
	    Element root = docPom.getDocumentElement();
	    dependencyElement.appendChild(groupIdElement);
	    dependencyElement.appendChild(artifactIdElement);
	    dependencyElement.appendChild(versionElement);
	    if(type!=null){
	    	typeElement.setTextContent(type);
	    	dependencyElement.appendChild(typeElement);
	    }
	    dependenciesElement.appendChild(dependencyElement);
	    root.appendChild(dependenciesElement);
	    TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		StringWriter xmlAsWriter = new StringWriter(); 
		StreamResult result = new StreamResult(xmlAsWriter);
		DOMSource source = new DOMSource(docPom);
		trans.transform(source, result);
		project.getProject().getFile("pom.xml").setContents(new ByteArrayInputStream(xmlAsWriter.toString().getBytes("UTF-8")), 0, null);
	}
	
	protected void updateConf(SWTUtilExt botUtil, String projectName){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Maven","Update Project...").select();
		new PushButton("OK").click();
		botUtil.waitForAll(Long.MAX_VALUE);
	}
	
	public void buildProject(String projectName, String mavenBuild, String packaging, String version) throws Exception {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Run As",mavenBuild).select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"),TimePeriod.NORMAL);
		new LabeledText("Goals:").setText("clean package");
		new PushButton("Run").click();
		new WaitUntil(new ProjectIsBuilt(),TimePeriod.NORMAL);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		project.getFolder("target").refreshLocal(IResource.DEPTH_INFINITE,new NullProgressMonitor());
		IFile jarFile = project.getFile("target/" + projectName + version+"."+packaging);
		assertTrue(jarFile + " is missing ", jarFile.exists());
	}
	
	public void deleteProjects(boolean fromSystem){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		for(Project p: pexplorer.getProjects()){
			p.delete(fromSystem);
		}
		SWTUtilExt botUtil = new SWTUtilExt(bot);
		botUtil.waitForAll(Long.MAX_VALUE);
	}
}
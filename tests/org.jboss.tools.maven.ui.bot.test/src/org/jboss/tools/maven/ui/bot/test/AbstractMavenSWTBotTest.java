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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewRuntimeWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewRuntimeWizardPage;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.dialog.ASRuntimePage;
import org.jboss.tools.maven.ui.bot.test.dialog.DynamicWebProjectFirstPage;
import org.jboss.tools.maven.ui.bot.test.dialog.DynamicWebProjectThirdPage;
import org.jboss.tools.maven.ui.bot.test.dialog.DynamicWebProjectWizard;
import org.jboss.tools.maven.ui.bot.test.dialog.maven.MavenPreferencesPage;
import org.jboss.tools.maven.ui.bot.test.dialog.maven.MavenUserPreferencesDialog;
import org.jboss.tools.maven.ui.bot.test.utils.ButtonIsEnabled;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsNotBuilt;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.jboss.reddeer.eclipse.wst.server.ui.Runtime;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public abstract class AbstractMavenSWTBotTest{
	
	public static final String JBOSS_AS_7_1 = System.getProperty("jbosstools.test.jboss.home.7.1");
	public static String serverName;
	public static String runtimeName;
	public static final String USER_SETTINGS = "target/classes/settings.xml"; 
	
	@BeforeClass 
	public static void beforeClass(){
		// close Welcome screen
		try {
			SWTBotView activeView = Bot.get().activeView();
			if (activeView != null && activeView.getTitle().equals("Welcome")){
			    activeView.close();  
			}
		} catch (WidgetNotFoundException exc) {
			// welcome screen not found, no need to close it
		}
		
		
		MavenPreferencesPage mpreferencesp = new MavenPreferencesPage();
		mpreferencesp.open();
		mpreferencesp.updateIndexesOnStartup(false);
		mpreferencesp.ok();
		
		MavenUserPreferencesDialog mpreferences = new MavenUserPreferencesDialog();
		mpreferences.open();
		mpreferences.setUserSettings(new File(USER_SETTINGS).getAbsolutePath());
		mpreferences.ok();
		
		setGit();
		runtimeName = createASRuntime();
		serverName = createASServer();
	}
	
	@AfterClass
	public static void afterClass(){
		deleteProjects(true,true);
	}
	
	private static String createASRuntime(){
		RuntimePreferencePage rp = new RuntimePreferencePage();
		rp.open();
		for(Runtime runtime: rp.getServerRuntimes()){
			if(runtime.getType().equals("JBoss 7.1 Runtime")){
				rp.ok();
				return runtime.getName();
			}
		}
		NewRuntimeWizardDialog rd = rp.addRuntime();
		rd.addWizardPage(new ASRuntimePage(), 1);
		((NewRuntimeWizardPage)rd.getFirstPage()).selectType("JBoss Community","JBoss 7.1 Runtime");
		rd.selectPage(1);
		ASRuntimePage as = (ASRuntimePage)rd.getWizardPage();
		as.setHomeDirectory(JBOSS_AS_7_1);
		String name = as.getName();
		rd.finish();
		rp.ok();
		return name;
	}
	
	private static String createASServer(){
		ServersView sw = new ServersView();
		sw.open();
		for(Server server: sw.getServers()){
			if(server.getLabel().getName().equals("AS7.1")){
				return "AS7.1";
			}
		}
		NewServerWizardDialog ns = (NewServerWizardDialog)sw.newServer();
		NewServerWizardPage sp = ns.getFirstPage();
		sp.selectType("JBoss Community","JBoss AS 7.1");
		String name = "AS7.1";
		sp.setName("AS7.1");
		ns.finish();
		return name;
		
	}

	public static void setPerspective(String perspective){
		new ShellMenu("Window","Open Perspective","Other...").select();
		try{
	        // Try to select perspective label within available perspectives
			new DefaultTable().select(perspective);
	      } catch (IllegalArgumentException iae){
	        // Try to select perspective label within available perspectives with "(default)" suffix
	    	  new DefaultTable().select(perspective + " (default)");
	      }
		new PushButton("OK").click();
	}
	
	public boolean isMavenProject(String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return project.hasNature(IMavenConstants.NATURE_ID);
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
		RegexMatchers m = new RegexMatchers("Run As",mavenBuild);
		new ContextMenu(m.getMatchers()).select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"),TimePeriod.NORMAL);
		new LabeledText("Goals:").setText(goals);
		new PushButton("Run").click();
		if(shouldBuild){
			new WaitUntil(new ProjectIsBuilt(),TimePeriod.LONG);
		}else {
			new WaitUntil(new ProjectIsNotBuilt(),TimePeriod.LONG);
		}
	}
	
	public static void deleteProjects(boolean fromSystem, boolean update){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		List<Project> projects = pexplorer.getProjects();
		for(Project p: projects){
			if(update){
				updateConf(p.getName());
			}
			p.select();
			new ContextMenu("Refresh").select();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			p.delete(fromSystem);
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue("Not all projects have been deleted", pexplorer.getProjects().isEmpty());
	}
	
	public void checkWebTarget(String projectName, String finalName) throws CoreException{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		project.getFolder("target").refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		IFolder projectFolder = project.getFolder("target/" + finalName);
		assertTrue(projectFolder +" is missing ", projectFolder.exists());
		IPath webInfPath = new Path("WEB-INF");
		assertFalse(projectFolder.getFolder(webInfPath.append("src")).exists());
		assertFalse(projectFolder.getFolder(webInfPath.append("dev")).exists());
		assertTrue(projectFolder.getFolder(webInfPath.append("lib")).exists());
		
		IFile jarFile = project.getFolder("target").getFile(finalName+".war");
		assertTrue("war file of "+projectName+" is missing ", jarFile.exists());
	}
	
	public void checkJarTarget(String projectName, String finalName) throws CoreException{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		project.getFolder("target").refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		IFolder projectFolder = project.getFolder("target/classes");
		assertTrue(projectFolder +" is missing ", projectFolder.exists());
		
		projectFolder = project.getFolder("target/test-classes");
		assertTrue(projectFolder +" is missing ", projectFolder.exists());
		
		IFile jarFile = project.getFolder("target").getFile(finalName+".jar");
		assertTrue("jar file of "+projectName+" is missing ", jarFile.exists());
	}
	
	public void activateMavenFacet(String projectName) throws CoreException{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName),TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		new DefaultTreeItem(1, "JBoss Maven Integration").setChecked(true);
		SWTFormsBot x =new SWTFormsBot();
		x.hyperlink("Further configuration required...").click();
		try{
		new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"),TimePeriod.NORMAL);
		}catch(TimeoutException ex){
			SWTFormsBot xs =new SWTFormsBot();
			xs.hyperlink("Further configuration required...").click();
			new WaitUntil(new ShellWithTextIsActive("Modify Faceted Project"),TimePeriod.NORMAL);
		}
	    new PushButton("OK").click();
	    new PushButton("OK").click();
	    new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	    assertTrue(projectName+ " doesn't have maven nature", isMavenProject(projectName));
	}
	
	public void convertToMavenProject(String projectName, String defaultPackaging, boolean withDependencies){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Configure","Convert to Maven Project").select();
		new WaitUntil(new ShellWithTextIsActive("Create new POM"),TimePeriod.NORMAL);
		//assertTrue("Project " +projectName+" packaging should be set to "+defaultPackaging,new DefaultCombo("Artifact","Packaging:").getText().equals(defaultPackaging));
		new PushButton("Finish").click();
		if(withDependencies){
			new WaitUntil(new ShellWithTextIsActive("Convert to Maven Dependencies"),TimePeriod.NORMAL);
			new WaitUntil(new ButtonIsEnabled("Finish"), TimePeriod.LONG);
			new PushButton("Finish").click();
		} else {
			new WaitWhile(new ShellWithTextIsActive("Create new POM"),TimePeriod.NORMAL);
		}
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	public void assertNoErrors(String projectName) throws CoreException{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		//super.assertNoErrors(project);
	}
	//TODO editor is missing in Reddeer...and check the packaging
	public void checkPackaging(String projectName, String packaging){
		PackageExplorer pExplorer = new PackageExplorer();
		pExplorer.open();
		//new ViewTreeItem(projectName,"pom.xml").select();
		//new ContextMenu("Open").select();
	}
	

	public void createWebProject(String name,String runtime, boolean webxml){
		DynamicWebProjectWizard dw = new DynamicWebProjectWizard();
		dw.open();
		dw.selectPage(1);
		DynamicWebProjectFirstPage dfp = (DynamicWebProjectFirstPage)dw.getWizardPage();
		dfp.setProjectName(name);
		if(runtime == null){
			dfp.setRuntime("<None>");
		} else {
			dfp.setRuntime(runtime);
		}
		dw.selectPage(3);
		DynamicWebProjectThirdPage dtp = (DynamicWebProjectThirdPage)dw.getWizardPage();
		dtp.generateWebXml(webxml);
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
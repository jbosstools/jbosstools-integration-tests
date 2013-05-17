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
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.dialog.DynamicWebProjectFirstPage;
import org.jboss.tools.maven.ui.bot.test.dialog.DynamicWebProjectThirdPage;
import org.jboss.tools.maven.ui.bot.test.dialog.DynamicWebProjectWizard;
import org.jboss.tools.maven.ui.bot.test.dialog.EJBProjectDialog;
import org.jboss.tools.maven.ui.bot.test.dialog.EJBProjectFirstPage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
 * @author Rastislav Wagner
 * 
 */
public abstract class AbstractConfiguratorsTest extends AbstractMavenSWTBotTest{
	
	public static final String PROJECT_NAME_JSF="testWEB_JSF";
	public static final String PROJECT_NAME_JAXRS="testWEB_JAXRS";
	public static final String PROJECT_NAME_CDI="testWEB_CDI";
	public static final String PROJECT_NAME_CDI_EJB="testEJB_CDI";
	public static final String PROJECT_NAME_SEAM="testWEB_SEAM";
	public static final String PROJECT_NAME_JPA="testWEB_JPA";
	public static final String JSF_NATURE="JavaServer Faces";//"org.jboss.tools.jsf.jsfnature";
	public static final String JAXRS_NATURE="JAX-RS (REST Web Services)";//"org.jboss.tools.ws.jaxrs.nature";
	public static final String CDI_NATURE="CDI (Contexts and Dependency Injection)";//"org.jboss.tools.cdi.core.cdinature";
	public static final String SEAM_NATURE="Seam";//"org.jboss.tools.seam.core.seamnature";
	public static final String JPA_NATURE="JPA";//"org.hibernate.eclipse.console.hibernateNature";
	
	public static final String WEB_XML_LOCATION="/WebContent/WEB-INF/web.xml";
	public static final String JBOSS7_AS_HOME=System.getProperty("jbosstools.test.jboss.home.7.1");
//jpa config, gwt, hibernate
	

	
	public void addPersistence(String projectName) throws ParserConfigurationException, TransformerException, UnsupportedEncodingException, CoreException{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("New","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new DefaultTreeItem("XML","XML File").select();
		new PushButton("Next >").click();
		new LabeledText("Enter or select the parent folder:").setText(projectName+"/src/META-INF");
		new LabeledText("File name:").setText("persistence.xml");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New XML File"),TimePeriod.NORMAL);
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = factory.newDocumentBuilder();
	    Document docPer = docBuilder.newDocument();
	    
	    Element persistenceElement = docPer.createElement("persistence");
	    persistenceElement.setAttribute("version","2.0");
	    persistenceElement.setAttribute("xmlns", "http://java.sun.com/xml/ns/persistence");
	    persistenceElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
	    persistenceElement.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd");
	    
	    
	    Element persistenceUnitElement = docPer.createElement("persistence-unit");
	    persistenceUnitElement.setAttribute("name","primary");
	    persistenceUnitElement.setAttribute("transaction-type","JTA");
	    
	    persistenceElement.appendChild(persistenceUnitElement);
	    docPer.appendChild(persistenceElement);
	    TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		StringWriter xmlAsWriter = new StringWriter(); 
		StreamResult result = new StreamResult(xmlAsWriter);
		DOMSource source = new DOMSource(docPer);
		trans.transform(source, result);
		project.getProject().getFolder("src").getFolder("META-INF").getFile("persistence.xml").setContents(new ByteArrayInputStream(xmlAsWriter.toString().getBytes("UTF-8")), 0, null);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
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
	
	public void checkProjectWithoutRuntime(String projectName) throws CoreException{
		assertTrue(projectName+ " doesn't have maven nature",isMavenProject(projectName));
		updateConf(projectName);
		assertFalse("Project "+projectName+" has "+CDI_NATURE+" nature.",hasNature(projectName, CDI_NATURE,null));
		assertFalse("Project "+projectName+" has "+JSF_NATURE+" nature.",hasNature(projectName, JSF_NATURE,null));
		assertFalse("Project "+projectName+" has "+JAXRS_NATURE+" nature.",hasNature(projectName, JAXRS_NATURE,null));
	}
	
	public void checkProjectWithRuntime(String projectName) throws CoreException{
		assertTrue(projectName+ " doesn't have maven nature",isMavenProject(projectName));
		updateConf(projectName);
		assertTrue("Project "+projectName+" has "+CDI_NATURE+" nature.",hasNature(projectName, CDI_NATURE,null));
		assertTrue("Project "+projectName+" doesn't have "+JSF_NATURE+" nature.",hasNature(projectName, JSF_NATURE,null));
		assertTrue("Project "+projectName+" doesn't have "+JAXRS_NATURE+" nature.",hasNature(projectName, JAXRS_NATURE,null));
	}
	
	public void createEJBProject(String name, String runtime) throws CoreException{
		EJBProjectDialog ejb = new EJBProjectDialog();
		ejb.open();
		ejb.selectPage(1);
		EJBProjectFirstPage efp = (EJBProjectFirstPage)ejb.getWizardPage();
		efp.setProjectName(name);
		if(runtime == null){
			efp.setRuntime("<None>");
		} else {
			efp.setRuntime(runtime);
		}
		ejb.finish();
	}
	
	public void addFacesConf(String projectName) throws InterruptedException{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("New","Other...").select();
		new DefaultTreeItem("JBoss Tools Web","JSF","Faces Config").select();
		new PushButton("Next >").click();
		new PushButton("Browse...").click();
		new DefaultTreeItem(projectName,"WebContent","WEB-INF").select();
		new PushButton("OK").click();
		new PushButton("Finish").click();
		updateConf(projectName);
	}
	
	public void addServlet(String projectName, String servletName, String servletClass, String load) throws ParserConfigurationException, SAXException, IOException, CoreException, TransformerException{
		IProject facade = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = factory.newDocumentBuilder();
	    Document docPom = docBuilder.parse(facade.getProject().getFile(WEB_XML_LOCATION).getContents());
	    Element servletElement = docPom.createElement("servlet");
	    Element servletNameElement = docPom.createElement("servlet-name");  
	    Element servletClassElement = docPom.createElement("servlet-class");	    
	    Element loadElement = docPom.createElement("load-on-startup");
	    
	    servletNameElement.setTextContent(servletName);
	    servletClassElement.setTextContent(servletClass);
	    loadElement.setTextContent(load);
	    
	    Element root = docPom.getDocumentElement();
	    servletElement.appendChild(servletNameElement);
	    servletElement.appendChild(servletClassElement);
	    servletElement.appendChild(loadElement);
	    root.appendChild(servletElement);
	    TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		StringWriter xmlAsWriter = new StringWriter(); 
		StreamResult result = new StreamResult(xmlAsWriter);
		DOMSource source = new DOMSource(docPom);
		trans.transform(source, result);
		facade.getProject().getFile(WEB_XML_LOCATION).setContents(new ByteArrayInputStream(xmlAsWriter.toString().getBytes("UTF-8")), 0, null);
		new WaitWhile(new JobIsRunning());
		updateConf(projectName);	
	}
}
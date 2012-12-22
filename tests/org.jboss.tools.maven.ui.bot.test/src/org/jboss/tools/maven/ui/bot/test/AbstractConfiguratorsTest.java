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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
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
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
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
	
	private SWTUtilExt botUtil= new SWTUtilExt(bot);

	
	public void addPersistence(String projectName) throws ParserConfigurationException, SAXException, IOException, CoreException, TransformerException, InterruptedException {
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("New","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new ShellTreeItem("XML","XML File").select();
		new PushButton("Next >").click();
		new LabeledText("Enter or select the parent folder:").setText(projectName+"/src/META-INF");
		new LabeledText("File name:").setText("persistence.xml");
		new PushButton("Finish").click();
		botUtil.waitForAll(Long.MAX_VALUE);
		Thread.sleep(1000);
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
		botUtil.waitForAll(Long.MAX_VALUE);
	}

	public void createMavenizedDynamicWebProject(String projectName, boolean runtime) throws Exception{
		new ShellMenu("File","New","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new ShellTreeItem("Web","Dynamic Web Project").select();
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("New Dynamic Web Project"),TimePeriod.NORMAL);;
		new LabeledText("Project name:").setText(projectName);

		if(runtime){
			new PushButton("New Runtime...").click();
			new WaitUntil(new ShellWithTextIsActive("New Server Runtime Environment"),TimePeriod.NORMAL);
			new ShellTreeItem("JBoss Community","JBoss 7.1 Runtime").select();
			new PushButton("Next >").click();
			new LabeledText("Home Directory").setText(JBOSS7_AS_HOME);
			new PushButton("Finish").click();
		} else {
			new DefaultCombo("Target runtime",0).setSelection("<None>");
		}
		new WaitUntil(new ShellWithTextIsActive("New Dynamic Web Project"),TimePeriod.NORMAL);
		new PushButton("Next >").click();
		new PushButton("Next >").click();
		new CheckBox("Generate web.xml deployment descriptor").toggle(true);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Dynamic Web Project"),TimePeriod.LONG);
		botUtil.waitForAll(Long.MAX_VALUE);
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName),TimePeriod.NORMAL);
		new ShellTreeItem("Project Facets").select();
		new ShellTreeItem(1,"JBoss Maven Integration").setChecked(true);
	    botUtil.waitForAll();
	    bot.hyperlink("Further configuration required...").click();
		new PushButton("OK").click();
		new PushButton("OK").click();
	    botUtil.waitForAll();
		assertTrue(projectName+ " doesn't have maven nature",isMavenProject(projectName));
		updateConf(botUtil,projectName);
		assertFalse("Project "+projectName+" has "+CDI_NATURE+" nature.",hasNature(projectName, CDI_NATURE)); //false always
		if(runtime){
			assertTrue("Project "+projectName+" doesn't have "+JSF_NATURE+" nature.",hasNature(projectName, JSF_NATURE));
			assertTrue("Project "+projectName+" doesn't have "+JAXRS_NATURE+" nature.",hasNature(projectName, JAXRS_NATURE));
		} else {
			assertFalse("Project "+projectName+" has "+JSF_NATURE+" nature.",hasNature(projectName, JSF_NATURE));
			assertFalse("Project "+projectName+" has "+JAXRS_NATURE+" nature.",hasNature(projectName, JAXRS_NATURE));
		}
	}
	
	public void createMavenizedEJBProject(String projectName, boolean runtime)throws Exception{
		new ShellMenu("File","New","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new ShellTreeItem("EJB","EJB Project").select();
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("New EJB Project"),TimePeriod.NORMAL);;
		new LabeledText("Project name:").setText(projectName);

		if(runtime){
			new PushButton("New Runtime...").click();
			new WaitUntil(new ShellWithTextIsActive("New Server Runtime Environment"),TimePeriod.NORMAL);
			new ShellTreeItem("JBoss Community","JBoss 7.1 Runtime").select();
			new PushButton("Next >").click();
			new LabeledText("Home Directory").setText(JBOSS7_AS_HOME);
			new PushButton("Finish").click();
		} else {
			new DefaultCombo("Target runtime",0).setSelection("<None>");
		}
		new WaitUntil(new ShellWithTextIsActive("New EJB Project"),TimePeriod.NORMAL);
		new PushButton("Finish").click();

		botUtil.waitForAll(Long.MAX_VALUE);

		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName),TimePeriod.NORMAL);
		new ShellTreeItem("Project Facets").select();
		new ShellTreeItem(1,"JBoss Maven Integration").setChecked(true);
	    bot.hyperlink("Further configuration required...").click();
	   	new DefaultCombo("Packaging:").setSelection("ejb");
	   	new PushButton("OK").click();
		new PushButton("OK").click();
	    botUtil.waitForAll();
		assertTrue(projectName+ " doesn't have maven nature",isMavenProject(projectName));
		updateConf(botUtil,projectName);
		assertFalse("Project "+projectName+" has "+CDI_NATURE+" nature.",hasNature(projectName, CDI_NATURE));
		
	}
	
	public void addFacesConf(String projectName) throws InterruptedException{
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("New","Other...").select();
		new ShellTreeItem("JBoss Tools Web","JSF","Faces Config").select();
		new PushButton("Next >").click();
		new PushButton("Browse...").click();
		new ShellTreeItem(projectName,"WebContent","WEB-INF").select();
		new PushButton("OK").click();
		new PushButton("Finish").click();
		updateConf(botUtil,projectName);
	}
	
	public void addServlet(String projectName, String servletName, String servletClass, String load) throws Exception{
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
		botUtil.waitForAll();
		updateConf(botUtil,projectName);	
	}
	public void clean(){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		for(Project p: pexplorer.getProjects()){
			p.delete(true);
		}
	}
}
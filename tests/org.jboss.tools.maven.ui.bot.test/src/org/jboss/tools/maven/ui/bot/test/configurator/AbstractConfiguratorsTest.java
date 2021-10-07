/*******************************************************************************
 * Copyright (c) 2011-2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test.configurator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.updateConf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.ejb.ui.project.facet.EjbProjectWizard;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.jboss.tools.maven.ui.bot.test.utils.EditorResourceHelper;
import org.junit.After;
/**
 * @author Rastislav Wagner
 * 
 */
public abstract class AbstractConfiguratorsTest extends AbstractMavenSWTBotTest{
	
	public static final String PROJECT_NAME_JSF="testWEB_JSF";
	public static final String PROJECT_NAME_JAXRS="testWEB_JAXRS";
	public static final String PROJECT_NAME_CDI="testWEB_CDI";
	public static final String PROJECT_NAME_CDI_EJB="testEJB_CDI";
	public static final String PROJECT_NAME_JPA="testWEB_JPA";
	public static final String JSF_FACET="JavaServer Faces";
	public static final String JAXRS_FACET="JAX-RS (REST Web Services)";
	public static final String CDI_FACET="CDI (Contexts and Dependency Injection)";
	public static final String JPA_FACET="JPA";
	public static final String WEB_XML_LOCATION="/WebContent/WEB-INF/web.xml"; 
	//jpa config, gwt, hibernate
	
	@After
	public void deleteProjects(){
		deleteProjects(true);
	}
	
	public void addPersistence(String projectName, String jpaVersion) throws FileNotFoundException{
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenuItem("New","Other...").select();
		new WaitUntil(new ShellIsAvailable("New"),TimePeriod.DEFAULT);
		new DefaultTreeItem("XML","XML File").select();
		new PushButton("Next >").click();
		new LabeledText("Enter or select the parent folder:").setText(projectName+"/src/META-INF");
		new LabeledText("File name:").setText("persistence.xml");
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable("New XML File"),TimePeriod.DEFAULT);
		
		new DefaultEditor("persistence.xml");
		new DefaultCTabItem("Source").activate();
		EditorResourceHelper.replaceClassContentByResource(new FileInputStream("resources/persistence_jpa_" + jpaVersion + ".xm_"), true, true);
	}
	
	public void checkProjectWithoutRuntime(String projectName){
		updateConf(projectName);
		assertFalse("Project "+projectName+" has "+CDI_FACET+" nature.",hasNature(projectName,null, CDI_FACET));
		assertFalse("Project "+projectName+" has "+JSF_FACET+" nature.",hasNature(projectName,null, JSF_FACET));
		assertFalse("Project "+projectName+" has "+JAXRS_FACET+" nature.",hasNature(projectName, null, JAXRS_FACET));
	}
	
	public void checkProjectWithRuntime(String projectName){
		updateConf(projectName);
		assertTrue("Project "+projectName+" doesn't have "+JSF_FACET+" nature.",hasNature(projectName,null, JSF_FACET));
		assertTrue("Project "+projectName+" doesn't have "+JAXRS_FACET+" nature.",hasNature(projectName,null, JAXRS_FACET));
	}
	
	public void createEJBProject(String name, String runtime) {
	    EjbProjectWizard ejb = new EjbProjectWizard();
		ejb.open();
		EjbProjectFirstPage efp = new EjbProjectFirstPage(ejb);
		efp.setProjectName(name);
		if(runtime == null){
			efp.setTargetRuntime("<None>");
		} else {
			efp.setTargetRuntime(runtime);
		}
		ejb.finish(TimePeriod.VERY_LONG);
	}
	
	public void addFacesConf(String projectName){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenuItem("New","Other...").select();
		new DefaultTreeItem("JBoss Tools Web","JSF","Faces Config").select();
		new PushButton("Next >").click();
		new PushButton("Browse...").click();
		new DefaultTreeItem(projectName,"WebContent","WEB-INF").select();
		new PushButton("OK").click();
		new PushButton("Finish").click();
		updateConf(projectName);
	}
	
	
	public void addServlet(String projectName, String servletName, String servletClass, String load){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem("WebContent","WEB-INF","web.xml").open();
		DefaultEditor de = new DefaultEditor("web.xml");
		new DefaultCTabItem("Source").activate();
		DefaultStyledText dt = new DefaultStyledText();
		dt.insertText(2, 0, "<servlet> <servlet-name>"+servletName+"</servlet-name><servlet-class>"+servletClass+"</servlet-class><load-on-startup>"+load+"</load-on-startup> </servlet>");
		de.save();
		updateConf(projectName);	
	}
	
}
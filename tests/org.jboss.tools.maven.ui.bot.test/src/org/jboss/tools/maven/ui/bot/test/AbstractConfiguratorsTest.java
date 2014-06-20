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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

//import org.jboss.reddeer.direct.platform.JobManager;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
//import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.maven.ui.bot.test.dialog.EJBProjectDialog;
import org.jboss.tools.maven.ui.bot.test.dialog.EJBProjectFirstPage;
import org.jboss.tools.maven.ui.bot.test.utils.EditorResourceHelper;
import org.junit.After;
/**
 * @author Rastislav Wagner
 * 
 */
public abstract class AbstractConfiguratorsTest extends AbstractMavenSWTBotTest{
	
	public static final String PROJECT_NAME_JSF="testWEB_JSF";
	public static final String PROJECT_NAME_PORTLET="testWEB_PORTLET";
	public static final String PROJECT_NAME_JAXRS="testWEB_JAXRS";
	public static final String PROJECT_NAME_CDI="testWEB_CDI";
	public static final String PROJECT_NAME_CDI_EJB="testEJB_CDI";
	public static final String PROJECT_NAME_SEAM="testWEB_SEAM";
	public static final String PROJECT_NAME_JPA="testWEB_JPA";
	public static final String JSF_FACET="JavaServer Faces";
	public static final String JAXRS_FACET="JAX-RS (REST Web Services)";
	public static final String CDI_FACET="CDI (Contexts and Dependency Injection)";
	public static final String SEAM_FACET="Seam";
	public static final String JPA_FACET="JPA";
	public static final String PORTLET_FACET="JBoss Portlets";
	public static final String PORTLET_CORE_FACET="JBoss Core Portlet";
	public static final String PORTLET_JSF_FACET="JBoss JSF Portlet";
	public static final String PORTLET_SEAM_FACET="JBoss Seam Portlet";
	
	public static final String WEB_XML_LOCATION="/WebContent/WEB-INF/web.xml";
	public static final String JBOSS7_AS_HOME=System.getProperty("jbosstools.test.jboss.home.7.1");
//jpa config, gwt, hibernate
	
	@After
	public void deleteProjects(){
	    PackageExplorer pexplorer = new PackageExplorer();
        pexplorer.open();
        List<Project> projects = pexplorer.getProjects();
        for(Project p: projects){
            p.delete(true);
        }
        assertTrue("Not all projects have been deleted", pexplorer.getProjects().isEmpty());
        //JobManager.killAllJobs();
        //new WaitWhile(new JobIsRunning());
	}
	
	public void addPersistence(String projectName) throws FileNotFoundException{
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
		
		new DefaultEditor("persistence.xml");
		new DefaultCTabItem("Source").activate();
		EditorResourceHelper.replaceClassContentByResource(new FileInputStream("resources/persistence.xm_"), true, true);
	}
	
	public void checkProjectWithoutRuntime(String projectName){
		updateConf(projectName);
		assertFalse("Project "+projectName+" has "+CDI_FACET+" nature.",hasNature(projectName,null, CDI_FACET));
		assertFalse("Project "+projectName+" has "+JSF_FACET+" nature.",hasNature(projectName,null, JSF_FACET));
		assertFalse("Project "+projectName+" has "+JAXRS_FACET+" nature.",hasNature(projectName, null, JAXRS_FACET));
	}
	
	public void checkProjectWithRuntime(String projectName){
		updateConf(projectName);
		assertTrue("Project "+projectName+" has "+CDI_FACET+" nature.",hasNature(projectName,null, CDI_FACET));
		assertTrue("Project "+projectName+" doesn't have "+JSF_FACET+" nature.",hasNature(projectName,null, JSF_FACET));
		assertTrue("Project "+projectName+" doesn't have "+JAXRS_FACET+" nature.",hasNature(projectName,null, JAXRS_FACET));
	}
	
	public void createEJBProject(String name, String runtime) {
		EJBProjectDialog ejb = new EJBProjectDialog();
		ejb.open();
		EJBProjectFirstPage efp = (EJBProjectFirstPage)ejb.getWizardPage(0);
		efp.setProjectName(name);
		if(runtime == null){
			efp.setTargetRuntime("<None>");
		} else {
			efp.setTargetRuntime(runtime);
		}
		ejb.finish();
	}
	
	public void addFacesConf(String projectName){
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
	
	
	public void addServlet(String projectName, String servletName, String servletClass, String load){
		PackageExplorer pe = new PackageExplorer();
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
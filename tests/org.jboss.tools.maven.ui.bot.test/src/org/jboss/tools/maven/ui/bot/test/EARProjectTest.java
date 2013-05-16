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

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
public class EARProjectTest extends AbstractMavenSWTBotTest{
	
	public static final String WAR_PROJECT_NAME="earWeb";
	public static final String EJB_PROJECT_NAME="earEJB";
	public static final String EAR_PROJECT_NAME="ear";

	@Before
	public void before(){
		setPerspective("Java EE");
	}
	
	@Test
	public void testEARProject() throws InterruptedException{
		createEARProject();		
		prepareProject(EAR_PROJECT_NAME+"EJB", "5 Maven build...","ejb");
		prepareProject(EAR_PROJECT_NAME+"Connector", "5 Maven build...","rar");
		prepareProject(EAR_PROJECT_NAME+"Client", "5 Maven build...","app-client");
		prepareProject(EAR_PROJECT_NAME+"Web","5 Maven build...","war");
		convertToMavenProject(EAR_PROJECT_NAME, "ear",true);
		//shouldnt this be added during conversion? JBIDE-13781 + <extensions>true</extensions> otherwise fails
		addPlugin(EAR_PROJECT_NAME, "org.apache.maven.plugins", "maven-acr-plugin", "1.0");
		buildProject(EAR_PROJECT_NAME, "3 Maven build...", "clean package",true);
		ProjectItem targetFiles = getTargetFiles(EAR_PROJECT_NAME);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"EJB-0.0.1-SNAPSHOT.jar") != null);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"Client-0.0.1-SNAPSHOT.jar") != null);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"Connector-0.0.1-SNAPSHOT.rar") != null);
		assertTrue(targetFiles.getChild(EAR_PROJECT_NAME+"Web-0.0.1-SNAPSHOT.war") != null);
		
	}
	
	public ProjectItem getTargetFiles(String projectName){
		PackageExplorer pex = new PackageExplorer();
		pex.open();
		pex.getProject(projectName).select();
		new ContextMenu("Refresh").select();
		return pex.getProject(projectName).getProjectItem("target",projectName+"-0.0.1-SNAPSHOT");
	}
	
	public void prepareProject(String projectName, String mavenBuild,String packaging){
		convertToMavenProject(projectName,packaging, false);
		buildProject(projectName, mavenBuild, "clean install",true);
		checkPackaging(projectName, packaging);
	}
	
	public void createEARProject(){
		new ShellMenu("File","New","Other...").select();
		new WaitUntil(new ShellWithTextIsActive("New"),TimePeriod.NORMAL);
		new DefaultTreeItem("Java EE","Enterprise Application Project").select();
		new PushButton("Next >").click();
		new LabeledText("Project name:").setText(EAR_PROJECT_NAME);
		new PushButton("Next >").click();
		new PushButton("New Module...").click();
		new WaitUntil(new ShellWithTextIsActive("Create default Java EE modules."),TimePeriod.NORMAL);
		new PushButton("Finish").click();
		new WaitUntil(new ShellWithTextIsActive("New EAR Application Project"),TimePeriod.NORMAL);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New EAR Application Project"),TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
}
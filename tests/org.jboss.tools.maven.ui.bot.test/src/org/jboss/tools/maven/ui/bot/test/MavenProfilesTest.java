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

import java.io.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.maven.ui.bot.test.dialog.ImportMavenProjectWizard;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
public class MavenProfilesTest extends AbstractMavenSWTBotTest {
	
	public static final String AUTOACTIVATED_PROFILE_IN_POM = "active-profile";
	public static final String[] AUTOACTIVATED_PROFILES_IN_USER_SETTINGS = {"profile.from.settings.xml", "jboss"};
	public static final String[] COMMON_PROFILES = {"common-profile"};
	public static final String[] SIMPLE_JAR_ALL_PROFILES = {"inactive-profile", "common-profile", "active-profile"};
	
	private SWTUtilExt botUtil= new SWTUtilExt(bot);
	
	@BeforeClass
	public static void setup() {
		setPerspective("Java");
	}
	
	//@Test
	public void testOpenMavenProfiles() throws IOException, InterruptedException, CoreException, ParseException{
		importMavenProject("projects/simple-jar/pom.xml");
		testAutoActivatedProfiles();
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();

		//activate all profiles
		pexplorer.getProject("simple-jar").select();
		new ContextMenu("Maven","Select Maven Profiles...").select();
		new WaitUntil(new ShellWithTextIsActive("Select Maven profiles"), TimePeriod.NORMAL);
		new PushButton("Select All").click();
		new PushButton("Activate").click();
	    new PushButton("OK").click();
	    testActivatedProfiles("simple-jar", SIMPLE_JAR_ALL_PROFILES);
	    
	    //disable all profiles
		pexplorer.getProject("simple-jar").select();
		new ContextMenu("Maven","Select Maven Profiles...").select();
		new WaitUntil(new ShellWithTextIsActive("Select Maven profiles"), TimePeriod.NORMAL);
		new PushButton("Deselect all").click();
		new PushButton("Deactivate").click();
	    new PushButton("OK").click();
	    testActivatedProfiles("simple-jar", null);
	    pexplorer.getProject("simple-jar").delete(false);
	}
	
	@Test
	public void testOpenMultipleMavenProfiles() throws IOException, InterruptedException, CoreException, ParseException{
		importMavenProject("projects/simple-jar/pom.xml");
		importMavenProject("projects/simple-jar1/pom.xml");
		importMavenProject("projects/simple-jar2/pom.xml");
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.selectProjects("simple-jar","simple-jar1","simple-jar2");
		new ContextMenu("Maven","Select Maven Profiles...").select();
		new WaitUntil(new ShellWithTextIsActive("Select Maven profiles"), TimePeriod.NORMAL);
		new PushButton("Select All").click();
		new PushButton("Activate").click();
	    new PushButton("OK").click();
	    botUtil.waitForAll();
		testActivatedProfiles("simple-jar", COMMON_PROFILES);
		testActivatedProfiles("simple-jar1", COMMON_PROFILES);
		testActivatedProfiles("simple-jar2", COMMON_PROFILES);
		pexplorer.getProject("simple-jar").delete(false);
		pexplorer.getProject("simple-jar1").delete(false);
		pexplorer.getProject("simple-jar2").delete(false);
	}
	
	
	private void testActivatedProfiles(String projectName, String[] expectedProfiles) {
		Set<String> setOfExpectedProfiles =new HashSet<String>();
		if(expectedProfiles != null){    	
	    	setOfExpectedProfiles = new HashSet<String>(Arrays.asList(expectedProfiles));
	    	for(String act: AUTOACTIVATED_PROFILES_IN_USER_SETTINGS){
	    		setOfExpectedProfiles.add(act);
	    	}
	    }
	    IMavenProjectFacade facade = MavenPlugin.getMavenProjectRegistry().getMavenProject("org.jboss.tools.maven.tests", projectName, "1.0.0-SNAPSHOT");
		assertNotNull("facade is null",facade);
		System.out.println(setOfExpectedProfiles);
		System.out.println();
		Set<String> setOfFacadeProfiles = new HashSet<String>(MavenPlugin.getProjectConfigurationManager().getResolverConfiguration(facade.getProject()).getActiveProfileList());
		System.out.println(setOfFacadeProfiles);
		for(String s: setOfExpectedProfiles){
	    	   assertTrue(s+" profile is not activated",setOfFacadeProfiles.contains(s));	
	    }
	}
	
	private void testAutoActivatedProfiles(){
		IMavenProjectFacade facade = MavenPlugin.getMavenProjectRegistry().getMavenProject("org.jboss.tools.maven.tests", "simple-jar", "1.0.0-SNAPSHOT");
	    assertNotNull("facade is null",facade);
	    assertEquals("Auto Activated profiles from pom.xml doesn't match", AUTOACTIVATED_PROFILE_IN_POM, facade.getMavenProject().getActiveProfiles().get(0).getId());
	    assertEquals("Auto Activated profiles from settings.xml doesn't match", AUTOACTIVATED_PROFILES_IN_USER_SETTINGS[0], facade.getMavenProject().getActiveProfiles().get(1).getId());
	    assertEquals("Auto Activated profiles from settings.xml doesn't match", AUTOACTIVATED_PROFILES_IN_USER_SETTINGS[1], facade.getMavenProject().getActiveProfiles().get(2).getId());
	}
	
	private void importMavenProject(String pomPath) throws IOException, InterruptedException{
		ImportMavenProjectWizard importWizard = new ImportMavenProjectWizard();
		importWizard.open();
		importWizard.importProject((new File(pomPath)).getParentFile().getCanonicalPath());
		botUtil.waitForAll();
	}
}
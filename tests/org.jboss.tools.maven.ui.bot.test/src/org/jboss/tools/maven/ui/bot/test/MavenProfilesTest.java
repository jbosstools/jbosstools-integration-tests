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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.matcher.WithRegexMatchers;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.reddeer.maven.ui.preferences.ConfiguratorPreferencePage;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.jboss.tools.maven.reddeer.profiles.SelectProfilesDialog;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
public class MavenProfilesTest extends AbstractMavenSWTBotTest {
	
	public static final String AUTOACTIVATED_PROFILE_IN_POM = "active-profile";
	public static final String AUTOACTIVATED_PROFILE_IN_SETTINGS="auto.activated.settings";
	public static final String PROFILE_IN_SETTINGS="profile.settings";
	public static final String COMMON_PROFILE = "common-profile";
	public static final String[] SIMPLE_JAR_ALL_PROFILES = {"inact-profile", "common-profile", "active-profile"};
	
	
	@BeforeClass
	public static void setup() throws IOException {
		setPerspective("Java");
		ConfiguratorPreferencePage jm = new ConfiguratorPreferencePage();
		jm.open();
		ConfigureMavenRepositoriesWizard mr = jm.configureRepositories();
		boolean deleted = mr.removeAllRepos();
		if(deleted){
			mr.confirm();
		} else {
			mr.cancel();
		}
		jm.ok();
		MavenUserPreferencePage mu = new MavenUserPreferencePage();
		mu.open();
		mu.setUserSettings(new File("usersettings/settings.xml").getCanonicalPath());
		mu.ok();
		importMavenProject("projects/simple-jar/pom.xml");
		importMavenProject("projects/simple-jar1/pom.xml");
		importMavenProject("projects/simple-jar2/pom.xml");
	}
	
	@AfterClass
	public static void clean(){
		deleteProjects(false, false);
	}
	
	@After
	public void cleanProfiles(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			SelectProfilesDialog mp = new SelectProfilesDialog(p.getName());
			mp.open();
			mp.deselectAllProfiles();
			mp.ok();
		}
	}
	
	@Test
	public void testAutoActivatedProfiles() throws IOException{
		SelectProfilesDialog mp = new SelectProfilesDialog("simple-jar");
		mp.open();
		List<String> profiles = mp.getAllProfiles();
		mp.cancel();
		for(String p: profiles){
		    if(p.contains("(auto activated)")){
		        assertTrue(p.contains(AUTOACTIVATED_PROFILE_IN_POM) || p.contains(AUTOACTIVATED_PROFILE_IN_SETTINGS));
			}
		}
		buildProject("simple-jar", "..Maven build...", "clean verify",true);
	}
	@Test
	public void activateAllProfiles() {
		SelectProfilesDialog mp = new SelectProfilesDialog("simple-jar");
		mp.open();
		mp.activateAllProfiles();
		String profilesText = mp.getActiveProfilesText();
		mp.ok();
		
		String[] profiles = profilesText.split(", ");
		assertTrue("not all profiles are activated",profiles.length == 5);
		for(String p:profiles){
		    
			if(! (p.equals(AUTOACTIVATED_PROFILE_IN_SETTINGS) || p.equals(SIMPLE_JAR_ALL_PROFILES[0]) || p.equals(SIMPLE_JAR_ALL_PROFILES[1]) ||
					p.equals(SIMPLE_JAR_ALL_PROFILES[2]) || p.equals(PROFILE_IN_SETTINGS))){
				fail("not all profiles are activated");
			}
		}
		PackageExplorer pe = new PackageExplorer();
        pe.open();
        pe.getProject("simple-jar").select();
        WithRegexMatchers m = new WithRegexMatchers("Run As","..Maven build...");
        new ContextMenu(m.getMatchers()).select();
        new DefaultShell("Edit Configuration");
        String activeProfiles = new LabeledText("Profiles:").getText();
        new PushButton("Close").click();
        try{
            new DefaultShell("Save Changes");
            new PushButton("No").click();
        } catch(SWTLayerException ex){
            ex.printStackTrace();
        } finally {
            new WaitWhile(new ShellWithTextIsAvailable("Edit Configuration"));
            profiles = activeProfiles.split(", ");
            assertTrue("not all profiles are activated",profiles.length == 5);
            for(String p:profiles){
                
                if(! (p.equals(AUTOACTIVATED_PROFILE_IN_SETTINGS) || p.equals(SIMPLE_JAR_ALL_PROFILES[0]) || p.equals(SIMPLE_JAR_ALL_PROFILES[1]) ||
                        p.equals(SIMPLE_JAR_ALL_PROFILES[2]) || p.equals(PROFILE_IN_SETTINGS))){
                    fail("not all profiles are activated");
                }
            }
        }
		
	}
	@Test
	public void activateProfile() {
		SelectProfilesDialog mp = new SelectProfilesDialog("simple-jar");
		mp.open();
		mp.activateProfile(SIMPLE_JAR_ALL_PROFILES[0]);
		String profilesText = mp.getActiveProfilesText();
		mp.ok();
		assertTrue(profilesText.equals(SIMPLE_JAR_ALL_PROFILES[0]));
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject("simple-jar").select();
		WithRegexMatchers m = new WithRegexMatchers("Run As","..Maven build...");
        new ContextMenu(m.getMatchers()).select();
        new DefaultShell("Edit Configuration");
        String activeProfile = new LabeledText("Profiles:").getText();
        new PushButton("Close").click();
        try{
            new DefaultShell("Save Changes");
            new PushButton("No").click();
        } catch(SWTLayerException ex){
            ex.printStackTrace();
        } finally {
            new WaitWhile(new ShellWithTextIsAvailable("Edit Configuration"));
            assertEquals(SIMPLE_JAR_ALL_PROFILES[0], activeProfile);
        }
	}
	
	@Test
	public void deactivateProfile() {
		SelectProfilesDialog mp = new SelectProfilesDialog("simple-jar");
		mp.open();
		mp.deactivateProfile(AUTOACTIVATED_PROFILE_IN_POM+" (auto activated)");
		String profilesText = mp.getActiveProfilesText();
		mp.ok();
		String[] profiles = profilesText.split(", ");
		assertTrue("profile "+AUTOACTIVATED_PROFILE_IN_POM+" is still activated",profiles.length == 1);
		if(!profiles[0].equals("!"+AUTOACTIVATED_PROFILE_IN_POM)){
			fail("profile "+AUTOACTIVATED_PROFILE_IN_POM+" is still activated");
		}
		buildProject("simple-jar", "..Maven build...", "clean verify",false);
	}
	
	private static void importMavenProject(String pomPath) throws IOException{
		MavenImportWizard importWizard = new MavenImportWizard();
		importWizard.open();
		((MavenImportWizardFirstPage)importWizard.getWizardPage(0)).importProject((new File(pomPath)).getParentFile().getCanonicalPath());
	}
}
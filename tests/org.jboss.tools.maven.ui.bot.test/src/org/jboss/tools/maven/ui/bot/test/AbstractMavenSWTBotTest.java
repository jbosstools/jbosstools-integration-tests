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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jboss.reddeer.eclipse.condition.ProjectExists;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectThirdPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizard;
import org.jboss.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizardArtifactPage;
import org.jboss.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizardPage;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.StyledText;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatchers;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.junit.BeforeClass;
import org.jboss.tools.maven.reddeer.preferences.MavenPreferencePage;

public abstract class AbstractMavenSWTBotTest{
	
	@BeforeClass 
	public static void beforeClass(){
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenPreferencePage mpreferencesp = new MavenPreferencePage();
		preferenceDialog.select(mpreferencesp);
		mpreferencesp.updateIndexesOnStartup(false);
		preferenceDialog.ok();
		
		setGit();
	}
	
	public boolean hasNature(String projectName, String version, String... natureID){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new WaitUntil(new ShellWithTextIsActive("Properties for "+projectName), TimePeriod.NORMAL);
		new DefaultTreeItem("Project Facets").select();
		boolean result = new DefaultTreeItem(new DefaultTree(1),natureID).isChecked();
		if(version!=null){
			result = result && new DefaultTreeItem(new DefaultTree(1),natureID).getCell(1).equals(version);
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
		RegexMatcher rm1 = new RegexMatcher("Run As");
		RegexMatcher rm2 = new RegexMatcher(mavenBuild);
		WithTextMatchers m = new WithTextMatchers(rm1,rm2);
		new ContextMenu(m.getMatchers()).select();
		new WaitUntil(new ShellWithTextIsActive("Edit Configuration"),TimePeriod.NORMAL);
		new LabeledText("Goals:").setText(goals);
		new PushButton("Run").click();
		ProjectIsBuilt pb = new ProjectIsBuilt();
		new WaitUntil(pb,TimePeriod.VERY_LONG);
		if(shouldBuild){
			assertTrue(pb.isBuildSuccesfull());
		}else {
			assertFalse(pb.isBuildSuccesfull());
		}
	}
	
	public static void deleteProjects(boolean fromSystem){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		pexplorer.deleteAllProjects(fromSystem);
	}
	
	public void checkWebTarget(String projectName, String finalName){
	    PackageExplorer pe = new PackageExplorer();
	    pe.open();
	    pe.getProject(projectName).select();
	    new ContextMenu("Refresh").select();
	    new WaitWhile(new JobIsRunning());
	    assertTrue(pe.getProject(projectName).containsItem("target",finalName+".war"));
	}
	
	public void convertToMavenProject(String projectName, String defaultPackaging, boolean withDependencies){
		PackageExplorer pexplorer = new PackageExplorer();
		pexplorer.open();
		new WaitUntil(new ProjectExists(projectName));
		pexplorer.getProject(projectName).select();
		new ContextMenu("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		assertEquals("Project " +projectName+" packaging should be set to "+defaultPackaging, defaultPackaging, new LabeledCombo(new DefaultGroup("Artifact"),"Packaging:").getText());
		new PushButton("Finish").click();
		try{
		    new DefaultShell("Convert to Maven Dependencies");
		    new WaitUntil(new WidgetIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
		    if(withDependencies){
		        new PushButton("Finish").click();
		    } else {
		        new PushButton("Skip Dependency Conversion").click();
		    }
	        new WaitWhile(new ShellWithTextIsAvailable("Convert to Maven Dependencies"));
		} catch (SWTLayerException ex){
		    
		} finally {
 		    new WaitWhile(new ShellWithTextIsAvailable("Create new POM"));
	        new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		}
	}
	
	//TODO editor is missing in Reddeer...and check the packaging
	public void checkPackaging(String projectName, String packaging){
		PackageExplorer pExplorer = new PackageExplorer();
		pExplorer.open();
		//new ViewTreeItem(projectName,"pom.xml").select();
		//new ContextMenu("Open").select();
	}
	

	public void createWebProject(String name,String runtime, boolean webxml){
		WebProjectWizard dw = new WebProjectWizard();
		dw.open();
		WebProjectFirstPage dfp = new WebProjectFirstPage();
		dfp.setProjectName(name);
		if(runtime == null){
			dfp.setTargetRuntime("<None>");
		} else {
			dfp.setTargetRuntime(runtime);
		}
		WebProjectThirdPage dtp = new WebProjectThirdPage();
		dtp.setGenerateWebXmlDeploymentDescriptor(webxml);
		dw.finish();
	}
	
	public static void setGit(){
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		wd.select("Team","Git","Label Decorations");
		new DefaultTabItem("Text Decorations").activate();
		if(!new LabeledText("Projects:").getText().equals("{name}")){
			new LabeledText("Projects:").setText("{name}");
		}
		wd.ok();
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		
	}
	
	public void createBasicMavenProject(String artifactId, String groupId, String projectPackage, String javaTarget){
		MavenProjectWizard mw = new MavenProjectWizard();
		mw.open();
		MavenProjectWizardPage mp = new MavenProjectWizardPage();
		mp.createSimpleProject(true);
		mw.next();
		MavenProjectWizardArtifactPage ap = new MavenProjectWizardArtifactPage();
		ap.setArtifactId(artifactId);
		ap.setGroupId(groupId);
		ap.setPackage(projectPackage);
		mw.finish();
		
		Editor e= openPom(artifactId);
		StyledText stext = new DefaultStyledText();
		int pos = stext.getPositionOfText("</project>");
		String javaTargetPlugin = null;
		try {
			javaTargetPlugin = new Scanner(new FileInputStream("resources/pom/JavaTarget")).useDelimiter("\\A").next();
		} catch (FileNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		stext.selectPosition(pos);
		stext.insertText(javaTargetPlugin);
		stext.selectText("source_to_replace");
		stext.insertText(javaTarget);
		stext.selectText("target_to_replace");
		stext.insertText(javaTarget);
		e.save();
	}
	
	public Editor openPom(String project){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(project).getProjectItem("pom.xml").open();
		Editor e = new DefaultEditor(project+"/pom.xml");
		new DefaultCTabItem("pom.xml").activate();
		return e;
	}
	
}
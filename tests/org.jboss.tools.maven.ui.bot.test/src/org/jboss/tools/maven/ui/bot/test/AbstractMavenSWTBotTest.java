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

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.matcher.WithTextMatchers;
import org.eclipse.reddeer.eclipse.condition.ProjectExists;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectFirstPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectThirdPage;
import org.eclipse.reddeer.eclipse.jst.servlet.ui.project.facet.WebProjectWizard;
import org.eclipse.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizard;
import org.eclipse.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizardArtifactPage;
import org.eclipse.reddeer.eclipse.m2e.core.ui.wizard.MavenProjectWizardPage;
import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.api.StyledText;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.maven.reddeer.preferences.MavenPreferencePage;
import org.jboss.tools.maven.ui.bot.test.utils.JavaVersionHelper;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectIsBuilt;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractMavenSWTBotTest{
	
	protected static final Logger log = Logger.getLogger(AbstractMavenSWTBotTest.class);
	
	@BeforeClass 
	public static void beforeClass(){
		new WorkbenchShell().maximize();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenPreferencePage mpreferencesp = new MavenPreferencePage(preferenceDialog);
		preferenceDialog.select(mpreferencesp);
		mpreferencesp.updateIndexesOnStartup(false);
		preferenceDialog.ok();
		
		setGit();
	}
	
	@AfterClass
	public static void cleanUp(){
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			try{
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				if(!p.getTreeItem().isDisposed()){
					org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
	}
	
	public boolean hasNature(String projectName, String version, String... natureID){
		PropertyDialog pd = openPropertiesProject(projectName);
		new WaitUntil(new ShellIsAvailable("Properties for "+projectName), TimePeriod.DEFAULT);
		new DefaultTreeItem("Project Facets").select();
		boolean result = new DefaultTreeItem(new DefaultTree(1),natureID).isChecked();
		if(version!=null){
			result = result && new DefaultTreeItem(new DefaultTree(1),natureID).getCell(1).equals(version);
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		pd.ok();
		new WaitWhile(new ShellIsAvailable("Properties for "+projectName), TimePeriod.DEFAULT);
		return result;
	}
	
	protected void addDependency(String projectName, String groupId, String artifactId, String version){
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Maven","Add Dependency").select();
		new WaitUntil(new ShellIsAvailable("Add Dependency"), TimePeriod.DEFAULT);
		new LabeledText("Group Id:").setText(groupId);
		new LabeledText("Artifact Id:").setText(artifactId);
		new LabeledText("Version: ").setText(version);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
	}
	
	public void addPlugin(String projectName, String groupId, String artifactId, String version){
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Maven","Add Plugin").select();
		new WaitUntil(new ShellIsAvailable("Add Plugin"), TimePeriod.DEFAULT);
		new LabeledText("Group Id:").setText(groupId);
		new LabeledText("Artifact Id:").setText(artifactId);
		new LabeledText("Version: ").setText(version);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	protected static void updateConf(String projectName){
		updateConf(projectName,false);
	}
	
	protected static void updateConf(String projectName,boolean forceDependencies){
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Maven","Update Project...").select();
		new WaitUntil(new ShellIsAvailable("Update Maven Project"),TimePeriod.LONG);
		new CheckBox("Force Update of Snapshots/Releases").toggle(forceDependencies);
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Update Maven Project"),TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
	
	public void buildProject(String projectName, String mavenBuild, String goals, boolean shouldBuild){
		ConsoleView cview = new ConsoleView();
		cview.open();
		try{
			cview.clearConsole();
		} catch (CoreLayerException ex){
			//there's not clear console button, since nothing run before
		}
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		RegexMatcher rm1 = new RegexMatcher("Run As");
		RegexMatcher rm2 = new RegexMatcher(mavenBuild);
		WithTextMatchers m = new WithTextMatchers(rm1,rm2);
		new ContextMenuItem(m.getMatchers()).select();
		new WaitUntil(new ShellIsAvailable("Edit Configuration"),TimePeriod.DEFAULT);
		new LabeledText("Goals:").setText(goals);
		new PushButton("Run").click();
		ProjectIsBuilt pb = new ProjectIsBuilt();
		new WaitUntil(pb,TimePeriod.VERY_LONG);
		if(shouldBuild){
			assertTrue("Build should be succesfull but is not.. \n" + pb.description(),pb.isBuildSuccesfull());
			
		}else {
			assertFalse(pb.isBuildSuccesfull());
		}
	}
	
	public static void deleteProjects(boolean fromSystem){
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			try{
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				if(!p.getTreeItem().isDisposed()){
					org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
	}
	
	public void checkWebTarget(String projectName, String finalName){
	    PackageExplorerPart pe = new PackageExplorerPart();
	    pe.open();
	    pe.getProject(projectName).select();
	    new ContextMenuItem("Refresh").select();
	    new WaitWhile(new JobIsRunning());
	    assertTrue(pe.getProject(projectName).containsResource("target",finalName+".war"));
	}
	
	public void convertToMavenProject(String projectName, String defaultPackaging, boolean withDependencies){
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		new WaitUntil(new ProjectExists(projectName));
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Configure","Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		assertEquals("Project " +projectName+" packaging should be set to "+defaultPackaging, defaultPackaging, new LabeledCombo(new DefaultGroup("Artifact"),"Packaging:").getText());
		new PushButton("Finish").click();
		try{
		    new DefaultShell("Convert to Maven Dependencies");
		    new WaitUntil(new ControlIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
		    if(withDependencies){
		        new PushButton("Finish").click();
		    } else {
		        new PushButton("Skip Dependency Conversion").click();
		    }
	        new WaitWhile(new ShellIsAvailable("Convert to Maven Dependencies"));
		} catch (CoreLayerException ex){
		    
		} finally {
 		    new WaitWhile(new ShellIsAvailable("Create new POM"));
	        new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		}
	}
	
	//TODO editor is missing in Reddeer...and check the packaging
	public void checkPackaging(String projectName, String packaging){
		PackageExplorerPart pExplorer = new PackageExplorerPart();
		pExplorer.open();
		//new ViewTreeItem(projectName,"pom.xml").select();
		//new ContextMenu("Open").select();
	}
	

	public void createWebProject(String name,String runtime, boolean webxml){
		WebProjectWizard dw = new WebProjectWizard();
		dw.open();
		WebProjectFirstPage dfp = new WebProjectFirstPage(dw);
		dfp.setProjectName(name);
		if(runtime == null){
			dfp.setTargetRuntime("<None>");
		} else {
			dfp.setTargetRuntime(runtime);
		}
		
		// update default configuration for Java 8
		if (JavaVersionHelper.getJavaVersion().equals("1.8")) {
			new PushButton("Modify...").click();
			Tree tree = new DefaultTree();
			tree.getItem("Java").select();
			tree.getContextMenu().getItem("Change Version...").select();
			new DefaultCombo(0).setSelection("1.8");
			new OkButton().click();
			new OkButton().click();
		}
		
		dw.next();
		dw.next();
		WebProjectThirdPage dtp = new WebProjectThirdPage(dw);
		dtp.setGenerateWebXmlDeploymentDescriptor(webxml);
		dw.finish(TimePeriod.VERY_LONG);
		waitForAllScheduledJobs();
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
		MavenProjectWizardPage mp = new MavenProjectWizardPage(mw);
		mp.createSimpleProject(true);
		mw.next();
		MavenProjectWizardArtifactPage ap = new MavenProjectWizardArtifactPage(mw);
		ap.setArtifactId(artifactId);
		ap.setGroupId(groupId);
		ap.setPackage(projectPackage);
		mw.finish(TimePeriod.VERY_LONG);
		
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
	
	private void waitForAllScheduledJobs(){
		int i =1;
		while(i>0){
			try{
				new WaitUntil(new JobIsRunning());
			} catch (WaitTimeoutExpiredException ex){
				break;
			}
			new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		}
	}
	
	public PropertyDialog openPropertiesProject(String project){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(project);
		PropertyDialog pd = new PropertyDialog(project);
		pd.open();
		return pd;
	}
	
	public PropertyDialog openPropertiesPackage(String project){
		PackageExplorerPart pe = new PackageExplorerPart();
		pe.open();
		pe.selectProjects(project);
		PropertyDialog pd = new PropertyDialog(project);
		pd.open();
		return pd;
	}
	
}
/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage.SourceAttachmentEnum;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;
import org.junit.BeforeClass;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY10x)
public class DeltaspikeTestBase {

	protected static final ProblemsView problemsView = new ProblemsView();
	
	@BeforeClass
	public static void disableSourceLookup(){
		
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage sp = new SourceLookupPreferencePage();
		preferenceDialog.select(sp);
		sp.setSourceAttachment(SourceAttachmentEnum.NEVER);
		preferenceDialog.ok();
	}

	protected static void importDeltaspikeProject(String projectName,ServerRequirement sr) {
		
		try {
			importMavenProject(projectName);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unable to import "+projectName);
		}
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
		new DefaultTreeItem("Targeted Runtimes").select();
		for(TableItem i: new DefaultTable().getItems()){
			if(i.getText().equals(sr.getRuntimeNameLabelText(sr.getConfig()))){
				i.setChecked(true);
			} else {
				i.setChecked(false);
			}
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+projectName));
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	public static void importMavenProject(String projectName) throws IOException {
		
		try {
			final Path sourceFolder = new File("target/classes/prj/"+projectName).toPath();
			File dir = new File("target/copies/"+projectName);
			if(dir.exists()){
				deleteDir(dir);
			}
			final Path destFolder = dir.toPath();
			Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(destFolder.resolve(sourceFolder.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
					Files.copy(file, destFolder.resolve(sourceFolder.relativize(file)));
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			fail("Unable to find pom "+projectName);
		}
		
		String pomPath = "target/copies/"+projectName;
		MavenImportWizard importWizard = new MavenImportWizard();
		importWizard.open();
		new MavenImportWizardFirstPage().importProject((new File(pomPath)).getCanonicalPath(),false);
	}
	
	private static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                fail("Unable to delete "+dir);
	            }
	        }
	    }

	    return dir.delete();
	}

	protected void insertIntoFile(String projectName, String packageName,
			String bean, int line, int column, String insertedText) {
		openClass(projectName, packageName, bean);
		TextEditor e =new TextEditor(bean);
		e.insertText(line, column, insertedText);
		//SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		//editor.insertText(line, column, insertedText);
		e.save();
	}
	
	protected void replaceInEditor(String projectName, String packageName, String bean, 
			String target, String replacement, boolean save) {
		openClass(projectName, packageName, bean);
		replaceInEditor(target, replacement, save);
	}
	
	protected void replaceInEditor(String target, String replacement, boolean save) {
		TextEditor e =new TextEditor();
		//eclipseEditor.selectRange(0, 0, eclipseEditor.getText().length());
		e.setText(e.getText().replace(
				target + (replacement.equals("") ? System
								.getProperty("line.separator") : ""),
				replacement));		
		if (save) e.save();
	}

	protected void annotateBean(String projectName, String packageName,
			String bean, int line, int column, String annotation) {

		insertIntoFile(projectName, packageName, bean, line, column, annotation);
		new WaitUntil(new SpecificProblemExists(new RegexMatcher(
				".*cannot be resolved.*")), TimePeriod.NORMAL);

		TextEditor e =new TextEditor();
		new ShellMenu(new WithTextMatcher("Source"), new RegexMatcher("Organize Imports.*")).select();

		e.save();

	}

	protected void openClass(String projectName, String packageName,
			String classFullName) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName)
				.getProjectItem("Java Resources","src", packageName, classFullName).open();

	}
	
	protected static void cleanProjects() {

		new ShellMenu(new WithTextMatcher("Project"), new RegexMatcher("Clean.*")).select();
		new WaitUntil(new ShellWithTextIsActive("Clean"));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Clean"));
		new WaitWhile(new JobIsRunning());
		
	}

	protected void deleteAllProjects() {
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			try{
				org.jboss.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.NORMAL);
				if(!p.getTreeItem().isDisposed()){
					org.jboss.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
	}
}

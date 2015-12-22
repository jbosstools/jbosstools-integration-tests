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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DeltaspikeTestBase {

	private static final File DELTASPIKE_LIBRARY_DIR = new File(
			System.getProperty("deltaspike.libs.dir"));

	protected static final ProblemsView problemsView = new ProblemsView();

	@AfterClass
	public static void cleanUp() {
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
		ExternalProjectImportWizardDialog iDialog = new ExternalProjectImportWizardDialog();
		iDialog.open();
		WizardProjectsImportPage fPage = new WizardProjectsImportPage();
		fPage.copyProjectsIntoWorkspace(true);
		try {
			fPage.setRootDirectory((new File("resources/prj/"+projectName)).getParentFile().getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fPage.selectProjects(projectName);
		iDialog.finish();

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
		new WaitWhile(new JobIsRunning());
		addDeltaspikeLibrariesIntoProject(projectName);
		cleanProjects();
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

	private static void addDeltaspikeLibrariesIntoProject(String projectName) {

		File[] libraries = addLibraryIntoProjectFolder(projectName,
				DELTASPIKE_LIBRARY_DIR);
		if (libraries == null)
			return;
		/** refresh the workspace **/
		new ShellMenu("File", "Refresh").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).select();

		new ContextMenu("Properties").select();
		new DefaultShell("Properties for "+projectName);
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();

		for (File library : libraries) {
			new PushButton("Add JARs...").click();
			new DefaultShell("JAR Selection");
			new DefaultTreeItem(projectName, library.getName()).select();
			new WaitUntil(new ButtonWithTextIsEnabled(new PushButton("OK")));
			new PushButton("OK").click();
			new WaitWhile(new ShellWithTextIsAvailable("JAR Selection"));
		}
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Properties for "+projectName));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

	}

	private static File[] addLibraryIntoProjectFolder(String projectName,
			File librariesFolder) {
		FileChannel inChannel = null;
		FileChannel outChannel = null;

		assertNotNull("You have to provide location of folder with "
				+ "library with property 'deltaspike.libs.dir'",
				DELTASPIKE_LIBRARY_DIR);
		List<File> libraryFiles = new ArrayList<File>();
		FileInputStream istream = null;
		FileOutputStream ostream = null;
		try {
			for (File in : librariesFolder.listFiles()) {
				if (in.isDirectory() || 
					!in.getName().substring(in.getName().lastIndexOf(".") + 1).equals("jar")) {
					continue;
				}
				File out = new File(Platform.getLocation() + File.separator
						+ projectName + File.separator + File.separator
						+ in.getName());

				istream = new FileInputStream(in);
				ostream = new FileOutputStream(out);
				
				inChannel = istream.getChannel();
				outChannel = ostream.getChannel();

				inChannel.transferTo(0, inChannel.size(), outChannel);
				libraryFiles.add(in);
			}
		} catch (IOException ioException) {

		} finally {
			try{
				if (istream != null){
					istream.close();
				}
				if (ostream != null){
					ostream.close();
				}
			} catch (IOException ex){
				
			}
		}
		return libraryFiles.toArray(new File[libraryFiles.size()]);
	}

}

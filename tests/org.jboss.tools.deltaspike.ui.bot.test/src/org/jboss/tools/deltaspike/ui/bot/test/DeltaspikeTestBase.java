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
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.WithRegexMatchers;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage.SourceAttachmentEnum;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DeltaspikeTestBase {

	private static final File DELTASPIKE_LIBRARY_DIR = new File(
			System.getProperty("deltaspike.libs.dir"));

	protected static final PackageExplorer packageExplorer = new PackageExplorer();
	protected static final ProblemsView problemsView = new ProblemsView();

	@AfterClass
	public static void cleanUp() {
		deleteAllProjects();
	}
	
	@BeforeClass
	public static void disableSourceLookup(){
		SourceLookupPreferencePage sp = new SourceLookupPreferencePage();
		sp.open();
		sp.setSourceAttachment(SourceAttachmentEnum.NEVER);
		sp.ok();
	}

	protected static void importDeltaspikeProject(String projectName,ServerRequirement sr) {
		ExternalProjectImportWizardDialog iDialog = new ExternalProjectImportWizardDialog();
		iDialog.open();
		WizardProjectsImportPage fPage = iDialog.getFirstPage();
		fPage.copyProjectsIntoWorkspace(true);
		try {
			fPage.setRootDirectory((new File("resources/prj/"+projectName)).getParentFile().getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fPage.selectProjects(projectName);
		iDialog.finish();

		PackageExplorer pe = new PackageExplorer();
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
		new PushButton("Apply").click();
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
		new WaitUntil(new SpecificProblemExists(new Regex(
				".*cannot be resolved.*")), TimePeriod.NORMAL);

		TextEditor e =new TextEditor();
		WithRegexMatchers m = new WithRegexMatchers("Source", "Organize Imports.*");
		new ShellMenu(m.getMatchers()).select();

		e.save();

	}

	protected void openClass(String projectName, String packageName,
			String classFullName) {

		packageExplorer.open();
		packageExplorer.getProject(projectName)
				.getProjectItem("src", packageName, classFullName).open();

	}
	
	protected static void cleanProjects() {
		
		WithRegexMatchers m = new WithRegexMatchers("Project", "Clean.*");
		new ShellMenu(m.getMatchers()).select();
		new WaitUntil(new ShellWithTextIsActive("Clean"));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Clean"));
		new WaitWhile(new JobIsRunning());
		
	}

	private static void deleteAllProjects() {
		packageExplorer.open();
		for (Project project : packageExplorer.getProjects()) {
			project.delete(true);
		}
	}

	private static void addRuntimeIntoProject(String runtimeName,
			String projectName) {

		packageExplorer.open();
		packageExplorer.getProject(projectName).select();

		/* will be simpler in the future -> new TargetedRuntimesProperties() */
		new ContextMenu("Properties").select();
		Shell shell = new DefaultShell();
		new DefaultTreeItem("Targeted Runtimes").select();
		new CheckBox("Show all runtimes").toggle(true);
		Table table = new DefaultTable();
		for (int i = 0; i < table.rowCount(); i++) {
			table.getItem(i).setChecked(false);
		}
		table.getItem(runtimeName).setChecked(true);
		String textShell = shell.getText();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(textShell),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitWhile(new ShellWithTextIsActive("Progress Information"),
				TimePeriod.LONG);

	}

	private static void addDeltaspikeLibrariesIntoProject(String projectName) {

		File[] libraries = addLibraryIntoProjectFolder(projectName,
				DELTASPIKE_LIBRARY_DIR);
		if (libraries == null)
			return;
		/** refresh the workspace **/
		new ShellMenu("File", "Refresh").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		packageExplorer.open();
		packageExplorer.getProject(projectName).select();

		new ContextMenu("Properties").select();
		new DefaultShell();
		new DefaultTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();

		for (File library : libraries) {
			new PushButton("Add JARs...").click();
			new WaitUntil(new ShellWithTextIsActive("JAR Selection"),
					TimePeriod.NORMAL);
			new DefaultShell("JAR Selection");
			new DefaultTreeItem(projectName, library.getName()).select();
			new WaitUntil(new ButtonWithTextIsActive(new PushButton("OK")));
			new PushButton("OK").click();
			new WaitWhile(new ShellWithTextIsActive("JAR Selection"),
					TimePeriod.NORMAL);
		}
		new PushButton("OK").click();
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

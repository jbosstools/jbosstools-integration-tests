/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.uiutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.exception.WorkbenchPartNotFound;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class EditorResourceHelper {
	
	
	public void replaceClassContentByResource(String editorName, InputStream resource, boolean closeEdit) {
		replaceClassContentByResource(editorName, resource, true, closeEdit);
	}
	
	/**
	 * method replaces whole content of class "classEdit" by inputstream resource
	 * If closeEdit param is true, editor is not only saved but closed as well 
	 * Prerequisite: editor has been set 
	 * @param classEdit
	 * @param resource
	 * @param closeEdit
	 */
	public void replaceClassContentByResource(String editorName, InputStream resource, boolean save, boolean closeEdit) {
		String code = readStream(resource);
		DefaultEditor e = new DefaultEditor(editorName);
		new DefaultStyledText().setText("");
		new DefaultStyledText().setText(code);
		if (save) e.save();
		if (closeEdit) e.close();
	}
	
	public void replaceClassContentByResource(String editorName, InputStream resource, boolean closeEdit, String... param) {
		replaceClassContentByResource(editorName, resource, true, closeEdit, param);
	}
	
	/**
	 * Method copies resource to class opened in SWTBotEditor with entered parameters
	 * @param classEdit
	 * @param resource
	 * @param closeEdit
	 * @param param
	 */
	public void replaceClassContentByResource(String editorName, InputStream resource, boolean save, 
			boolean closeEdit, String... param) {
		String s = readStream(resource);
		String code = MessageFormat.format(s, (Object[])param);
		TextEditor e = new TextEditor(editorName);
		e.setText(code);
		if (save) e.save();
		if (closeEdit) e.close();
	}
	
	/**
	 * method copies resource from folder "src" param to folder "target" param
	 * @param src
	 * @param target
	 */
	public void copyResource(String src, String target) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProjects()[0];
		IFile f = project.getFile(target);
		String targetAbsolute = f.getLocationURI().getPath();
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			System.out.println(new File(src).getAbsolutePath());
			System.out.println(targetAbsolute);
			inputChannel = new FileInputStream(new File(src).getAbsolutePath()).getChannel();
			outputChannel = new FileOutputStream(targetAbsolute).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputChannel.close();
				outputChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void replaceInEditor(String target, String replacement) {
		replaceInEditor(target, replacement, true);
	}
	
	/**
	 * Method replaces string "target" by string "replacement.
	 * Prerequisite: editor has been set
	 * @param target
	 * @param replacement
	 */
	public void replaceInEditor(String target, String replacement, boolean save) {
		
		try{
			TextEditor editor = new TextEditor();
			editor.setText(editor.getText().replace(
				target + (replacement.equals("") ? System
								.getProperty("line.separator") : ""),
				replacement));
			if (save) editor.save();
		} catch (WorkbenchPartNotFound ex){
			Editor textEditor = new DefaultEditor();
			DefaultStyledText dt = new DefaultStyledText();
			String text = dt.getText();
			dt.setText("");
			dt.setText(text.replace(
					target + (replacement.equals("") ? System
									.getProperty("line.separator") : ""),
					replacement));		
			if (save) textEditor.save();
		}
	}

	public void replaceInEditor(String editorName, String target, String replacement) {
		replaceInEditor(editorName, target, replacement, true);
	}
	
	/**
	 * Method replaces string "target" by string "replacement.
	 * Prerequisite: editor has been set
	 * @param target
	 * @param replacement
	 */
	public void replaceInEditor(String editorName, String target, String replacement, boolean save) {
		
		try{
			TextEditor editor = new TextEditor(editorName);
			editor.setText(editor.getText().replace(
				target + (replacement.equals("") ? System
								.getProperty("line.separator") : ""),
				replacement));
			if (save) editor.save();
		} catch (WorkbenchPartNotFound ex){
			Editor textEditor = new DefaultEditor(editorName);
			DefaultStyledText dt = new DefaultStyledText();
			String text = dt.getText();
			dt.setText("");
			dt.setText(text.replace(
					target + (replacement.equals("") ? System
									.getProperty("line.separator") : ""),
					replacement));		
			if (save) textEditor.save();
		}
	}
	
	/**
	 * Method inserts the string "insertText" on location ("line", "column")
	 * Prerequisite: editor has been set
	 * @param line
	 * @param column
	 * @param insertText
	 */
	public void insertInEditor(int line, int column, String insertText) {
		TextEditor textEditor = new TextEditor();
		textEditor.insertLine(line, insertText);
		textEditor.save();
	}
	
	
	
	public List<String> getProposalList(String editorTitle, String textToSelect) {
		Editor editor = new DefaultEditor(editorTitle);
		DefaultStyledText dt = new DefaultStyledText();
		dt.selectPosition(dt.getPositionOfText(textToSelect));
		ContentAssistant cs = editor.openContentAssistant();
		List<String> proposals = cs.getProposals();
		cs.close();
		return proposals;
	}
	
	public List<String> getProposalList(String editorTitle, String textToSelect, int position) {
		Editor editor = new DefaultEditor(editorTitle);
		DefaultStyledText dt = new DefaultStyledText();
		dt.selectPosition(dt.getPositionOfText(textToSelect)+position);
		AbstractWait.sleep(TimePeriod.SHORT);
		ContentAssistant cs = editor.openContentAssistant();
		List<String> proposals = cs.getProposals();
		cs.close();
		return proposals;
	}
	
	/**
	 * Method deletes whole package with given name for entered project
	 * @param projectName
	 * @param packageName
	 */
	public void deletePackage(String projectName, String packageName) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Refresh").select();
		new WaitWhile(new JobIsRunning());
		deleteInProjectExplorer(projectName, "src",packageName);	
	}
	
	/**
	 * Method deletes whole web folder with given name for entered project
	 * @param projectName
	 * @param PACKAGE_NAME
	 */
	public void deleteWebFolder(String projectName) {
		deleteInProjectExplorer(projectName, "WebContent");
		
	}
	
	/**
	 * Method deletes folder with given name and path
	 * @param folderName
	 * @param path
	 */
	public void deleteInProjectExplorer(String projectName, String... path) {
		PackageExplorer pe = new PackageExplorer();
		Project p = pe.getProject(projectName);
		p.select();
		//refresh project due to bug in eclipse - new packages are shown outside of src
		new ContextMenu("Refresh").select();
		p.getProjectItem(path).delete();
	}

	/**
	 * Methods converts input stream to string component
	 * @param inputStream
	 * @return String - input stream converted to string
	 */
	public String readStream(InputStream inputStream) {
		// we don't care about performance in tests too much, so this should be
		// OK
		return new Scanner(inputStream).useDelimiter("\\A").next();
	}
	
	public void renameFileInExplorerBase(String project, String newFileName, String... oldFilePath) {
		PackageExplorer ex = new PackageExplorer();
		ex.open();
		ex.getProject(project).getProjectItem(oldFilePath).select();
		
		new ShellMenu(IDELabel.Menu.FILE, IDELabel.Menu.RENAME_WITH_DOTS).select();
		new DefaultShell(IDELabel.Shell.RENAME_RESOURCE);
		
		new LabeledText("New name:").setText(newFileName);	
		
		new PushButton(IDELabel.Button.OK).click();		
		new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.RENAME_RESOURCE));
		new WaitWhile(new JobIsRunning());
	}
	
	public void moveFileInExplorerBase(String projectName, String[] sourceFile, String[] destFolder) {
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(sourceFile).select();
		new ShellMenu(IDELabel.Menu.FILE,IDELabel.Menu.MOVE).select();
		new DefaultShell(IDELabel.Shell.MOVE);
		new DefaultTreeItem(destFolder).select();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(IDELabel.Shell.MOVE));
		new WaitWhile(new JobIsRunning());
	}

}
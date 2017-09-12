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
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.swt.api.StyledText;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.menu.ShellMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.api.Editor;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.core.exception.WorkbenchCoreLayerException;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.common.reddeer.label.IDELabel;

public class EditorResourceHelper {
	
	public void replaceClassContentByResource(String editorName, String code, boolean closeEdit) {
		replaceClassContentByResource(editorName, code, true, closeEdit);
		new WaitWhile(new JobIsRunning());
	}
	
	/**
	 * Method copies resource to class opened in SWTBotEditor with entered parameters
	 * @param classEdit
	 * @param resource
	 * @param closeEdit
	 * @param param
	 */
	public void replaceClassContentByResource(String editorName, String code, boolean save, 
			boolean closeEdit) {
		Editor e = new DefaultEditor(editorName);
		StyledText t = new DefaultStyledText();
		t.setText("");
		t.setText(code);
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
		Editor editor = new DefaultEditor();
		DefaultStyledText dt = new DefaultStyledText();
		String text = dt.getText();
		dt.setText("");
		dt.setText(text.replace(target, replacement));		
		if (save) editor.save();
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
		} catch (WorkbenchCoreLayerException ex){
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
		new WaitWhile(new JobIsRunning());
		AbstractWait.sleep(TimePeriod.getCustom(2));
		ContentAssistant cs = editor.openContentAssistant();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		List<String> proposals = cs.getProposals();
		cs.close();
		return proposals;
	}
	
	public List<String> getProposalList(String editorTitle, String textToSelect, int position) {
		Editor editor = new DefaultEditor(editorTitle);
		DefaultStyledText dt = new DefaultStyledText();
		dt.selectPosition(dt.getPositionOfText(textToSelect)+position);
		new WaitWhile(new JobIsRunning());
		AbstractWait.sleep(TimePeriod.getCustom(2));
		ContentAssistant cs = editor.openContentAssistant();
		AbstractWait.sleep(TimePeriod.getCustom(2));
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
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).refresh();
		deleteInProjectExplorer(projectName, CDIConstants.JAVA_RESOURCES,CDIConstants.SRC,packageName);	
	}
	
	/**
	 * Method deletes whole web folder with given name for entered project
	 * @param projectName
	 * @param PACKAGE_NAME
	 */
	public void deleteWebPagesFolder(String projectName) {
		deleteInProjectExplorer(projectName, "WebContent","pages");
		
	}
	
	/**
	 * Method deletes folder with given name and path
	 * @param folderName
	 * @param path
	 */
	public void deleteInProjectExplorer(String projectName, String... path) {
		ProjectExplorer pe = new ProjectExplorer();
		Project p = pe.getProject(projectName);
		p.select();
		//refresh project due to bug in eclipse - new packages are shown outside of src
		new ContextMenu().getItem("Refresh").select();
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
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(project).getProjectItem(oldFilePath).select();
		
		new ShellMenu().getItem(IDELabel.Menu.FILE, IDELabel.Menu.RENAME_WITH_DOTS).select();
		new DefaultShell(IDELabel.Shell.RENAME_RESOURCE);
		
		new LabeledText("New name:").setText(newFileName);	
		
		new PushButton(IDELabel.Button.OK).click();		
		new WaitWhile(new ShellIsAvailable(IDELabel.Shell.RENAME_RESOURCE));
		new WaitWhile(new JobIsRunning());
	}
	
	public void moveFileInExplorerBase(String projectName, String[] sourceFile, String[] destFolder) {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(sourceFile).select();
		new ShellMenu().getItem(IDELabel.Menu.FILE,IDELabel.Menu.MOVE).select();
		new DefaultShell(IDELabel.Shell.MOVE_RESOURCES);
		new DefaultTreeItem(destFolder).select();
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable(IDELabel.Shell.MOVE_RESOURCES));
		new WaitWhile(new JobIsRunning());
	}

}
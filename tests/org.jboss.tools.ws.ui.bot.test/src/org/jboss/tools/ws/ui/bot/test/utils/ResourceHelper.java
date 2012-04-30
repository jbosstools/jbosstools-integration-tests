/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

public class ResourceHelper {

	/**
	 * Method converts input stream to text format
	 * @param is
	 * @return
	 */
	public String readStream(InputStream is) {
		// we don't care about performance in tests too much, so this should be
		// OK
		return new Scanner(is).useDelimiter("\\A").next();
	}

	/**
	 * Method reads input file and output it as text
	 * @param f
	 * @return
	 */
	public String readFile(IFile f) {
		String content = null;
		InputStream is = null;
		try {
			is = f.getContents();
			content = readStream(is);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return content;
	}

	/**
	 * Method replaces string "target" by string "replacement.
	 * @param target
	 * @param replacement
	 */
	public void replaceInEditor(SWTBotEclipseEditor ed, String target, String replacement) {
		replaceInEditor(ed, target, replacement, true);
	}
	
	/**
	 * Method replaces string "target" by string "replacement.
	 * @param target
	 * @param replacement
	 */
	public void replaceInEditor(SWTBotEclipseEditor ed, String target, 
			String replacement, boolean save) {
		ed.selectRange(0, 0, ed.getText().length());
		ed.setText(ed.getText().replace(target,replacement));		
		if (save) ed.save();
	}
	
	/**
	 * Method copies resource to class opened in SWTBotEditor with entered parameters
	 * @param classEdit
	 * @param resource
	 * @param closeEdit
	 * @param param
	 */
	public void copyResourceToClass(SWTBotEditor classEdit,
			InputStream resource, boolean closeEdit, Object... param) {
		copyResourceToClass(classEdit, resource, true, closeEdit, param);
	}
	
	/**
	 * Method copies resource to class opened in SWTBotEditor with entered parameters
	 * @param classEdit
	 * @param resource
	 * @param closeEdit
	 * @param param
	 */
	public void copyResourceToClass(SWTBotEditor classEdit,
			InputStream resource, boolean save, boolean closeEdit, Object... param) {
		String s = readStream(resource);
		String code = MessageFormat.format(s, param);
		SWTBotEclipseEditor st = classEdit.toTextEditor();
		st.selectRange(0, 0, st.getText().length());
		st.setText(code);
		if (save) classEdit.save();
		if (closeEdit) classEdit.close();
	}
	
	/**
	 * Method recursively searches files names in dir and returns them as List of File objects 
	 * @param dir
	 * @param searchingFileNames
	 * @return
	 */
	public List<File> searchAllFiles(File dir, String[] searchingFileNames) {
		
		List<File> restEasyLibs = new ArrayList<File>();
		
		for (String restEasyLibReq : searchingFileNames) {
			restEasyLibs.add(searchInTextFiles(dir, restEasyLibReq));
		}
		return restEasyLibs;
	}

	/**
	 * Method recursively searches files name in dir and returns it as File object
	 * @param dir
	 * @param searchingFileName
	 * @return
	 */
	public File searchInTextFiles(File dir, String searchingFileName) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				File f = searchInTextFiles(file, searchingFileName);
				if (f != null) {
					return f;
				}
			} else {
				if (file.getName().equals(searchingFileName)) {
					return file;
				}
			}
		}
		return null;
	}
	
}

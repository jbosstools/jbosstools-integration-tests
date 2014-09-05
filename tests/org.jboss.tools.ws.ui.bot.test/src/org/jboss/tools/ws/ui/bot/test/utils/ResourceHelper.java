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
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 * 
 */
public class ResourceHelper {

	/**
	 * Method converts input stream to text format
	 * @param is
	 * @return
	 */
	public String readStream(InputStream is) {
		// we don't care about performance in tests too much, so this should be
		// OK
		try(Scanner scanner = new Scanner(is)) {
			return scanner.useDelimiter("\\A").next();
		}
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
	 * Method copies resource to class opened in SWTBotEditor with entered parameters
	 */
	public void copyResourceToClassWithSave(InputStream resource,
			boolean save, Object... param) {
		String s = readStream(resource);
		String code = MessageFormat.format(s, param);
		TextEditor editor = new TextEditor();
		editor.setText(code);
		if (save) {
			editor.save();
		}
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

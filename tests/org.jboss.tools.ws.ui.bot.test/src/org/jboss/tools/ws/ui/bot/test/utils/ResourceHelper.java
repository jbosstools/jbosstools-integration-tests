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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
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
		ed.selectRange(0, 0, ed.getText().length());
		ed.setText(ed.getText().replace(target,replacement));		
		ed.save();
	}

	
	/**
	 * Method copies resource to class opened in SWTBotEditor
	 * @param classEdit
	 * @param resource
	 * @param closeEdit
	 */
	public void copyResourceToClass(SWTBotEditor classEdit,
			InputStream resource, boolean closeEdit) {
		SWTBotEclipseEditor st = classEdit.toTextEditor();
		st.selectRange(0, 0, st.getText().length());
		String code = readStream(resource);
		st.setText(code);
		classEdit.save();
		if (closeEdit) classEdit.close();
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
		String s = readStream(resource);
		String code = MessageFormat.format(s, param);
		classEdit.toTextEditor().selectRange(0, 0, classEdit.toTextEditor().getText().length());
		classEdit.toTextEditor().setText(code);
		classEdit.save();
		if (closeEdit) classEdit.close();
	}
	
}

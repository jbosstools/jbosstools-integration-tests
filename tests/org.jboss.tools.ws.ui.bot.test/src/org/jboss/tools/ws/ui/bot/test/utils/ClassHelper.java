/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.utils;

import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;

/**
 * Created by Jan Novak
 */
public class ClassHelper {

	public static void addImport(TextEditor editor, String importString) {
		editor.activate();
		int lineAfterPackageDefinition = getPackageDefinitionLine(editor) + 1;
		new DefaultStyledText().insertText(lineAfterPackageDefinition, 0, System.lineSeparator());
		new DefaultStyledText().insertText(lineAfterPackageDefinition, 0, importString);
	}

	public static void addClassAnnotation(TextEditor editor, String annotation) {
		editor.activate();
		int lineInFrontOfClassDef = getClassDefinitionLine(editor);
		new DefaultStyledText().insertText(lineInFrontOfClassDef, 0, System.lineSeparator());
		new DefaultStyledText().insertText(lineInFrontOfClassDef, 0, annotation);
	}

	public static void addClassAnnotation(TextEditor editor, String annotation, String importString) {
		addClassAnnotation(editor, annotation);
		addImport(editor, importString);
	}

	public static int getClassDefinitionLine(TextEditor editor) {
		for (String definition : new String[]{"public class", "public @interface", "public interface"}) {
			int lineOfObjectDef = editor.getLineOfText(definition);
			if (lineOfObjectDef != -1)
				return lineOfObjectDef;
		}

		throw new IllegalArgumentException("No definition was found in the Editor!");
	}

	private static int getPackageDefinitionLine(TextEditor editor) {
		int line = editor.getLineOfText("package ");

		if (line != -1)
			return line;

		throw new IllegalArgumentException("No package definition was found in the Editor!");
	}
}

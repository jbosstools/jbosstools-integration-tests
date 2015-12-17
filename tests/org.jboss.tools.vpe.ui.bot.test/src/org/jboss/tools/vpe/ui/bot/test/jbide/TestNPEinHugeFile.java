/*******************************************************************************
 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.jbide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

public class TestNPEinHugeFile extends VPEAutoTestCase {

	private static final String TEST_PAGE_NAME = "employee.xhtml"; //$NON-NLS-1$
	private static final String TEXT = "veryLongNewTagNameIsTypingOrAttributeValueIsTypingUntilWeWillGetNPEExceptionOrEclipseCrashWhileRefreshingDOMTree"; //$NON-NLS-1$
	@Test
	public void testNPEinHugeFile_Jbide9827() {
		openFileAndType(18, 9);
	}
	@Test
	public void testCrashInAttribute_Jbide9997() {
		openFileAndType(18, 27);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		/*
		 *  copy big XHTML page from resources folder
		 */
		try {
			String resourceWebContentLocation = getPathToResources("WebContent"); //$NON-NLS-1$
			FileHelper.copyFilesBinaryRecursively(
					new File(resourceWebContentLocation),
					new File(FileHelper.getProjectLocation(
							JBT_TEST_PROJECT_NAME),
							"WebContent"), null);
		} catch (IOException ioe) {
			throw new RuntimeException(
					"Unable to copy necessary files from plugin's resources directory", //$NON-NLS-1$
					ioe);
		}
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).refresh();

	}

	private void openFileAndType(int line, int col) {
		/*
		 * File employee.xhtml should already be copied to WebContent, 
		 * open it.
		 */
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME)
			.getProjectItem("WebContent", TEST_PAGE_NAME)
			.open();
		TextEditor xhtmlTextEditor = new TextEditor(TEST_PAGE_NAME);
		xhtmlTextEditor.setCursorPosition(line, col);
		KeyboardFactory.getKeyboard().type(TEXT);
		/*
		 * Sleep for 20sec, wait for refresh.
		 * 10sec could be enough also.
		 */
		AbstractWait.sleep(TimePeriod.getCustom(20));
		Throwable e = getException();
		if (e != null) {
			/*
			 * Show internal stack trace 
			 */
			Writer result = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(result);
		    e.printStackTrace(printWriter);
			fail("Internal Error: " + result.toString()); //$NON-NLS-1$
		}
		/*
		 * Close the editor
		 */
		xhtmlTextEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
}

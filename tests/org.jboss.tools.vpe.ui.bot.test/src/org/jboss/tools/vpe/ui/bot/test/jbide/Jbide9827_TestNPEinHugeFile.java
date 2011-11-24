/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class Jbide9827_TestNPEinHugeFile extends VPEAutoTestCase {

	private static final String TEST_PAGE_NAME = "employee.xhtml"; //$NON-NLS-1$
	
	public Jbide9827_TestNPEinHugeFile() {
		super();
	}

	@Override
	protected void closeUnuseDialogs() { }

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
	
	public void testNPEwhenTypingTagName() {
		/*
		 *  copy big XHTML page from resources folder
		 */
		try {
			String resourceWebContentLocation = getPathToResources("WebContent"); //$NON-NLS-1$
			FileHelper.copyFilesBinaryRecursively(
					new File(resourceWebContentLocation),
					new File(FileHelper.getProjectLocation(
							JBT_TEST_PROJECT_NAME, bot),
							IDELabel.JsfProjectTree.WEB_CONTENT), null);
		} catch (IOException ioe) {
			throw new RuntimeException(
					"Unable to copy necessary files from plugin's resources directory", //$NON-NLS-1$
					ioe);
		}
		bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.REFRESH).click();
		bot.sleep(Timing.time1S());
		  /*
		   * open main page
		   */
		packageExplorer.openFile(JBT_TEST_PROJECT_NAME,
				IDELabel.JsfProjectTree.WEB_CONTENT, TEST_PAGE_NAME);
		final SWTBotEclipseEditor xhtmlTextEditor = bot.editorByTitle(TEST_PAGE_NAME).toTextEditor();
		xhtmlTextEditor.typeText(18, 9, 
			"veryLongNewTagNameIsTypingAndTypingUntilWeWillGetNPEExceptionWhileRefreshingDOMTree"); //$NON-NLS-1$
		/*
		 * Sleep for 20sec, wait for refresh.
		 * 10sec could be enough also.
		 */
		bot.sleep(Timing.time20S());
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
	}
}

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
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests Unicode Characters are properly displayed within Visual Editor
 * 
 * @author vlado pakan
 *
 */
public class UnicodeCharacterDisplayingTest extends VPEEditorTestCase {

	private static final String UNICODE_TEXT = "\u013E\u0161\u010D\u0165\u017E\u00FD\u00E1\u00ED\u00E9\u0160\u010E\u0164\u00C1\u00C9\u00D3\u00CD\u00DA\u65E5\u672C\u8A9E";

	private static final String PAGE_TEXT = "<!DOCTYPE html>\n" + "<html>\n" + "  <head>\n" + "  </head>\n"
			+ "  <body>\n" + "    " + UnicodeCharacterDisplayingTest.UNICODE_TEXT + "\n" + "  </body>\n" + "</html>";

	private static final String TEST_PAGE_NAME = "UnicodeCharacterDisplayingTest.html";

	private TextEditor htmlEditor;
	private SWTBotWebBrowser webBrowser;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createHtmlPage(UnicodeCharacterDisplayingTest.TEST_PAGE_NAME);
		htmlEditor = new TextEditor(UnicodeCharacterDisplayingTest.TEST_PAGE_NAME);
		htmlEditor.setText(UnicodeCharacterDisplayingTest.PAGE_TEXT);
		htmlEditor.save();
		new WaitWhile(new JobIsRunning());
		webBrowser = new SWTBotWebBrowser(UnicodeCharacterDisplayingTest.TEST_PAGE_NAME);
	}

	/**
	 * Check if Unicode Characters are properly displayed within Visual Editor
	 */
	@Test
	public void testUnicodeCharacterDisplaying() {
		assertVisualEditorContainsNodeWithValue(webBrowser, UnicodeCharacterDisplayingTest.UNICODE_TEXT,
				UnicodeCharacterDisplayingTest.TEST_PAGE_NAME);
	}

	@Override
	public void tearDown() throws Exception {
		htmlEditor.close();
		super.tearDown();
	}
}

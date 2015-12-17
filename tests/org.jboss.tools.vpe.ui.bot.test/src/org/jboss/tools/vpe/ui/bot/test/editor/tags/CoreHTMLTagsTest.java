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
package org.jboss.tools.vpe.ui.bot.test.editor.tags;

import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests Core HTML Tags behavior
 * 
 * @author vlado pakan
 *
 */
public class CoreHTMLTagsTest extends VPEEditorTestCase {

	private static final String PAGE_TEXT = "<!DOCTYPE html>\n" + "<html>\n"
			+ "  <body style=\"color:red; text-align:center; background-color:green\">Body tag test</body>\n"
			+ "</html>";

	private static final String TEST_PAGE_NAME = "CoreHTMLTagsTest.html";

	private TextEditor htmlEditor;
	private SWTBotWebBrowser webBrowser;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createHtmlPage(CoreHTMLTagsTest.TEST_PAGE_NAME);
		htmlEditor = new TextEditor(CoreHTMLTagsTest.TEST_PAGE_NAME);
		webBrowser = new SWTBotWebBrowser(CoreHTMLTagsTest.TEST_PAGE_NAME);
	}

	/**
	 * Tests Body Tag
	 */
	@Test
	public void testBodyTag() {

		htmlEditor.setText(CoreHTMLTagsTest.PAGE_TEXT);
		htmlEditor.save();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		assertVisualEditorContains(webBrowser, "BODY", new String[] { "style" },
				new String[] { "color: red; text-align: center; background-color: green;" },
				CoreHTMLTagsTest.TEST_PAGE_NAME);
		// check after refresh
		new DefaultToolItem(RunningPlatform.isOSX() ? "Refresh (⌘R)" : "Refresh").click();
		assertVisualEditorContains(webBrowser, "BODY", new String[] { "style" },
				new String[] { "color: red; text-align: center; background-color: green;" },
				CoreHTMLTagsTest.TEST_PAGE_NAME);

	}

	@Override
	public void tearDown() throws Exception {
		htmlEditor.close();
		super.tearDown();
	}
}

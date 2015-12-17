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

import java.io.IOException;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * Tests functionality of styles defined within page
 * 
 * @author vlado pakan
 *
 */
public class StylesOnThePageTest extends VPEEditorTestCase {

	private static final String JSP_FILE_NAME = "stylesOnThePageTest.jsp";

	@Test
	public void testStylesOnThePage() throws IOException {
		packageExplorer.open();
		packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", "pages")
				.select();
		createJspPage(StylesOnThePageTest.JSP_FILE_NAME);
		new WaitWhile(new JobIsRunning());
		TextEditor jspEditor = new TextEditor(StylesOnThePageTest.JSP_FILE_NAME);
		String oldStyle = "background: lime;color: red;";
		jspEditor.setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" + "<html>\n" + "  <head>\n"
				+ "    <style type=\"text/css\">h3 {" + oldStyle + "}</style>\n" + "  </head>\n" + "  <body>\n"
				+ "    <h3>\n" + "      Title Level 3\n" + "    </h3>\n" + "  </body>\n" + "</html>");
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		// add CSS File Reference
		new DefaultCTabItem("Preview").activate();
		Browser visualSourcePaneBrowser = new InternalBrowser(0);
		Browser previewPaneBrowser = new InternalBrowser(1);
		// Test if current style is applied on Visual/Source pane
		String textToContain = "<STYLE>h3 {" + oldStyle + "}</STYLE>";
		String browserText = visualSourcePaneBrowser.getText().toLowerCase();
		assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		textToContain = "<H3 style=\"-moz-user-modify:";
		assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		// Test if current style is applied on Preview pane
		textToContain = "<STYLE>h3 {" + oldStyle + "}</STYLE>";
		browserText = previewPaneBrowser.getText().toLowerCase();
		assertTrue("Browser on Preview pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		textToContain = "<H3 style=\"-moz-user-modify:";
		assertTrue("Browser on Preview pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		// Apply new style
		String newStyle = "background: black;color: white;";
		jspEditor.activate();
		new DefaultCTabItem("Visual/Source").activate();
		jspEditor.setText(jspEditor.getText().replaceFirst(oldStyle, newStyle));
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		new DefaultCTabItem("Preview").activate();
		// Test if current style is applied on Visual/Source pane
		textToContain = "<STYLE>h3 {" + newStyle + "}</STYLE>";
		browserText = visualSourcePaneBrowser.getText().toLowerCase();
		assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		textToContain = "<H3 style=\"-moz-user-modify:";
		assertTrue("Browser on Visual/>Source pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		// Test if current style is applied on Preview pane
		textToContain = "<STYLE>h3 {" + newStyle + "}</STYLE>";
		browserText = previewPaneBrowser.getText().toLowerCase();
		assertTrue("Browser on Preview pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		textToContain = "<H3 style=\"-moz-user-modify:";
		assertTrue("Browser on Preview pane has to contain text " + textToContain + " but it doesn't.\n"
				+ "Browser text is:\n" + browserText, browserText.contains(textToContain.toLowerCase()));
		jspEditor.activate();
		new DefaultCTabItem("Visual/Source").activate();
	}

}

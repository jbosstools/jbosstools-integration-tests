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

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Tests large XHTML file editing
 * 
 * @author vlado pakan
 *
 */
public class XhtmlFilePerformanceTest extends VPEAutoTestCase {

	private static final String TEST_PAGE_NAME = "employee.xhtml";

	@Test
	public void testXhtmlFilePerformance() {
		// copy big XHTML page from resources folder
		try {
			String resourceWebContentLocation = getPathToResources("WebContent");
			FileHelper.copyFilesBinaryRecursively(new File(resourceWebContentLocation),
					new File(FileHelper.getProjectLocation(JBT_TEST_PROJECT_NAME), "WebContent"), null);
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to copy necessary files from plugin's resources directory", ioe);
		}
		new WorkbenchShell().maximize();
		// open main page
		ProjectItem piTestPage = packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent",
				XhtmlFilePerformanceTest.TEST_PAGE_NAME);
		piTestPage.refresh();
		piTestPage.open();
		final TextEditor xhtmlTextEditor = new TextEditor(XhtmlFilePerformanceTest.TEST_PAGE_NAME);
		String insertText = "!!!123 Test Title Inserted 321!!!";
		String origText = xhtmlTextEditor.getText();
		xhtmlTextEditor.selectText("<h1>");
		xhtmlTextEditor.insertText(xhtmlTextEditor.getPositionOfText("<h1>"), "<h1>" + insertText + "</h1>");
		xhtmlTextEditor.save();
		new DefaultToolItem(RunningPlatform.isOSX() ? "Refresh (⌘R)" : "Refresh").click();
		new DefaultCTabItem("Preview").activate();
		new DefaultCTabItem("Visual/Source").activate();
		// 5 seconds should be enough time for synchronizing view
		AbstractWait.sleep(TimePeriod.getCustom(5));
		SWTBotWebBrowser swtBotWebBrowserExt = new SWTBotWebBrowser(XhtmlFilePerformanceTest.TEST_PAGE_NAME);
		boolean isOK = swtBotWebBrowserExt.containsNodeWithValue(swtBotWebBrowserExt.getNsIDOMDocument(), insertText);
		xhtmlTextEditor.setText(origText);
		xhtmlTextEditor.save();
		xhtmlTextEditor.close();
		assertTrue("Web Browser has to contain text " + insertText + " but it doesn't.", isOK);

	}

}

/******************************************************************************* 
 * Copyright (c) 2012 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.jboss.tools.vpe.reddeer.view.JBTPaletteView;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.junit.Test;

/**
 * Prompt for Tag Attributes during Tag Insert test
 * 
 * @author vlado pakan
 *
 */
public class PromptForTagAttributesDuringTagInsertTest extends PreferencesTestCase {

	private static final String TEST_PAGE_NAME = "PromptForTagAttributes.jsp";
	private TextEditor jspEditor;
	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		createJspPage(PromptForTagAttributesDuringTagInsertTest.TEST_PAGE_NAME);
		jspEditor = new TextEditor(PromptForTagAttributesDuringTagInsertTest.TEST_PAGE_NAME);
	}

	/**
	 * Prompt for Tag Attributes during Tag Insert test
	 */
	@Test
	public void testPromptForTagAttributesDuringTagInsert() {
		new JBTPaletteView().open();
		jspEditor.activate();
		jspEditor.setText("");
		jspEditor.save();
		new WaitWhile(new JobIsRunning());
		// Check Ask for Tag Attributes during Insert
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		new VisualPageEditorPreferencePage().toggleAskForAttrsDuringTagInsert(true);
		new OkButton().click();
		SWTBotWebBrowser.activatePaletteTool("outputText");
		boolean dialogWasOpened = false;
		try {
			new DefaultShell("Insert Tag");
			dialogWasOpened = true;
			new FinishButton().click();
		} catch (SWTLayerException sle) {
			// do nothing
		}
		assertTrue("Dialog asking for Tag Attributes during Insert was not opened but it has to\nhttps://issues.jboss.org/browse/JBIDE-20752",
				dialogWasOpened);
		// Uncheck Ask for Tag Attributes during Insert
		new DefaultToolItem("Preferences").click();
		new DefaultShell("Preferences (Filtered)");
		new VisualPageEditorPreferencePage().toggleAskForAttrsDuringTagInsert(false);
		new OkButton().click();
		SWTBotWebBrowser.activatePaletteTool("outputText");
		dialogWasOpened = false;
		try {
			new DefaultShell("Insert Tag");
			dialogWasOpened = true;
			new FinishButton().click();
		} catch (SWTLayerException sle) {
			// do nothing
		}
		assertFalse("Dialog asking for Tag Attributes during Insert was opened but it must not to be.", dialogWasOpened);
	}

}

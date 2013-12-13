/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.jst.web.ui.internal.editor.messages.JstUIMessages;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class ExternalizeAllStringsDialogTest extends VPEAutoTestCase {

	private boolean isUnusedDialogOpened = false;
	
	@Override
	protected void closeUnuseDialogs() {
		try {
			SWTBotShell dlgShell = bot.shell(JstUIMessages.EXTERNALIZE_STRINGS_DIALOG_TITLE);
			dlgShell.setFocus();
			dlgShell.close();
		} catch (WidgetNotFoundException e) {
			e.printStackTrace();
		} finally {
			isUnusedDialogOpened = false;
		}
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return isUnusedDialogOpened;
	}
	
	public void _testExternalizeAllStringsDialog() throws Throwable {
		/*
		 * Open the file
		 */
		isUnusedDialogOpened = false;
		SWTBotEditor editor = SWTTestExt.packageExplorer.openFile(JBT_TEST_PROJECT_NAME,
				"WebContent", "pages", TEST_PAGE); //$NON-NLS-1$ //$NON-NLS-2$
		editor.setFocus();
		/*
		 * Open the dialog for the active editor
		 */

		/*
		 * Select example bundle 
		 */
		
		/*
		 * Check displayed properties
		 */
		
		/*
		 * Check next page:
		 * the table with non-externalized properties
		 */
		
		/*
		 * Check duplicate keys
		 */
		
		/*
		 * Check bundle name on the next page:
		 */
		
		/*
		 * Press OK
		 */
		
		/*
		 * Check the editor content, string should be replaced
		 */
		
	}

}

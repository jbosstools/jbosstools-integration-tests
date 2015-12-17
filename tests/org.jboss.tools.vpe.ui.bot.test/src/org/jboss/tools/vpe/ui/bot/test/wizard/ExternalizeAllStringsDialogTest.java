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
package org.jboss.tools.vpe.ui.bot.test.wizard;

import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;

public class ExternalizeAllStringsDialogTest extends VPEAutoTestCase {

	public void _testExternalizeAllStringsDialog() throws Throwable {
		/*
		 * Open the file
		 */
		SWTTestExt.packageExplorer.getProject(JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", "pages", TEST_PAGE).open();
		new DefaultEditor(TEST_PAGE).activate();
		
	}

}

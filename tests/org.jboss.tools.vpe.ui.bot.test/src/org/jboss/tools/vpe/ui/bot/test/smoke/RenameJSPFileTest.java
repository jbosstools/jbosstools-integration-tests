/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.jboss.tools.ui.bot.ext.helper.FileRenameHelper;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test renaming of JSP file
 * 
 * @author Vladimir Pakan
 *
 */
public class RenameJSPFileTest extends VPEEditorTestCase {

	private static final String NEW_JSP_FILE_NAME = "renamed-" + JSPPageCreationTest.TEST_NEW_JSP_FILE_NAME;
	
	@Test
	public void testRenameJSPFile() throws Throwable {
		checkRenameJSPFile();
	}

	private void checkRenameJSPFile() {
		String checkResult = FileRenameHelper.checkFileRenamingWithinWebProjects(
				JSPPageCreationTest.TEST_NEW_JSP_FILE_NAME, NEW_JSP_FILE_NAME,
				new String[] { JBT_TEST_PROJECT_NAME, "WebContent", "pages" });
		assertNull(checkResult, checkResult);

	}

}
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

import org.jboss.tools.vpe.reddeer.utils.FileRenameHelper;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test renaming of XHTML file
 * 
 * @author Vladimir Pakan
 * 
 */
public class RenameXHTMLFileTest extends VPEEditorTestCase {

	private static final String NEW_XHTML_FILE_NAME = "renamed-" + XHTMLPageCreationTest.TEST_NEW_XHTML_FILE_NAME;
	@Test
	public void testRenameXHTMLFile() throws Throwable {
		checkRenameXHTMLFile();
	}

	/**
	 * Check renaming of XHTML file
	 */
	private void checkRenameXHTMLFile() {
		String checkResult = FileRenameHelper.checkFileRenamingWithinWebProjects(
				XHTMLPageCreationTest.TEST_NEW_XHTML_FILE_NAME, NEW_XHTML_FILE_NAME, new String[] {
						JBT_TEST_PROJECT_NAME, "WebContent", "pages" });
		assertNull(checkResult, checkResult);
	}
}
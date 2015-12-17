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
import org.jboss.reddeer.core.handler.TreeItemHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.tools.jst.reddeer.web.ui.navigator.WebProjectsNavigator;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.junit.Test;

/**
 * Test renaming of faces-config.xml file
 * 
 * @author Vladimir Pakan
 *
 */
public class RenameFacesConfigFileTest extends VPEEditorTestCase {

	private static final String NEW_FACES_CONFIG_FILE_NAME = "faces-config-renamed.xml";
	private static final String OLD_FACES_CONFIG_FILE_NAME = "faces-config.xml";

	@Test
	public void testRenameFacesConfigFile() throws Throwable {
		checkRenameFacesConfigFile();
	}

	/**
	 * Check renaming of faces-config.xml file
	 */
	private void checkRenameFacesConfigFile() {

		WebProjectsNavigator webProjectsNavigator = new WebProjectsNavigator();
		webProjectsNavigator.open();
		String checkResult = FileRenameHelper.checkFileRenamingWithinWebProjects(OLD_FACES_CONFIG_FILE_NAME,
				NEW_FACES_CONFIG_FILE_NAME, new String[] { JBT_TEST_PROJECT_NAME, "Configuration" });
		assertNull(checkResult, checkResult);
		// web.xml file was properly modified
		TreeItem configFilesTreeItem = webProjectsNavigator.getProject(JBT_TEST_PROJECT_NAME)
				.getProjectItem("web.xml", "Context Params", "javax.faces.CONFIG_FILES").getTreeItem();
		configFilesTreeItem.select();
		TreeItemHandler.getInstance().click(configFilesTreeItem.getSWTWidget());
		new ContextMenu("Properties").select();
		new DefaultShell("Properties");
		String fullConfigFileName = new DefaultTableItem("Param-Value").getText(1);
		new PushButton("Close").click();
		assertTrue(
				NEW_FACES_CONFIG_FILE_NAME + " Name of " + OLD_FACES_CONFIG_FILE_NAME
						+ " file was not changed in web.xml file.",
				fullConfigFileName.endsWith(NEW_FACES_CONFIG_FILE_NAME));
		// put changes back
		checkResult = FileRenameHelper.checkFileRenamingWithinWebProjects(NEW_FACES_CONFIG_FILE_NAME,
				OLD_FACES_CONFIG_FILE_NAME,
				new String[] { JBT_TEST_PROJECT_NAME, "Configuration"});
		assertNull(checkResult, checkResult);
	}
}
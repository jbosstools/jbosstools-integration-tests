/******************************************************************************* 
 * Copyright (c) 2014-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.rhmap;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.aerogear.ui.bot.test.FeedHenryBotTest;
import org.jboss.reddeer.core.util.FileUtil;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.junit.BeforeClass;
import org.junit.Test;

@CleanWorkspace
public class ImportFHApp extends FeedHenryBotTest {

	@BeforeClass
	public static void importApp() {
		importApp(FH_PROJECT, FH_APP_NAME);
	}

	@Test
	public void testAppIsImported() {
		assertTrue(new ProjectExplorer().containsProject(FH_APP_NAME));
	}

	@Test
	public void testConfigXmlOpenedInEditor() {
		DefaultEditor configEditor = new DefaultEditor();
		assertTrue(configEditor.isActive());
		assertTrue(configEditor.getTitle().equals(FH_APP_NAME));
	}

	@Test
	public void testProjectHasHybridAppNature() throws IOException {
		String projectConfig = FileUtil.readFile(WS_PATH + "/" + FH_APP_NAME + "/.project");
		assertTrue(projectConfig.contains("<nature>org.eclipse.thym.core.HybridAppNature</nature>"));
	}

	@Test
	public void testProjectHasJSNature() throws IOException {
		String projectConfig = FileUtil.readFile(WS_PATH + "/" + FH_APP_NAME + "/.project");
		assertTrue(projectConfig.contains("<nature>org.eclipse.wst.jsdt.core.jsNature</nature>"));
	}

}

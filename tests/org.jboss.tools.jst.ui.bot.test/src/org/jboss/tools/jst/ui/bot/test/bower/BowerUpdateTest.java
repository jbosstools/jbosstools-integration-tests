/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.ui.bot.test.bower;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author Pavol Srna
 * @author Ilya Buziuk
 *
 */
public class BowerUpdateTest extends JSTTestBase {

	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
	}

	@After
	public void cleanup() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBowerUpdateShortcutAvailability() {
		bowerInit(PROJECT_NAME);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		assertTrue("Bower Update is not available", //$NON-NLS-1$
				new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Bower Update)")).isEnabled()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testDependeciesDownload() throws IOException {
		/* create bower.json */
		String content = "{\"name\": \"testProject\",\"dependencies\":{\"angularjs\": \"1.4.4\"}}";
		File bowerJson = new File(BASE_DIRECTORY + "/bower.json");
		FileWriter fw = new FileWriter(bowerJson, false);
		fw.write(content);
		fw.flush();
		fw.close();
		assertTrue(bowerJson.exists());
		new ProjectExplorer().getProject(PROJECT_NAME).refresh();
		bowerUpdate(PROJECT_NAME);
		new WaitUntil(new ConsoleHasText("angularjs#1.4.4 bower_components/angularjs"));
		assertTrue("BowerUpdate failed, dependencies not found in tree", new ProjectExplorer().getProject(PROJECT_NAME)
				.containsItem("bower_components", "angularjs", "angular.js"));
	}

}

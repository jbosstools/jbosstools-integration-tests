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
package org.jboss.tools.jst.ui.bot.test.npm;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.core.handler.ShellHandler;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Pavol Srna
 *
 */
public class NpmShortcutsTest extends JSTTestBase {

	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
	}

	@After
	public void cleanup() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNpmUpdateShortcutAvailability() {
		npmInit(PROJECT_NAME);
		PackageExplorerPart pe = new PackageExplorerPart();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		assertTrue("npm Update is not available", //$NON-NLS-1$
				new ContextMenuItem(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( npm Update)")).isEnabled()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNpmInstallShortcutAvailability() {
		npmInit(PROJECT_NAME);
		PackageExplorerPart pe = new PackageExplorerPart();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		assertTrue("npm Install is not available", //$NON-NLS-1$
				new ContextMenuItem(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( npm Install)")).isEnabled()); //$NON-NLS-1$ //$NON-NLS-2$
	}

}

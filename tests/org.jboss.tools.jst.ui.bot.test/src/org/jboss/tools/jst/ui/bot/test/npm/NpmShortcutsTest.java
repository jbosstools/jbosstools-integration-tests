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

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Pavol Srna
 *
 */
public class NpmShortcutsTest extends NpmTestBase {

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
	public void testNpmUpdateShortcutAvailability() {
		npmInit(PROJECT_NAME);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		assertTrue("npm Update is not available", //$NON-NLS-1$
				new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( npm Update)")).isEnabled()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNpmInstallShortcutAvailability() {
		npmInit(PROJECT_NAME);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).select();
		assertTrue("npm Install is not available", //$NON-NLS-1$
				new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( npm Install)")).isEnabled()); //$NON-NLS-1$ //$NON-NLS-2$
	}

}

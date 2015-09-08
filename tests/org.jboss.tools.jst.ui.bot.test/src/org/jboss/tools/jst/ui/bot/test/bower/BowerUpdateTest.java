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

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BowerUpdateTest extends BowerTestBase {

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

}

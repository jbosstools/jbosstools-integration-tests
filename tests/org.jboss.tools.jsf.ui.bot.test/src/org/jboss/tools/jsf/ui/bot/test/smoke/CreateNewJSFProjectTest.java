/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.ui.bot.test.smoke;

import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.junit.Test;

/**
 * Test adding new JSF Project
 * 
 * @author Vladimir Pakan
 *
 */
public class CreateNewJSFProjectTest extends JSFAutoTestCase {
	@Test
	public void testCreateNewJSFProject() {
		// Test creates new JSF Project
		// New JSF Project is created via setUp() method of super class VPEAutoTestCase.java
		packageExplorer.open();
		assertTrue("Project " + JBT_TEST_PROJECT_NAME + " was not created properly.",
				packageExplorer.containsProject(JBT_TEST_PROJECT_NAME));
	}

}

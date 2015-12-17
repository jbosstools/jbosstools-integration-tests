/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.browsersim;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.junit.Test;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.jboss.tools.common.reddeer.perspectives.JBossPerspective;
/**
 * Test opening of Browsersim within JBoss Perspective
 * @author Vladimir Pakan
 *
 */
@OpenPerspective(JBossPerspective.class)
public class OpenBrowserSimTest{
	public static Logger log = Logger.getLogger(OpenBrowserSimTest.class);

	/**
	 * Opens and closes BrowserSim
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@Test
	public void testOpenBrowserSim() throws IllegalArgumentException,
			IllegalAccessException {
		final String browserSimmProcessName = "BrowserSimRunner";
		int countBrowserSimmProcesses = SWTUtilExt
				.countJavaProcess(browserSimmProcessName);
		// this also asserts that BrowserSim runs without error within JBT
		new DefaultToolItem(new WorkbenchShell(),"Run BrowserSim").click();
		assertTrue("No new BrowserSim process was started",
				countBrowserSimmProcesses + 1 == SWTUtilExt
						.countJavaProcess(browserSimmProcessName));
		BrowserSimHandler.closeAllRunningInstances();
	}
}
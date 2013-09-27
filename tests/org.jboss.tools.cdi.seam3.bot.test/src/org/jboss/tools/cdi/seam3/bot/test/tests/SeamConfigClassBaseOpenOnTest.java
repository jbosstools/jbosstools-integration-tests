/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.seam3.bot.test.tests;

import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 * 
 */
public class SeamConfigClassBaseOpenOnTest extends Seam3TestBase {

	private static final String projectName = "seamConfigOpenOn";
	private static final String SEAM_CONFIG = "seam-beans.xml";

	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
		disableSourceLookup();
	}

	@Before
	public void openSeamConfig() {
		packageExplorer.openFile(projectName, 
				IDELabel.WebProjectsTree.WEB_CONTENT,
				IDELabel.WebProjectsTree.WEB_INF, 
				SEAM_CONFIG).toTextEditor();
		bot.cTabItem("Source").activate();
	}

	@Test
	public void testBeanOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "b:Bean1",
				"Bean1.java");

		/* test opened object */
		assertExpectedSelection("Bean1");
	}

	@Test
	public void testConstructorOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:parameters",
				"Bean1.java");

		/* test opened object */
		assertExpectedSelection("Bean1");
	}

	@Test
	public void testFieldOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "b:value",
				"Bean1.java");

		/* test opened object */
		assertExpectedSelection("value");
	}

	@Test
	public void testMethodOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "b:method",
				"Bean1.java");

		/* test opened object */
		assertExpectedSelection("method");
	}

	private void assertExpectedSelection(String selectedString) {
		assertEquals(selectedString, bot.activeEditor().toTextEditor()
				.getSelection());
	}

}

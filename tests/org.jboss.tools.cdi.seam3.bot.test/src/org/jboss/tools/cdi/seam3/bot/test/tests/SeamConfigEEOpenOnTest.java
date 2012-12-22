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

import org.jboss.tools.cdi.bot.test.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 * 
 */
public class SeamConfigEEOpenOnTest extends Seam3TestBase {

	private static String projectName = "seamConfigEEOpenOn";
	private static final String SEAM_CONFIG = "seam-beans.xml";

	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
	}

	@Before
	public void openSeamConfig() {
		packageExplorer.openFile(projectName, CDIConstants.WEBCONTENT,
				CDIConstants.WEB_INF, SEAM_CONFIG).toTextEditor();
		bot.cTabItem("Source").activate();
	}

	@Test
	public void testAlternativeOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Alternative",
				"Alternative.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Alternative");

	}

	@Test
	public void testDecoratorOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Decorator",
				"Decorator.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.decorator.Decorator");

	}

	@Test
	public void testInjectOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Inject",
				"Inject.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.inject.Inject");

	}

	@Test
	public void testInterceptorOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Interceptor",
				"Interceptor.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.interceptor.Interceptor");

	}

	@Test
	public void testInterceptorBindingOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG,
				"s:InterceptorBinding", "InterceptorBinding.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.interceptor.InterceptorBinding");

	}

	@Test
	public void testObservesOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Observes",
				"Observes.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.event.Observes");

	}

	@Test
	public void testProducesOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Produces",
				"Produces.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Produces");

	}

	@Test
	public void testQualifierOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Qualifier",
				"Qualifier.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.inject.Qualifier");

	}

	@Test
	public void testSpecializesOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Specializes",
				"Specializes.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Specializes");

	}

	@Test
	public void testStereotypeOpenOn() {

		/* open on bean class */
		OpenOnHelper.checkOpenOnFileIsOpened(bot, SEAM_CONFIG, "s:Stereotype",
				"Stereotype.class");

		/* test opened object */
		assertExpectedOpenedClass("javax.enterprise.inject.Stereotype");

	}

	private void assertExpectedOpenedClass(String packageName) {
		assertContains(packageName, bot.activeEditor().toTextEditor().getText());
	}

}

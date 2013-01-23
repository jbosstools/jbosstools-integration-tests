/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;

import org.jboss.reddeer.eclipse.jface.preference.FoldingPreferencePage;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	ValidationsInPreferenceTest.class,
	ExcludesAnnotationTest.class,
	ConfigPropertyAnnotationTest.class,
	MessageBundleAnnotationTest.class,
	MessageContextAnnotationTest.class,
	ExceptionHandlerAnnotationTest.class,
	ExceptionHandlerMethodsTest.class,
	SecuresAnnotationTest.class,
	SecuredAnnotationTest.class
})
public class DeltaspikeAllBotTests {
	
	@BeforeClass
	public static void disableFolding() {
		FoldingPreferencePage foldingPreferecePage = 
				new FoldingPreferencePage();
		foldingPreferecePage.open();
		foldingPreferecePage.disableFolding();
		foldingPreferecePage.ok();
	}
	
}

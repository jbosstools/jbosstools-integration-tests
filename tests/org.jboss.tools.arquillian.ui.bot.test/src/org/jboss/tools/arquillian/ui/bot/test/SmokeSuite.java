/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/

package org.jboss.tools.arquillian.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.arquillian.ui.bot.test.cruiserView.BasicArquilliaCruiserTest;
import org.jboss.tools.arquillian.ui.bot.test.preferences.ArquillianPreferencePageTest;
import org.jboss.tools.arquillian.ui.bot.test.preferences.ArquillianValidatorPreferencePageTest;
import org.jboss.tools.arquillian.ui.bot.test.project.AddArquillianProfile;
import org.jboss.tools.arquillian.ui.bot.test.project.CreateArquillianProject;
import org.jboss.tools.arquillian.ui.bot.test.testcase.CreateArquillianTestCase;
import org.jboss.tools.arquillian.ui.bot.test.testcase.RunArquillianTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	CreateArquillianProject.class,
	AddArquillianProfile.class,
	CreateArquillianTestCase.class,
	RunArquillianTestCase.class,
	ArquillianPreferencePageTest.class,
	ArquillianValidatorPreferencePageTest.class
	})
public class SmokeSuite {
	
}

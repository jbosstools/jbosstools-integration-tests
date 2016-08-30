/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.vpe.bot.test.html5.CreateHTML5Page;
import org.jboss.tools.vpe.bot.test.html5.HTML5EditorHighlight;
import org.jboss.tools.vpe.bot.test.html5.jquery.MultiPageNavigation;
import org.jboss.tools.vpe.bot.test.jsf.JSFEngineTest;
import org.jboss.tools.vpe.bot.test.preferences.BrowserEnginePreferencesTest;
import org.jboss.tools.vpe.bot.test.preferences.VpvPreferencesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	CreateHTML5Page.class,
	HTML5EditorHighlight.class,
	MultiPageNavigation.class,
	VpvPreferencesTest.class,
	BrowserEnginePreferencesTest.class,
	JSFEngineTest.class
})
public class VPEAllTests {

}

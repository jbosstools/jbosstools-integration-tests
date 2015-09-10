/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.vpe.ui.bot.test.browsersim.OpenBrowserSimTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CoreHTMLTagsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
/**
 * Smoke test suite testing Visual Page Editor and BrowserSim
 * @author vlado pakan
 *
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	OpenBrowserSimTest.class
	// ,CoreHTMLTagsTest.class
	})

public class SmokeSuite extends SWTBotTestCase{

}

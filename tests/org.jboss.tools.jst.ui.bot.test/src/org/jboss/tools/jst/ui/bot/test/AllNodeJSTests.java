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

package org.jboss.tools.jst.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.jst.ui.bot.test.nodejs.NodeJSDebuggerTest;
import org.jboss.tools.jst.ui.bot.test.nodejs.NodeJSLauncherTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	// Debugger Tests
	NodeJSLauncherTest.class,
	NodeJSDebuggerTest.class
})
public class AllNodeJSTests {

}

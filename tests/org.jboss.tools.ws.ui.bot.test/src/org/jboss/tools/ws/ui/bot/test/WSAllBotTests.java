 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ws.ui.bot.test.jbt.SampleWSTest;
import org.jboss.tools.ws.ui.bot.test.wtp.BottomUpWSTest;
import org.jboss.tools.ws.ui.bot.test.wtp.TopDownWSTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({	
	SampleWSTest.class,
	BottomUpWSTest.class,
	TopDownWSTest.class	
	})
@RunWith(RequirementAwareSuite.class)
public class WSAllBotTests {

}

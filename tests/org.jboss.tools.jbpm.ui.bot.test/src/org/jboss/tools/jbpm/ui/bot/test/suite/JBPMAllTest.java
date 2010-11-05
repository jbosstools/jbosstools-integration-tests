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
package org.jboss.tools.jbpm.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.jbpm.ui.bot.test.GPDTest;
import org.jboss.tools.jbpm.ui.bot.test.JBPMProjectTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

// @SuiteClasses({ JBPMProjectTest.class, JBPMDeployTest.class  })
// @SuiteClasses({ JBPMViewsTest.class  })
@SuiteClasses({ JBPMProjectTest.class, GPDTest.class  })
@RunWith(RequirementAwareSuite.class)
public class JBPMAllTest extends TestSuite {

	@BeforeClass
	public static void setUpSuite() {
		JBPMTest.prepare();
	}

	@AfterClass
	public static void tearDownSuite() {
		JBPMTest.clean();
	}
}

/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.openshift.ui.bot.test.connection.ID101OpenOpenShiftExplorerTest;
import org.jboss.tools.openshift.ui.bot.test.connection.ID102OpenNewConnectionShellTest;
import org.jboss.tools.openshift.ui.bot.test.connection.v3.ConnectionWizardHandlingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	// OpenShift v2 smoke tests
	ID101OpenOpenShiftExplorerTest.class,
	ID102OpenNewConnectionShellTest.class,
	
	// OpenShift v3 smoke tests
	ConnectionWizardHandlingTest.class
})
public class OpenShiftSmokeBotTests {

}

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

import org.jboss.tools.jbpm.ui.bot.test.BPMNConvertCase;
import org.jboss.tools.jbpm.ui.bot.test.GPDPaletteTest;
import org.jboss.tools.jbpm.ui.bot.test.GPDTest;
import org.jboss.tools.jbpm.ui.bot.test.JBPMDeployTest;
import org.jboss.tools.jbpm.ui.bot.test.JBPMProjectTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
	JBPMProjectTest.class, 
	GPDTest.class, 
	GPDPaletteTest.class,
	JBPMDeployTest.class, 
	BPMNConvertCase.class 
})
@RunWith(JBPMSuite.class)
public class AllTests extends TestSuite {

}

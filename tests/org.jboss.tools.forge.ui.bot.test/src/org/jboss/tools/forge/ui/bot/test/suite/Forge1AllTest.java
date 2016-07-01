/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.forge.ui.bot.console.test.CommandTest;
import org.jboss.tools.forge.ui.bot.console.test.EntityTest;
import org.jboss.tools.forge.ui.bot.console.test.InstallPluginTest;
import org.jboss.tools.forge.ui.bot.console.test.PersistenceTest;
import org.jboss.tools.forge.ui.bot.console.test.ProjectTest;
import org.jboss.tools.forge.ui.bot.console.test.ScaffoldingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * 
 * @author psrna
 *
 */
@SuiteClasses({
	ProjectTest.class,
	PersistenceTest.class,
	EntityTest.class,
	InstallPluginTest.class,
	ScaffoldingTest.class,
	CommandTest.class,
})
@RunWith(RedDeerSuite.class)
public class Forge1AllTest {
}

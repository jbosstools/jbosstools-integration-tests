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

package org.jboss.tools.docker.ui.bot.test;


import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.docker.ui.bot.test.connection.AddConnectionTest;
import org.jboss.tools.docker.ui.bot.test.ui.ContainerTabTest;
import org.jboss.tools.docker.ui.bot.test.ui.DeleteAllTest;
import org.jboss.tools.docker.ui.bot.test.ui.DockerContainerTest;
import org.jboss.tools.docker.ui.bot.test.ui.PerspectiveTest;
import org.jboss.tools.docker.ui.bot.test.ui.PullImageTest;
import org.jboss.tools.docker.ui.bot.test.ui.ImageTabTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * This is a swtbot testcase for an eclipse application.
 * 
 * @author jkopriva
 */
@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	PerspectiveTest.class, 
	AddConnectionTest.class,
	PullImageTest.class,
	DockerContainerTest.class,
	ImageTabTest.class,			
	ContainerTabTest.class,		
	DeleteAllTest.class
	
})
public class DockerAllBotTest {

}

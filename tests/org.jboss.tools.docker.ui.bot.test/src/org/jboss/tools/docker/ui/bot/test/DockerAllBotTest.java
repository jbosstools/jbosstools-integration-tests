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
import org.jboss.tools.docker.ui.bot.test.container.DockerContainerTest;
import org.jboss.tools.docker.ui.bot.test.container.ExposePortTest;
import org.jboss.tools.docker.ui.bot.test.container.LabelsTest;
import org.jboss.tools.docker.ui.bot.test.container.LinkContainersTest;
import org.jboss.tools.docker.ui.bot.test.container.PrivilegedModeTest;
import org.jboss.tools.docker.ui.bot.test.container.VariablesTest;
import org.jboss.tools.docker.ui.bot.test.container.VolumeMountTest;
import org.jboss.tools.docker.ui.bot.test.image.BuildImageTest;
import org.jboss.tools.docker.ui.bot.test.image.DeleteImagesAfter;
import org.jboss.tools.docker.ui.bot.test.image.HierarchyViewTest;
import org.jboss.tools.docker.ui.bot.test.image.ImageTagTest;
import org.jboss.tools.docker.ui.bot.test.image.PullImageTest;
import org.jboss.tools.docker.ui.bot.test.image.PushImageTest;
import org.jboss.tools.docker.ui.bot.test.ui.ComposeTest;
import org.jboss.tools.docker.ui.bot.test.ui.ContainerTabTest;
import org.jboss.tools.docker.ui.bot.test.ui.DifferentRegistryTest;
import org.jboss.tools.docker.ui.bot.test.ui.PerspectiveTest;
import org.jboss.tools.docker.ui.bot.test.ui.PropertiesViewTest;
import org.jboss.tools.docker.ui.bot.test.ui.SearchDialogTest;
import org.jboss.tools.docker.ui.bot.test.ui.ImageTabTest;
import org.jboss.tools.docker.ui.bot.test.ui.LaunchDockerImageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 
 * This is a RedDeer test case for an eclipse application.
 * 
 * @author jkopriva
 */

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	PerspectiveTest.class, 
	AddConnectionTest.class,
	BuildImageTest.class,
	PullImageTest.class,
	DockerContainerTest.class,
	ExposePortTest.class,
	ImageTabTest.class,
	ContainerTabTest.class,
	VolumeMountTest.class,
	PrivilegedModeTest.class,
	VariablesTest.class,
	LinkContainersTest.class,
	DifferentRegistryTest.class,
	SearchDialogTest.class,
	ImageTagTest.class,
	LabelsTest.class,
	HierarchyViewTest.class,
	PropertiesViewTest.class,
	PushImageTest.class,
	LaunchDockerImageTest.class,
	ComposeTest.class,
	
	DeleteImagesAfter.class
})
public class DockerAllBotTest {

}

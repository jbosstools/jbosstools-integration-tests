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

package org.jboss.tools.docker.ui.bot.test.ui;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class DifferentRegistryTest extends AbstractDockerBotTest {
	private String serverAddress = "registry.access.redhat.com";
	private String email = "test@test.com";
	private String userName = "test";
	private String password = "password";
	private String imageName = "devstudio/atomicapp:latest";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testDifferentRegistry() {
		ConsoleView cview = new ConsoleView();
		cview.open();
		try {
			cview.clearConsole();
		} catch (CoreLayerException ex) {
			// there's not clear console button, since nothing run before
		}
		setUpRegister(serverAddress, email, userName, password);
		setSecureStorage(this.password);
		pullImage(imageName, null, this.userName + "@" + serverAddress);
		assertTrue("Image is not deployed!", imageIsDeployed("devstudio/atomicapp"));
	}

	@After
	public void after() {
		deleteImageContainerAfter(serverAddress + "/devstudio/atomicapp");
		deleteConnection();
		deleteRegister(serverAddress);
	}

}

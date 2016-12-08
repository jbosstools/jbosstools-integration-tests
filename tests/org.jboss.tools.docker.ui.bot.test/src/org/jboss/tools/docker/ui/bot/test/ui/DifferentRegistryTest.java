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

import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jkopriva
 * @contributor adietish@redhat.com
 */
public class DifferentRegistryTest extends AbstractImageBotTest {

	private static final String REGISTRY_SERVER_ADDRESS = "registry.access.redhat.com";
	private static final String EMAIL = "test@test.com";
	private static final String USERNAME = "test";
	private static final String PASSWORD = "password";
	private static final String IMAGE_NAME = "rhel7.2";

	@Before
	public void before() {
		deleteImageIfExists(REGISTRY_SERVER_ADDRESS + "/" + IMAGE_NAME);
		deleteRegisterIfExists(REGISTRY_SERVER_ADDRESS);
	}

	@Test
	public void testDifferentRegistry() {
		clearConsole();
		setUpRegister(REGISTRY_SERVER_ADDRESS, EMAIL, USERNAME, PASSWORD);
		setSecureStorage(PASSWORD);
		pullImage(IMAGE_NAME, null, USERNAME + "@" + REGISTRY_SERVER_ADDRESS);
		assertTrue("Image is not deployed!", imageIsDeployed(IMAGE_NAME));
	}

}

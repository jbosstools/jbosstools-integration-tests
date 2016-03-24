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

import static org.junit.Assert.fail;

import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.tools.docker.reddeer.perspective.DockerPerspective;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class PerspectiveTest extends AbstractDockerBotTest {

	@Test
	public void testDockerToolingPerspective() {
		new DockerPerspective().open();
		try{
			new ShellWithTextIsActive("Docker Explorer");
		} catch (SWTLayerException ex){
			fail("Docker Explorer not found in Docker tooling perspective");
		}
	}

}

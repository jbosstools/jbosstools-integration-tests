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

import org.jboss.tools.docker.reddeer.ui.DockerContainersTab;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class PerspectiveTest extends AbstractDockerBotTest {

	@Test
	public void testPerspective() {
		openDockerPerspective();
	}
	
	@Test
	public void testDockerExplorerViewPresent() {
		openDockerPerspective();
		new DockerExplorerView().open();
	}
	
	@Test
	public void testDockerImagesTabPresent() {
		openDockerPerspective();
		DockerImagesTab tab = new DockerImagesTab();
		tab.open();
		tab.refresh();
	}
	
	@Test
	public void testDockerContainersTabPresent() {
		openDockerPerspective();
		DockerContainersTab tab = new DockerContainersTab();
		tab.open();
		tab.refresh();
	}

	@After
	public void clear() {
		cleanupShells();
	}

}

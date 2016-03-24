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

package org.jboss.tools.docker.ui.bot.test.connection;

import org.jboss.tools.docker.reddeer.core.ui.wizards.DockerConnectionWizard;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.Test;

public class AddConnectionTest extends AbstractDockerBotTest {
	
	
	@Test
	public void addConnectionTest(){
		String dockerServerURI = System.getProperty("dockerServerURI");
		createConnection(dockerServerURI);
	}
	
	private void createConnection(String dockerServer){
		DockerConnectionWizard connectionWizard = new DockerConnectionWizard();
		connectionWizard.open();
		connectionWizard.setTcpConnection(dockerServer);
		connectionWizard.finish();
	}
	
	

}

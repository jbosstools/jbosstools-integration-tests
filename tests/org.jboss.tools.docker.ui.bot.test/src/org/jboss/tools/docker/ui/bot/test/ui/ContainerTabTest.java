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

import java.util.ArrayList;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.tools.docker.reddeer.ui.DockerContainersTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.Test;


/**
 * 
 * @author jkopriva
 *
 */


public class ContainerTabTest extends AbstractDockerBotTest {

	@Test
	public void ContainerTabTest() {
		DockerContainersTab containerTab = new DockerContainersTab();
		String imageName = System.getProperty("imageName");
		String dockerServerURI = System.getProperty("dockerServerURI");
		containerTab.activate();
		containerTab.refresh();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);

		String nameFromTable = "";

		for (TableItem item : containerTab.getTableItems()) {
			if (item.getText(1).startsWith(imageName)) {
				nameFromTable = item.getText();
			}
		}

		ArrayList<String> namesConsole = getIds(executeCommand("docker -H " + dockerServerURI + " ps --format {{.Names}}"));

		for (String name : namesConsole) {
			if (name.contains(nameFromTable)||nameFromTable.contains(name)) {
				assertTrue("Container has been found!", true);
				return;
			}
		}
		assertTrue("Container has NOT been found!", false);

	}


}

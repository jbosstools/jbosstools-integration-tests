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
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.Test;


/**
 * 
 * @author jkopriva
 *
 */


public class ImageTabTest extends AbstractDockerBotTest {

	@Test
	public void ImageTabTest() {
		DockerImagesTab imageTab = new DockerImagesTab();
		String imageName = System.getProperty("imageName");
		String dockerServerURI = System.getProperty("dockerServerURI");
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);

		String idFromTable = "";

		for (TableItem item : imageTab.getTableItems()) {
			if (item.getText(1).startsWith(imageName)) {
				idFromTable = item.getText();
			}
		}
		idFromTable = idFromTable.replace("sha256:", "");

		ArrayList<String> idsConsole = getIds(executeCommand("docker -H " + dockerServerURI + " images -q"));

		for (String id : idsConsole) {
			if (idFromTable.contains(id)||id.contains(idFromTable)) {
				assertTrue("Image has been found!", true);
				return;
			}
		}
		assertTrue("Image has NOT been found!", false);

	}


}

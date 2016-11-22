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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class ImageTabTest extends AbstractDockerBotTest {

	private String imageName = "hello-world";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testImageTab() {
		pullImage(this.imageName);
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);

		String idFromTable = "";
		String repoTagsFromTable = "";
		String createdFromTable = "";
		String sizeFromTable = "";

		for (TableItem item : imageTab.getTableItems()) {
			if (item.getText(1).contains(imageName)) {
				idFromTable = item.getText();
				repoTagsFromTable = item.getText(1);
				createdFromTable = item.getText(2);
				sizeFromTable = item.getText(3).replaceAll(".", "").replaceAll(" MB", "");
				item.click();
			}
		}
		idFromTable = idFromTable.replace("sha256:", "");

		new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(imageName).select();

		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		propertiesView.selectTab("Info");
		String idProp = propertiesView.getProperty("Id").getPropertyValue();
		String repoTagsProp = propertiesView.getProperty("RepoTags").getPropertyValue();
		String createdProp = propertiesView.getProperty("Created").getPropertyValue();
		String sizeProp = propertiesView.getProperty("VirtualSize").getPropertyValue();

		assertTrue("Id in table and in Properties do not match!", idProp.contains(idFromTable));
		assertTrue("RepoTags in table and in Properties do not match!", repoTagsProp.equals(repoTagsFromTable));
		assertTrue("Created in table and in Properties do not match!", createdProp.equals(createdFromTable));
		assertTrue("Size in table and in Properties do not match!", sizeProp.startsWith(sizeFromTable));
	}

	public void testImageTabSearch() {
		pullImage(this.imageName);
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		imageTab.searchImage("aaa");
		assertTrue("Search result is not 0!", imageTab.getTableItems().size() == 0);
		imageTab.searchImage("");
		assertTrue("Search result is 0!", imageTab.getTableItems().size() > 0);
	}

	@After
	public void after() {
		deleteImage(this.imageName);
		deleteConnection();
	}

}

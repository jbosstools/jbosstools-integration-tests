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

package org.jboss.tools.docker.ui.bot.test.container;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.RunADockerImagePageOneWizard;
import org.jboss.tools.docker.reddeer.core.ui.wizards.RunADockerImagePageTwoWizard;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.reddeer.ui.DockerTerminal;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class LinkContainersTest extends AbstractDockerBotTest {

	private String imageName = "mariadb:latest";
	private String containerNameDB = "test_run_mariadb";
	private String containerNameClient = "test_connect_mariadb";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testLinkContainers() {
		pullImage(this.imageName);
		runDatabase();
		runClient(getDBAddress());
	}

	public void runDatabase() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning());
		imageTab.runImage(this.imageName);
		RunADockerImagePageOneWizard firstPage = new RunADockerImagePageOneWizard();
		firstPage.setName(this.containerNameDB);
		firstPage.setEntrypoint("docker-entrypoint.sh");
		firstPage.setCommand("mysqld");
		firstPage.setPublishAllExposedPorts(false);
		firstPage.next();
		RunADockerImagePageTwoWizard secondPage = new RunADockerImagePageTwoWizard();
		secondPage.addEnviromentVariable("MYSQL_ROOT_PASSWORD", "password");
		secondPage.finish();
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new ConsoleHasNoChange());
	}

	public String getDBAddress() {
		selectContainerInDockerExplorer(containerNameDB);
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		propertiesView.selectTab("Inspect");
		return propertiesView.getProperty("NetworkSettings", "IPAddress").getPropertyValue();
	}

	public void runClient(String dbAddress) {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning());
		imageTab.runImage(this.imageName);
		RunADockerImagePageOneWizard firstPage = new RunADockerImagePageOneWizard();
		firstPage.setName(this.containerNameClient);
		firstPage.setEntrypoint("docker-entrypoint.sh");
		firstPage.setCommand("mysql -h" + dbAddress + " -P3306 -uroot -ppassword");
		firstPage.addLinkToContainer(containerNameDB, "mysql");
		firstPage.setPublishAllExposedPorts();
		firstPage.setKeepSTDINOpen();
		firstPage.setAllocatePseudoTTY();
		try {
			firstPage.finish();
		} catch (WaitTimeoutExpiredException ex) {

		}
		DockerTerminal dt = new DockerTerminal();
		dt.activate();
		assertTrue("No output from terminal!", !dt.getTextFromPage("/" + containerNameClient).isEmpty());
	}

	@After
	public void after() {
		deleteContainer(this.containerNameClient);
		deleteContainer(this.containerNameDB);
		deleteImage(this.imageName);
		deleteConnection();
	}

}

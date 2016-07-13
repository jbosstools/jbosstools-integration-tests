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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.jface.preference.PreferenceDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.docker.reddeer.core.ui.wizards.DockerConnectionWizard;
import org.jboss.tools.docker.reddeer.perspective.DockerPerspective;
import org.jboss.tools.docker.reddeer.preferences.RegistryAccountsPreferencePage;
import org.jboss.tools.docker.reddeer.ui.ConnectionItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * 
 * @author jkopriva
 *
 */

public abstract class AbstractDockerBotTest {

	@BeforeClass
	public static void beforeClass() {
		new WorkbenchShell().maximize();
	}

	@AfterClass
	public static void cleanUp() {
		cleanupShells();
	}

	protected static void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	protected String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	protected ArrayList<String> getIds(String stringWithIds) {
		ArrayList<String> idList = new ArrayList<String>();
		if (stringWithIds == null || stringWithIds.equals("")) {
			return idList;
		}

		idList = new ArrayList<String>(Arrays.asList(stringWithIds.split("\\r?\\n")));
		return idList;
	}

	protected static void openDockerPerspective() {
		new DockerPerspective().open();
		try {
			new ShellWithTextIsAvailable("Docker Explorer");
		} catch (SWTLayerException ex) {
			fail("Docker Explorer not found in Docker tooling perspective");
		}
	}

	protected static void createConnection() {
		String dockerServerURI = System.getProperty("dockerServerURI");
		if (dockerServerURI == null || dockerServerURI.isEmpty()) {
			createConnectionSocket(System.getProperty("unixSocket"));
		} else {
			createConnectionTCP(dockerServerURI);
		}
	}

	protected static void createConnectionTCP(String dockerServer) {
		if (!new DockerExplorer().connectionExist(dockerServer)) {
			DockerConnectionWizard connectionWizard = new DockerConnectionWizard();
			connectionWizard.open();
			connectionWizard.setTcpConnection(dockerServer);
			connectionWizard.finish();
		}
		new DockerExplorer().enableConnection(dockerServer);
	}

	protected static void createConnectionSocket(String unixSocket) {
		if (!new DockerExplorer().connectionExist(unixSocket)) {
			DockerConnectionWizard connectionWizard = new DockerConnectionWizard();
			connectionWizard.open();
			connectionWizard.setUnixSocket(unixSocket);
			connectionWizard.finish();
		}
		new DockerExplorer().enableConnection(unixSocket);
	}

	protected static void deleteConnection() {
		new DockerExplorer().deleteConnection(getDockerServer());
	}

	protected static void deleteImage(String imageName) {
		new DockerExplorer().deleteImage(getDockerServer(), imageName);
	}

	protected static boolean imageIsDeployed(String imageName) {
		return new DockerExplorer().imageIsDeployed(getDockerServer(), imageName);
	}

	protected static void deleteContainer(String containerName) {
		new DockerExplorer().deleteContainer(getDockerServer(), containerName);
	}

	protected void pullImage(String imageName) {
		new DockerExplorer().pullImage(getDockerServer(), imageName);
	}

	protected void pullImage(String registry, String imageName) {
		new DockerExplorer().pullImage(getDockerServer(), registry, imageName);
	}

	protected void checkBrowserForErrorPage(BrowserView browser) {
		checkBrowserForErrorPage(browser, null);
	}

	protected void checkBrowserForErrorPage(BrowserView browser, String url) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		if (browser.getText().contains("Unable") || browser.getText().contains("404")) {
			if (url == null) {
				browser.refreshPage();
			} else {
				browser.openPageURL(url);
			}
		}
		new WaitWhile(new JobIsRunning());
		assertFalse("Browser contains text 'Status 404'\n Console output:\n" + consoleView.getConsoleText(),
				browser.getText().contains("Status 404") || browser.getText().contains("404 - Not Found")
						|| browser.getText().contains("Unable") || browser.getText().contains("Forbidden")
						|| browser.getText().contains("404"));
		assertFalse(
				"Browser contains text 'Error processing request'\n Console output:\n" + consoleView.getConsoleText(),
				browser.getText().contains("Error processing request"));
	}

	protected String createURL(String tail) {
		String dockerServerURI = System.getProperty("dockerServerURI");
		String serverURI;
		if (dockerServerURI != null && !dockerServerURI.isEmpty()) {
			serverURI = dockerServerURI.replaceAll("tcp://", "http://");
		} else {
			serverURI = "http://localhost:1234";
		}
		return serverURI.substring(0, serverURI.lastIndexOf(":")) + tail;
	}

	protected static void setUpRegister(String serverAddress, String email, String userName, String password) {
		PreferenceDialog dialog = new WorkbenchPreferenceDialog();
		RegistryAccountsPreferencePage page = new RegistryAccountsPreferencePage();
		dialog.open();
		dialog.select(page);
		page.addRegistry(serverAddress, email, userName, password);
		try {
			new DefaultShell("New Registry Account").setFocus();
		} catch (SWTLayerException e) {
			new DefaultShell("Preferences").setFocus();
		}
		new OkButton().click();
	}

	protected static void deleteRegister(String serverAddress) {
		PreferenceDialog dialog = new WorkbenchPreferenceDialog();
		RegistryAccountsPreferencePage page = new RegistryAccountsPreferencePage();
		dialog.open();
		dialog.select(page);
		page.removeRegistry(serverAddress);
		new OkButton().click();
	}

	protected static Set<String> getImages(String dockerServer) {
		Set<String> images = new HashSet<String>();
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem imagesItem = dc.getItem("Images");
		imagesItem.select();
		imagesItem.expand();
		for (TreeItem item : imagesItem.getItems()) {
			images.add(item.getText());
		}
		return images;
	}

	protected static void deleteImages(String dockerServer, Set<String> images) {
		for (String image : images) {
			new DockerExplorer().deleteImage(dockerServer, image);
		}
	}

	protected static Set<String> getContainers(String dockerServer) {
		Set<String> containers = new HashSet<String>();
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem containersItem = dc.getItem("Containers");
		containersItem.select();
		containersItem.expand();
		for (TreeItem item : containersItem.getItems()) {
			containers.add(item.getText());
		}
		return containers;
	}

	protected static void deleteContainers(String dockerServer, Set<String> containers) {
		for (String container : containers) {
			new DockerExplorer().deleteContainer(dockerServer, container);
		}
	}

	protected void prepareWorkspace() {
		openDockerPerspective();
		createConnection();
	}

	protected void cleanUpWorkspace() {
		cleanupShells();
		deleteConnection();
	}

	protected static String getDockerServer() {
		if (System.getProperty("dockerServerURI") != null) {
			return System.getProperty("dockerServerURI");
		} else if (System.getProperty("unixSocket") != null) {
			return System.getProperty("unixSocket");
		} else {
			return "unix:///var/run/docker.sock";
		}
	}

}

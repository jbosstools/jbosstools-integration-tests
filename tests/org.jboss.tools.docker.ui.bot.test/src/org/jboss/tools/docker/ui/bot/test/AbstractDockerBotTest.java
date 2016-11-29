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

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.jface.preference.PreferenceDialog;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.docker.reddeer.perspective.DockerPerspective;
import org.jboss.tools.docker.reddeer.preferences.RegistryAccountsPreferencePage;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.resources.AuthenticationMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * 
 * @author jkopriva@redhat.com
 *
 */

public abstract class AbstractDockerBotTest {

	private static DockerExplorerView dockerView;

	@BeforeClass
	public static void beforeClass() {
		new WorkbenchShell().maximize();
	}

	@AfterClass
	public static void cleanUp() {
		killPullingJobs();
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
		String searchConnection = System.getProperty("searchConnection");
		if (searchConnection != null && !searchConnection.isEmpty() && searchConnection.equals("true")) {
			createConnectionSearch("default");
		} else if (dockerServerURI == null || dockerServerURI.isEmpty()) {
			createConnectionSocket(System.getProperty("unixSocket"));
		} else {
			createConnectionTCP(dockerServerURI);
		}
	}

	protected static void createConnectionTCP(String dockerServer) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		if (!dockerView.getDockerConnectionsNames().contains(dockerServer)) {
			dockerView.createDockerConnection(AuthenticationMethod.TCP_CONNECTION, dockerServer, null, dockerServer);
			dockerView.getDockerConnection(dockerServer).enableConnection();
		}
	}

	protected static void createConnectionSocket(String unixSocket) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		if (!dockerView.getDockerConnectionsNames().contains(unixSocket)) {
			dockerView.createDockerConnection(AuthenticationMethod.UNIX_SOCKET, unixSocket, null, unixSocket);
			dockerView.getDockerConnection(unixSocket).enableConnection();
		}
	}

	protected static void createConnectionSearch(String connectionName) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		if (!dockerView.getDockerConnectionsNames().contains(connectionName)) {
			dockerView.createDockerConnection(connectionName);
			dockerView.getDockerConnection(connectionName).enableConnection();
		}
	}

	protected static void deleteConnection() {
		new DockerExplorerView().getDockerConnection(getDockerServer()).removeConnection();
	}

	protected static void deleteImage(String imageName) {
		if (new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(imageName) != null) {
			new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(imageName).remove();
		} else {
			fail("Image " + imageName + " does not exists!");
		}
	}

	protected static void deleteImage(String imageName, String imageTag) {
		new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(imageName, imageTag).remove();
	}

	protected static void deleteImages(String dockerServer, List<String> images) {
		for (String image : images) {
			deleteImage(image);
		}
	}

	protected static boolean imageIsDeployed(String imageName) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).imageIsDeployed(imageName);
	}

	protected static int deployedImagesCount(String imageName) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).deployedImagesCount(imageName);
	}

	protected static void deleteContainer(String containerName) {
		if (new DockerExplorerView().getDockerConnection(getDockerServer()).getContainer(containerName) != null) {
			new DockerExplorerView().getDockerConnection(getDockerServer()).getContainer(containerName).remove();
		} else {
			fail("Container " + containerName + " does not exists!");
		}
	}

	protected static void deleteContainers(String dockerServer, List<String> containers) {
		for (String container : containers) {
			deleteContainer(container);
		}
	}

	protected void pullImage(String imageName) {
		new DockerExplorerView().getDockerConnection(getDockerServer()).pullImage(imageName);
	}

	protected void pullImage(String imageName, String imageTag) {
		new DockerExplorerView().getDockerConnection(getDockerServer()).pullImage(imageName, imageTag, null);
	}

	protected void pullImage(String imageName, String imageTag, String dockerRegister) {
		new DockerExplorerView().getDockerConnection(getDockerServer()).pullImage(imageName, imageTag, dockerRegister);
	}

	protected String createURL(String tail) {
		String dockerServerURI = System.getProperty("dockerServerURI");
		String searchConnection = System.getProperty("searchConnection");
		String serverURI;
		if (dockerServerURI != null && !dockerServerURI.isEmpty()
				&& (searchConnection == null || !searchConnection.equals("true"))) {
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

	protected static List<String> getImages(String dockerServer) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).getImagesNames();
	}

	protected static List<String> getContainers(String dockerServer) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).getContainersNames();
	}

	protected void prepareWorkspace() {
		openDockerPerspective();
		deleteCreatedConnections();
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
			return "default";
		}
	}

	public static void setSecureStorage(String password) {
		try {
			new DefaultShell("Secure Storage Password");
			new LabeledText("Password:").setText(password);
			new LabeledText("Confirm password:").setText(password);
			new PushButton("OK").click();
			new DefaultShell("Secure Storage - Password Hint Needed");
			new PushButton("NO").click();
		} catch (CoreLayerException ex) {
			new PushButton("OK").click();
		} catch (SWTLayerException e) {
			new DefaultShell("Secure Storage");
			new LabeledText("Password:").setText(password);
			new PushButton("OK").click();
		}

	}

	public static void killPullingJobs() {
		Job[] currentJobs = Job.getJobManager().find(null);
		for (Job job : currentJobs) {
			if (job.getName().startsWith("Pulling docker image")) {
				job.cancel();
			}
		}
	}

	public static void deleteCreatedConnections() {
		List<String> connections = new DockerExplorerView().getDockerConnectionsNames();
		for (String connection : connections) {
			new DockerExplorerView().getDockerConnection(connection).removeConnection();
		}
	}

}

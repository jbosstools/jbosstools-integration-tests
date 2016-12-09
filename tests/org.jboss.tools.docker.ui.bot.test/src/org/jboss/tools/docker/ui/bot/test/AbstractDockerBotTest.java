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

import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
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

	private static final String DOCKER_UNIX_SOCKET = "unix:///var/run/docker.sock";

	private static DockerExplorerView dockerView;

	@BeforeClass
	public static void beforeClass() {
		new WorkbenchShell().maximize();
	}

	@AfterClass
	public static void cleanUp() {
		killRunningJobs();
		cleanupShells();
	}

	protected static void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	protected static void openDockerPerspective() {
		new DockerPerspective().open();
		try {
			new ShellWithTextIsAvailable("Docker Explorer");
		} catch (SWTLayerException ex) {
			fail("Docker Explorer not found in Docker tooling perspective. ");
		}
	}

	protected static void createConnection() {
		String dockerServerURI = System.getProperty("dockerServerURI");
		String searchConnection = System.getProperty("searchConnection");
		String unixSocket = System.getProperty("unixSocket");
		if (searchConnection != null && !searchConnection.isEmpty() && searchConnection.equals("true")) {
			createConnectionSearch("default");
		} else if (unixSocket != null && !unixSocket.isEmpty()) {
			createConnectionSocket(unixSocket);
		} else if (dockerServerURI != null && !dockerServerURI.isEmpty()) {
			createConnectionTCP(dockerServerURI);
		} else { // if no connection is specified, then use default linux socket
			createConnectionSocket(DOCKER_UNIX_SOCKET);
		}
	}

	protected static void createConnectionTCP(String dockerServer) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		if (!dockerView.getDockerConnectionsNames().contains(dockerServer)) {
			dockerView.createDockerConnection(AuthenticationMethod.TCP_CONNECTION, dockerServer, null, dockerServer);
		}
		dockerView.getDockerConnection(dockerServer).enableConnection();
	}

	protected static void createConnectionSocket(String unixSocket) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		if (!dockerView.getDockerConnectionsNames().contains(unixSocket)) {
			dockerView.createDockerConnection(AuthenticationMethod.UNIX_SOCKET, unixSocket, null, unixSocket);
		}
		dockerView.getDockerConnection(unixSocket).enableConnection();
	}

	protected static void createConnectionSearch(String connectionName) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		if (!dockerView.getDockerConnectionsNames().contains(connectionName)) {
			dockerView.createDockerConnection(connectionName);
		}
		dockerView.getDockerConnection(connectionName).enableConnection();
	}

	protected static void deleteConnection() {
		checkConnection();
		new DockerExplorerView().getDockerConnection(getDockerServer()).removeConnection();
	}

	protected static void checkConnection() {
		if (new DockerExplorerView().getDockerConnection(getDockerServer()) == null) {
			fail("Docker connection does not exist! Please check parameters.");
		}
	}

	protected static String getCompleteImageName(String imageName) {
		for (String image : new DockerExplorerView().getDockerConnection(getDockerServer()).getImagesNames()) {
			if (image.contains(imageName)) {
				imageName = image.replace(":", "");
			}
		}
		return imageName;
	}

	protected static void deleteImage(String imageName) {
		String name = getCompleteImageName(imageName);
		if (imageIsDeployed(name)) {
			new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(name).remove();
		} else {
			fail("Image " + imageName + ":latest" + "(" + name + ":latest)" + " does not exists!");
		}
	}

	protected static void deleteImage(String imageName, String imageTag) {
		String name = getCompleteImageName(imageName);
		if (new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(name, imageTag) != null) {
			new DockerExplorerView().getDockerConnection(getDockerServer()).getImage(name, imageTag).remove();
		} else {
			fail("Image " + imageName + ":" + imageTag + "(" + name + ":" + imageTag + ")" + " does not exists!");
		}
	}

	protected static boolean imageIsDeployed(String imageName) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).imageIsDeployed(imageName);
	}

	protected static int deployedImagesCount(String imageName) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).deployedImagesCount(imageName);
	}

	protected static boolean containerIsDeployed(String containerName) {
		return new DockerExplorerView().getDockerConnection(getDockerServer()).containerIsDeployed(containerName);
	}

	protected static void deleteContainer(String containerName) {
		if (containerIsDeployed(containerName)) {
			new DockerExplorerView().getDockerConnection(getDockerServer()).getContainer(containerName).remove();
		} else {
			fail("Container " + containerName + " does not exists!");
		}
	}

	protected void pullImage(String imageName) {
		pullImage(imageName, null, null);
	}

	protected void pullImage(String imageName, String imageTag) {
		pullImage(imageName, imageTag, null);
	}

	protected void pullImage(String imageName, String imageTag, String dockerRegister) {
		checkConnection();
		try {
			new DockerExplorerView().getDockerConnection(getDockerServer()).pullImage(imageName, imageTag,
					dockerRegister);
		} catch (WaitTimeoutExpiredException ex) {
			killRunningJobs();
			fail("Timeout expired when pulling image:" + imageName + (imageTag == null ? "" : ":" + imageTag) + "!");
		}
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
		page.removeRegistry(serverAddress);
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
		new WaitWhile(new JobIsRunning());
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
		deleteAllConnections();
		createConnection();
	}

	protected void cleanUpWorkspace() {
		cleanupShells();
		killRunningJobs();
		deleteConnection();
	}

	protected static String getDockerServer() {
		if (System.getProperty("dockerServerURI") != null && !System.getProperty("dockerServerURI").isEmpty()) {
			return getConnectionName(System.getProperty("dockerServerURI"));
		} else if (System.getProperty("unixSocket") != null && !System.getProperty("unixSocket").isEmpty()) {
			return getConnectionName(System.getProperty("unixSocket"));
		} else {
			return getConnectionName("default");
		}
	}

	private static String getConnectionName(String connectionName) {
		dockerView = new DockerExplorerView();
		dockerView.open();
		for (String name : dockerView.getDockerConnectionsNames()) {
			if (name.contains(connectionName)) {
				connectionName = name;
				break;
			}
		}
		return connectionName;
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
			try {
				new DefaultShell("Secure Storage");
				new LabeledText("Password:").setText(password);
				new PushButton("OK").click();
			} catch (SWTLayerException ex) {
				// Secure storage password is set
			}
		}

	}

	public static void killRunningJobs() {
		Job[] currentJobs = Job.getJobManager().find(null);
		for (Job job : currentJobs) {
			if (job.getName().startsWith("Pulling docker image") || job.getName().startsWith("Tagging Image")
					|| job.getName().startsWith("Pushing Docker Image")) {
				job.cancel();
			}
		}
	}

	public static void deleteAllConnections() {
		List<String> connections = new DockerExplorerView().getDockerConnectionsNames();
		for (String connection : connections) {
			new DockerExplorerView().getDockerConnection(connection).removeConnection();
		}
	}

	public static void deleteImageContainerAfter(String... imageContainerNames) {
		checkConnection();
		killRunningJobs();
		for (String imageContainerName : imageContainerNames) {
			if (imageIsDeployed(imageContainerName.split(":")[0])) {
				if (!imageContainerName.contains(":")) {
					deleteImage(imageContainerName);
				} else {
					deleteImage(imageContainerName.split(":")[0], imageContainerName.split(":")[1]);
				}
			}
			if (containerIsDeployed(imageContainerName)) {
				deleteContainer(imageContainerName);
			}
		}
	}

}

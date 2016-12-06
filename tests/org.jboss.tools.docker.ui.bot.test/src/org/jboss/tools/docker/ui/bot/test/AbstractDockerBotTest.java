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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.jobs.Job;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.jface.preference.PreferenceDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.docker.reddeer.perspective.DockerPerspective;
import org.jboss.tools.docker.reddeer.preferences.RegistryAccountsPreferencePage;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.resources.AuthenticationMethod;
import org.jboss.tools.docker.reddeer.ui.resources.DockerConnection;
import org.jboss.tools.docker.reddeer.ui.resources.DockerImage;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

/**
 * 
 * @author jkopriva@redhat.com
 * @contributor adietish@redhat.com
 *
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(DockerPerspective.class)
public abstract class AbstractDockerBotTest {

	private static final String SYSPROP_DOCKER_MACHINE_NAME = "dockerMachineName";
	private static final String SYSPROP_DOCKER_SERVER_URI = "dockerServerURI";
	private static final String SYSPROP_UNIX_SOCKET = "unixSocket";

	private static final String DEFAULT_CONNECTION_NAME = "default";

	private DockerConnection connection = null;
	
	@AfterClass
	public static void afterClass() {
		killRunningJobs();
		cleanupShells();
	}

	protected static void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	protected List<String> getIds(String stringWithIds) {
		if (StringUtils.isBlank(stringWithIds)) {
			return Collections.emptyList();
		}
		ArrayList<String> idList = new ArrayList<String>();
		idList = new ArrayList<String>(Arrays.asList(stringWithIds.split("\\r?\\n")));
		return idList;
	}

	/**
	 * Creates a connection with the settings in this test. Stores it in
	 * instance variable {@link #connection}.
	 * 
	 * @returns the connection that was creates
	 * 
	 * @see #SYSPROP_DOCKER_MACHINE_NAME
	 * @see #SYSPROP_DOCKER_SERVER_URI
	 * @see #SYSPROP_UNIX_SOCKET
	 */
	protected DockerConnection createConnection() {
		DockerExplorerView dockerView = new DockerExplorerView();
		dockerView.open();
		String dockerMachineName = System.getProperty(SYSPROP_DOCKER_MACHINE_NAME);
		String dockerServerURI = System.getProperty(SYSPROP_DOCKER_SERVER_URI);
		String unixSocket = System.getProperty(SYSPROP_UNIX_SOCKET);
		if (!StringUtils.isBlank(dockerMachineName)) {
			dockerView.createDockerConnectionSearch(dockerMachineName);
			this.connection = getConnectionByName(dockerMachineName);
		} else if (!StringUtils.isEmpty(dockerServerURI)) {
			dockerView.createDockerConnection(AuthenticationMethod.TCP_CONNECTION, dockerServerURI, null, dockerServerURI);
			this.connection = getConnectionByHost(dockerServerURI);
		} else if (!StringUtils.isEmpty(unixSocket)) {
			dockerView.createDockerConnection(AuthenticationMethod.UNIX_SOCKET, unixSocket, null, unixSocket);
			this.connection = getConnectionByHost(unixSocket);
		} else {
			fail("Cannot create a docker connection. "
					+ "Neither " + SYSPROP_DOCKER_MACHINE_NAME 
					+ " nor " + SYSPROP_DOCKER_SERVER_URI 
					+ " nor " + SYSPROP_UNIX_SOCKET + " were defined.");
		}

		// can't be null, fails before
		connection.enableConnection();
		return connection;
	}

	protected void deleteConnection() {
		getConnection().removeConnection();
		this.connection = null;
	}

	protected String getCompleteImageName(String imageName) {
		for (String image : getConnection().getImagesNames()) {
			if (image.contains(imageName)) {
				imageName = image.replace(":", "");
			}
		}
		return imageName;
	}

	protected void deleteImage(String imageName) {
		String name = getCompleteImageName(imageName);
		if (imageIsDeployed(name)) {
			getConnection().getImage(name).remove();
		} else {
			fail("Image " + imageName + ":latest" + "(" + name + ":latest)" + " does not exists!");
		}
	}

	protected void deleteImage(String imageName, String imageTag) {
		String completeImageName = getCompleteImageName(imageName);
		DockerImage image = getConnection().getImage(completeImageName, imageTag);
		if (image == null) {
			fail("Image " + imageName + ":" + imageTag + "(" + completeImageName + ":" + imageTag + ")" + " does not exists!");
		}
		image.remove();
	}

	protected void deleteImages(String dockerServer, List<String> images) {
		for (String image : images) {
			deleteImage(image);
		}
	}

	protected boolean imageIsDeployed(String imageName) {
		return getConnection().imageIsDeployed(imageName);
	}

	protected int deployedImagesCount(String imageName) {
		return getConnection().deployedImagesCount(imageName);
	}

	protected boolean containerIsDeployed(String containerName) {
		return getConnection().containerIsDeployed(containerName);
	}

	protected void deleteContainer(String containerName) {
		if (containerIsDeployed(containerName)) {
			getConnection().getContainer(containerName).remove();
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
		try {
			getConnection().pullImage(imageName, imageTag, dockerRegister);
		} catch (WaitTimeoutExpiredException ex) {
			killRunningJobs();
			fail("Timeout expired when pulling image:" + imageName + (imageTag == null ? "" : ":" + imageTag) + "!");
		}
	}

	protected String createURL(String tail) {
		String dockerServerURI = System.getProperty(SYSPROP_DOCKER_SERVER_URI);
		String serverURI = null;
		if (!StringUtils.isBlank(System.getProperty(SYSPROP_DOCKER_MACHINE_NAME)) 
				|| !StringUtils.isBlank(System.getProperty(SYSPROP_UNIX_SOCKET))
				|| StringUtils.isBlank(dockerServerURI)) {
			serverURI = "http://localhost:1234";
		} else if (!StringUtils.isBlank(dockerServerURI)) {
			serverURI = dockerServerURI.replaceAll(
					DockerExplorerView.SCHEME_TCP, 
					DockerExplorerView.SCHEME_HTTP);
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

	protected List<String> getImages(String dockerServer) {
		return getConnection().getImagesNames();
	}

	protected List<String> getContainers(String dockerServer) {
		return getConnection().getContainersNames();
	}

	protected void prepareWorkspace() {
	}

	protected void cleanUpWorkspace() {
		cleanupShells();
		killRunningJobs();
	}

	private String getServer() {
		if (!StringUtils.isBlank(System.getProperty(SYSPROP_DOCKER_SERVER_URI))) {
			return System.getProperty(SYSPROP_DOCKER_SERVER_URI);
		} else if (!StringUtils.isBlank(System.getProperty(SYSPROP_UNIX_SOCKET))) {
			return System.getProperty(SYSPROP_UNIX_SOCKET);
		} else {
			return DEFAULT_CONNECTION_NAME;
		}
	}

	/**
	 * Returns {@code true} if the configuration for this test is set to use a host as docker server (unix socket, serverURI). 
	 * Returns {@code false} if the configuration for this test is set to use a name as docker server (docker-machine)
	 * @return
	 */
	private boolean isDockerServerHost() {
		return !StringUtils.isBlank(System.getProperty(SYSPROP_DOCKER_SERVER_URI))
				|| !StringUtils.isBlank(System.getProperty(SYSPROP_UNIX_SOCKET));
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

	public void deleteImageContainerAfter(String... imageContainerNames) {
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

	/**
	 * Returns a connection that matches the current settings. If none is found, a new one is created.
	 * 
	 * @return
	 * 
	 * @see #createConnection()
	 */
	protected DockerConnection getConnection() {
		if (connection != null) {
			return connection;
		}

		if (isDockerServerHost()) {
			this.connection = getConnectionByHost(getServer());
		} else {
			this.connection = getConnectionByName(getServer());
		}

		if (connection == null) {
			createConnection();
		}
		return connection;
	}

	private DockerConnection getConnectionByName(String name) {
		 DockerConnection connection = new DockerExplorerView().getDockerConnectionByName(name);
		 if (connection == null) {
			 fail("Connection " + name + " was not found" );
		 }
		 return connection;
	}

	private DockerConnection getConnectionByHost(String host) {
		 DockerConnection connection = new DockerExplorerView().getDockerConnectionByHost(host);
		 if (connection == null) {
			 fail("Connection to " + host + " was not found" );
		 }
		 return connection;
	}
	
	protected void deleteAllConnections() {
		for (String name : new DockerExplorerView().getDockerConnectionNames()) {
			getConnectionByName(name).removeConnection();;
		}
	}
}

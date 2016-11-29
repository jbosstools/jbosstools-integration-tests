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

package org.jboss.tools.docker.reddeer.ui;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.NewDockerConnectionPage;
import org.jboss.tools.docker.reddeer.ui.resources.AuthenticationMethod;
import org.jboss.tools.docker.reddeer.ui.resources.DockerConnection;

/**
 * 
 * @author jkopriva@redhat.com, mlabuda@redhat.com
 *
 */

public class DockerExplorerView extends WorkbenchView {

	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();

	public DockerExplorerView() {
		super("Docker", "Docker Explorer");
	}

	/**
	 * Gets names of all docker connections present in docker explorer.
	 * 
	 * @return list of docker connections names
	 */
	public List<String> getDockerConnectionsNames() {
		activate();
		List<String> connectionsNames = new ArrayList<String>();
		try {
			List<TreeItem> connections = new DefaultTree().getItems();
			for (TreeItem item : connections) {
				connectionsNames.add(treeViewerHandler.getNonStyledText(item));
			}
		} catch (CoreLayerException ex) {
			// no connections in view
		}
		return connectionsNames;
	}

	public boolean connectionExist(String connectionName) {
		return getDockerConnectionsNames().contains(connectionName);
	}

	public void refreshView() {
		List<String> connections = getDockerConnectionsNames();
		for (String connection : connections) {
			getDockerConnection(connection).refresh();
		}
	}

	/**
	 * Creates a docker connection connected to a docker daemon through Search
	 * Connection socket with name "default".
	 * 
	 */
	public void createDockerConnection(String connectionName) {
		activate();
		NewDockerConnectionPage connectionWizard = new NewDockerConnectionPage();
		connectionWizard.open();
		connectionWizard.search(connectionName);
		connectionWizard.finish();
	}

	/**
	 * Creates a docker connection connected to a docker daemon through unix
	 * socket with name "default".
	 * 
	 * @param unixSocket
	 *            unix socket of a docker daemon
	 */
	public void createDockerConnection(String connectionName, String unixSocket) {
		createDockerConnection(AuthenticationMethod.UNIX_SOCKET, unixSocket, null, connectionName);
	}

	/**
	 * Creates a docker connection connected to a docker daemon through TCP with
	 * name "default".
	 * 
	 * @param tcpURI
	 *            TCP URI
	 * @param certificatePath
	 *            path to a certificate
	 */
	public void createDockerConnection(String connectionName, String tcpURI, String certificatePath) {
		createDockerConnection(AuthenticationMethod.TCP_CONNECTION, tcpURI, certificatePath, connectionName);
	}

	/**
	 * Creates a docker connection connected to a docker daemon through TCP or
	 * unix socket with a specified name.
	 * 
	 * @param authMethod
	 *            unix socket or TCP URI
	 * @param unixSocketOrTcpURI
	 *            unix socket path or TCP URI
	 * @param certificatePath
	 *            path to a certificate if exists
	 * @param connectionName
	 *            docker connection name
	 */
	public void createDockerConnection(AuthenticationMethod authMethod, String unixSocketOrTcpURI,
			String authentificationCertificatePath, String connectionName) {

		activate();
		NewDockerConnectionPage connectionWizard = new NewDockerConnectionPage();
		connectionWizard.open();
		connectionWizard.setConnectionName(connectionName);
		if (AuthenticationMethod.TCP_CONNECTION.equals(authMethod) && authentificationCertificatePath != null) {
			connectionWizard.setTcpConnection(unixSocketOrTcpURI, authentificationCertificatePath, false);
		} else if (AuthenticationMethod.TCP_CONNECTION.equals(authMethod) && authentificationCertificatePath == null) {
			connectionWizard.setTcpConnection(unixSocketOrTcpURI);
		} else {
			connectionWizard.setUnixSocket(unixSocketOrTcpURI);
		}
		connectionWizard.finish();
	}

	/**
	 * Gets docker connection with specific name or null if does not exists.
	 * 
	 * @return DockerConnection with specific name or null if does not exist.
	 */
	public DockerConnection getDockerConnection(String connectionName) {
		activate();
		try {
			return new DockerConnection(treeViewerHandler.getTreeItem(new DefaultTree(), connectionName));
		} catch (JFaceLayerException ex) {
			return null;
		}
	}

}

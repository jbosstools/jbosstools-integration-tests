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
package org.jboss.tools.docker.reddeer.condition;

import static org.junit.Assert.assertNotNull;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.docker.reddeer.ui.resources.DockerConnection;

/**
 * @author adietish@redhat.com
 */
public class ContainerIsDeployedCondition extends AbstractWaitCondition {

	private String name;
	private DockerConnection connection;

	public ContainerIsDeployedCondition(String name, DockerConnection connection) {
		assertNotNull(this.name = name);
		assertNotNull(this.connection = connection);
	}

	@Override
	public boolean test() {
		return connection.getContainer(name) != null;
	}
}

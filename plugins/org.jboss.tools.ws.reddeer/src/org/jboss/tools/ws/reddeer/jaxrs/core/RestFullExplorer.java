/*******************************************************************************
 * Copyright (c) 2007-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.reddeer.jaxrs.core;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;

/**
 * Represents JAX-RS Explorer showed in Project Explorer as a project item in
 * the project with enabled jax-rs support.
 *
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public class RestFullExplorer {

	private ProjectItem restFulExplorer;

	/**
	 * Represents JAX-RS Explorer in the specified project.
	 * 
	 * @param wsProjectName name of the project with JAX-RS Explorer
	 */
	public RestFullExplorer(String wsProjectName) {
		Project project = new ProjectExplorer().getProject(wsProjectName);

		/*
		 * REDDEER-369 workaround for bug that will show project with no items
		 */
		project.getTreeItem().expand();
		project.getTreeItem().collapse();
		project.getTreeItem().expand();

		/* wait for validation */
		AbstractWait.sleep(TimePeriod.getCustom(2));// improves stability

		/* open JAX-RS / RESTful Explorer */
		restFulExplorer = project
				.getProjectItem(RestFullLabels.REST_EXPLORER_LABEL.getLabel());
		restFulExplorer.open();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Returns all rest services from JAX-RS Explorer.
	 */
	public List<RestService> getAllRestServices() {
		List<RestService> restServices = new ArrayList<RestService>();
		for (ProjectItem projectItem : restFulExplorer.getChildren()) {
			restServices.add(new RestService(projectItem));
		}
		return restServices;
	}

	/**
	 * Returns rest service with the specified <var>method</var>.
	 * 
	 * @param method JAX-RS method's name
	 */
	public RestService getRestService(String method) {
		for (ProjectItem projectItem : restFulExplorer.getChildren()) {
			if (projectItem.getText().contains(method)) {
				return new RestService(projectItem);
			}
		}
		return null;
	}
}

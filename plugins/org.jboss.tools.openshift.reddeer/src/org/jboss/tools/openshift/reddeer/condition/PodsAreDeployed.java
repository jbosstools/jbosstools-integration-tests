/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.condition;

import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftProject;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftResource;

/**
 * Wait condition to wait until pods are deployed. That mean desired pod amount is
 * equal to current pod amount in property value of a replication controller.
 * This does not necessary mean that OpenShift explorer view has already been
 * updated and all pods are already visible, it takes a bit longer until those 
 * changes are propagated and visible on this level. Basically it is enough to see updated
 * property value.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class PodsAreDeployed extends AbstractWaitCondition {
	
	private OpenShiftResource replicationController;
	private String podAmountValue;
	
	/**
	 * Constructor to wait for a specific amount of pods to be running
	 * @param projectName name of project with a replication controller
	 * @param resourceName name of replication controller
	 * @param desiredAmountOfPods desired amount of running pods
	 */
	public PodsAreDeployed(String projectName, String resourceName, int desiredAmountOfPods) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		replicationController = explorer.getOpenShift3Connection().getProject(projectName).
				getOpenShiftResource(Resource.DEPLOYMENT, resourceName);
		podAmountValue = desiredAmountOfPods + " current / " + desiredAmountOfPods + " desired";
	}
	
	@Override
	public boolean test() {
		replicationController.select();
		return replicationController.getPropertyValue("Misc", "Replicas").trim().equals(
				podAmountValue);
	}
	
	public static int getNumberOfCurrentReplicas() {
		return Integer.valueOf(getReplicasInfo().split(" ")[0]).intValue();
	}
	
	public static int getNumberOfDesiredReplicas() {
		return Integer.valueOf(getReplicasInfo().split(" ")[3]).intValue();
	}
	
	public static String getReplicasInfo() { 
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShiftProject project = explorer.getOpenShift3Connection().getProject();		
		OpenShiftResource replicationController = null;
		List<OpenShiftResource> rsrcs = project.getOpenShiftResources(Resource.DEPLOYMENT);
		for (OpenShiftResource resource: rsrcs) {
			if (resource.getName().contains("-" + rsrcs.size())) {
				replicationController = resource;
				break;
			}
		}
		
		return replicationController.getPropertyValue("Misc", "Replicas").trim();
	}
}

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
package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.fail;

import java.util.List;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class DeleteResourceTest extends AbstractCreateApplicationTest {

	@BeforeClass
	public static void waitForApplication() {
		new WaitUntil(new OpenShiftResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE), 
				TimePeriod.getCustom(600), true, 8000);
		
		new WaitUntil(new AmountOfResourcesExists(Resource.POD, 2), TimePeriod.VERY_LONG, true, 5000);
	}
	
	@Test
	public void testDeletePod() {
		OpenShiftResource applicationPod = getApplicationPod();
		String podName = applicationPod.getName();
		
		applicationPod.delete();
		
		try {
			new WaitWhile(new OpenShiftResourceExists(Resource.POD, podName), TimePeriod.getCustom(15));
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application pod should be deleted at this point, but it it still present.");
		}
	}

	private OpenShiftResource getApplicationPod() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		List<OpenShiftResource> pods = explorer.getOpenShift3Connection().getProject().
				getOpenShiftResources(Resource.POD);
		for (OpenShiftResource pod: pods) {
			if (!pod.getName().equals("eap-app-1-build") && !pod.getName().equals("eap-app-1-deploy")) {
				return pod;
			}
		}
		return null;
	}
	
	@Test
	public void testDeleteBuild() {
		deleteResourceAndAssert(Resource.BUILD);
	}
	
	@Test
	public void testDeleteBuildConfig() {
		deleteResourceAndAssert(Resource.BUILD_CONFIG);
	}
	
	@Test
	public void testDeleteRoute() {
		deleteResourceAndAssert(Resource.ROUTE);
	}
	
	@Test
	public void testDeleteImageStream() {
		deleteResourceAndAssert(Resource.IMAGE_STREAM);
	}
	
	@Test
	public void testDeleteDeploymentConfig() {
		deleteResourceAndAssert(Resource.DEPLOYMENT_CONFIG);
	}
	
	@Test
	public void testDeleteService() {
		deleteResourceAndAssert(Resource.SERVICE);
	}
	
	private void deleteResourceAndAssert(Resource resource) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShiftResource rsrc = explorer.getOpenShift3Connection().getProject().
				getOpenShiftResources(resource).get(0);
		String resourceName = rsrc.getName();
		rsrc.delete();
		
		try {
			new WaitWhile(new OpenShiftResourceExists(resource, resourceName), TimePeriod.getCustom(15));
		} catch (WaitTimeoutExpiredException ex) {
			fail("Route " + resource + " should be deleted at this point but it is still present.");
		}
	}
}

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

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.openshift.reddeer.condition.ApplicationPodIsRunning;
import org.jboss.tools.openshift.reddeer.condition.PodsAreDeployed;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftResource;
import org.jboss.tools.openshift.reddeer.view.resources.Service;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScalingTest extends AbstractCreateApplicationTest {
	
	private String replicationControllerName = buildConfigName + "-1";
	
	@BeforeClass
	public static void waitForApplicationPod() {
		new WaitUntil(new ApplicationPodIsRunning(), TimePeriod.getCustom(1200));
	}
	
	@Test
	public void testScaleApplicationViaContextMenuOfService() {	
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		int amountOfPods = PodsAreDeployed.getNumberOfDesiredReplicas();
		new WaitUntil(new PodsAreDeployed(DatastoreOS3.PROJECT1_DISPLAYED_NAME, 
				replicationControllerName, amountOfPods));
		
		Service eapService = explorer.getOpenShift3Connection().getProject().getService(buildConfigName);
		
		eapService.select();
		scaleUp(++amountOfPods);
		
		eapService.select();
		scaleDown(--amountOfPods);
		
		eapService.select();
		amountOfPods += 2;
		scaleTo(amountOfPods);
	}

	@Test
	public void testScaleApplicationViaContextMenuOfReplicationController() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		int amountOfPods = PodsAreDeployed.getNumberOfDesiredReplicas();
		new WaitUntil(new PodsAreDeployed(DatastoreOS3.PROJECT1_DISPLAYED_NAME, 
				replicationControllerName, amountOfPods));
		
		OpenShiftResource replicationController = explorer.getOpenShift3Connection().
				getProject().getOpenShiftResources(Resource.DEPLOYMENT).get(0);
		
		replicationController.select();
		scaleUp(++amountOfPods);
		
		replicationController.select();
		scaleDown(--amountOfPods);
		
		replicationController.select();
		amountOfPods += 2;
		scaleTo(amountOfPods);
	}
	
	private void scaleUp(int newAmountOfPods) {
		new ContextMenu(OpenShiftLabel.ContextMenu.SCALE_UP).select();
		assertPodAmountDesiredEqualsCurrent(newAmountOfPods);
	}
	
	private void scaleDown(int newAmountOfPods) {
		new ContextMenu(OpenShiftLabel.ContextMenu.SCALE_DOWN).select();
		assertPodAmountDesiredEqualsCurrent(newAmountOfPods);
	}
	
	private void scaleTo(int newAmountOfPods) {
		new ContextMenu(OpenShiftLabel.ContextMenu.SCALE_TO).select();
		new DefaultShell(OpenShiftLabel.Shell.SCALE_DEPLOYMENT);
		new DefaultText().setText(String.valueOf(newAmountOfPods));
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SCALE_DEPLOYMENT));
		assertPodAmountDesiredEqualsCurrent(newAmountOfPods);
	}
	
	private void assertPodAmountDesiredEqualsCurrent(int podAmount) {
		try {
			new WaitUntil(new PodsAreDeployed(DatastoreOS3.PROJECT1_DISPLAYED_NAME, 
				replicationControllerName, podAmount), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Pods have not been scaled, amount of current and desired pods do "
					+ "not match. Desired amount:" + podAmount + 
					". Real amount of replicas: " + PodsAreDeployed.getReplicasInfo());
		}
	}
}

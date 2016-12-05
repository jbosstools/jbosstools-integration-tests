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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.ApplicationPodIsRunning;
import org.jboss.tools.openshift.reddeer.condition.ConsoleHasSomeText;
import org.jboss.tools.openshift.reddeer.condition.OpenShiftResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogsTest extends AbstractCreateApplicationTest {
	
	@BeforeClass
	public static void waitForApplication() {
		new WaitUntil(new OpenShiftResourceExists(Resource.BUILD, "eap-app-1", ResourceState.COMPLETE), 
				TimePeriod.getCustom(600), true, TimePeriod.getCustom(8));
		
		new WaitUntil(new AmountOfResourcesExists(Resource.POD, 2), TimePeriod.VERY_LONG, true,
				TimePeriod.getCustom(5));
	}
	
	@Test
	public void testPodLogOfApplicationPod() {
		TestUtils.setUpOcBinary();
		
		ApplicationPodIsRunning applicationPodIsRunning = new ApplicationPodIsRunning();
		new WaitUntil(applicationPodIsRunning, TimePeriod.LONG);
		
		OpenShiftResource pod  = new OpenShiftExplorerView().getOpenShift3Connection().getProject().
			getOpenShiftResource(Resource.POD, applicationPodIsRunning.getApplicationPodName());
		pod.select();
		String podName = pod.getName();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.POD_LOG).select();
		
		ConsoleView consoleView = new ConsoleView();
		
		new WaitUntil(new ConsoleHasSomeText() {
			@Override public String errorMessage() {
				return "Console of pod log is not opened or does not have text";
			}}, TimePeriod.NORMAL);
		new WaitUntil(new ConsoleHasNoChange(TimePeriod.getCustom(7)), TimePeriod.VERY_LONG);
		
		assertTrue("Console label is incorrect, it should contains project name and pod name.\n"
						+ "but label is: " + consoleView.getConsoleLabel(), consoleView.getConsoleLabel().contains(
						DatastoreOS3.PROJECT1 + "\\" + podName));
		assertTrue("Console text should contain output from EAP runtime",
				consoleView.getConsoleText().contains("Admin console listening on"));
	}
	
	@Test
	public void testPodLogOfCompletedBuildPod() {
		TestUtils.setUpOcBinary();
		
		new WaitUntil(new OpenShiftResourceExists(Resource.BUILD, "eap-app-1"), 
				TimePeriod.getCustom(600), true, TimePeriod.getCustom(8));
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		explorer.activate();
		explorer.getOpenShift3Connection().
				getProject().getOpenShiftResources(Resource.BUILD).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.BUILD_LOG).select();
		
		new WaitUntil(new ConsoleHasSomeText() {
			@Override public String errorMessage() {
				return "Console is not opened or does not have text. Could be failing because of JBIDE-23622.";
			}}, TimePeriod.NORMAL);

		ConsoleView consoleView = new ConsoleView();
		assertTrue("Console label is incorrect, it should contains project name and name of build pod.\n"
				+ "but label is: " + consoleView.getConsoleLabel(), consoleView.getConsoleLabel().contains(
				DatastoreOS3.PROJECT1 + "\\eap-app-1-build"));
		
		try {
			new WaitUntil(new org.jboss.reddeer.eclipse.condition.ConsoleHasText("Push successful"),
				TimePeriod.getCustom(600));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be output of succesful build in console log, but there is not.\n"
					+ "Check whether output has not changed. Assumed output in the end of log is 'Push successful'");
		}
	}
}

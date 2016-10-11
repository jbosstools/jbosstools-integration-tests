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
package org.jboss.tools.openshift.ui.bot.test.application.adapter;

import static org.junit.Assert.assertTrue;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

/**
 * Test capabilities of switching project deployment location.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID801SwitchProjectDeploymentTest extends IDXXXCreateTestingApplication {

	@Test
	public void testSwitchDeployment() {
		ServersView servers = new ServersView();
		servers.open();
		
		ServerAdapter serverAdapter = new ServerAdapter(ServerAdapter.Version.OPENSHIFT2, applicationName);
		serverAdapter.select();
		
		new ContextMenu("Properties").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(new StringContains("Properties for " + applicationName)),
				TimePeriod.getCustom(5), false);
		
		DefaultShell serverAdapterShell = new DefaultShell(new WithTextMatcher(
				new StringContains("Properties for " + applicationName)));		
		new PushButton("Switch Location").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertTrue("Location was not correctly switched to Servers.",
			new DefaultLabel(9).getText().equals("/Servers/" +
				applicationName + " at OpenShift 2.server"));
		
		new PushButton("Switch Location").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertTrue("Location was not switched to default.",
				new DefaultLabel(9).getText().equals("[workspace metadata]"));
		
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(serverAdapterShell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}

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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.view.ServerAdapter;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.junit.Test;

/**
 * Test correctness of data in server details (ServerEditor).
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID802ServerAdapterOverviewTest extends IDXXXCreateTestingApplication {

	@Test
	public void testServerAdapterOverview() {
		ServersView servers = new ServersView();
		servers.open();
		
		ServerAdapter serverAdapter = new ServerAdapter(ServerAdapter.Version.OPENSHIFT2, applicationName);
		serverAdapter.openOverview();
		
		assertTrue("Deployed project name is not same as in project explorer.",
				new LabeledCombo("Deploy Project: ").getSelection().equals(applicationName));
		
		assertTrue("Connection contains incorrect information. There should be user and server.",
				new LabeledText("Connection: ").getText().contains(DatastoreOS2.USERNAME) &&
				new LabeledText("Connection: ").getText().contains(DatastoreOS2.SERVER));
		
		assertTrue("Domain name is not shown properly. " + new LabeledText("Domain Name: ").getText() +
				" is shown instead of " + DatastoreOS2.DOMAIN,
				new LabeledText("Domain Name: ").getText().equals(DatastoreOS2.DOMAIN));
		
		assertTrue("Application name is incorrect. Was " + new LabeledText("Application Name: ").getText() +
				" but should be " + applicationName,
				new LabeledText("Application Name: ").getText().equals(applicationName));
		
		new CheckBox(0).click();
		
		assertTrue("Remote should be enabled after allowing overriding project settings.", 
				new LabeledText("Remote: ").isEnabled());
		
		serverAdapter.closeOverview();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}

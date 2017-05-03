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
package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Connection;
import org.junit.Test;

/** 
 * Test creating a new domain via context menu of connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID201NewDomainTest {

	@Test
	public void testNewDomain() {
		createDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
	}
	
	public static void createDomain(String username, String server, String domainName) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Connection connection = explorer.getOpenShift2Connection(username, server);
		connection.select();
		
		// Sometimes there is shown progress information shell
		try {
			new WaitUntil(new ShellIsAvailable("Progress Information"));
			new WaitWhile(new ShellIsAvailable("Progress Information"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
		}
		connection.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_DOMAIN).select();

		new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(domainName);
		
		new WaitUntil(new ControlIsEnabled(new FinishButton()));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			connection.getDomain(domainName);
		} catch (JFaceLayerException ex) {
			fail("Domain has not been created");
		}
	}
}

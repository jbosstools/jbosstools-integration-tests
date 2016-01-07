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

import java.util.Random;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test editing domain name via context menu.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID202EditDomainTest {

	@Test
	public void testEditDomainName() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Connection connection = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER);
				
		connection.getDomain(DatastoreOS2.DOMAIN).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_DOMAIN).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_DOMAIN);
		
		DatastoreOS2.DOMAIN = "jbdsqedomain" + new Random().nextInt(100);
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(DatastoreOS2.DOMAIN);
		
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_DOMAIN), TimePeriod.LONG);
		
		connection.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			connection.getDomain(DatastoreOS2.DOMAIN);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Domain name has not been successfully refreshed in OpenShift explorer after renaming."
					+ " Domain name should be " + DatastoreOS2.DOMAIN);
		}
	}
}

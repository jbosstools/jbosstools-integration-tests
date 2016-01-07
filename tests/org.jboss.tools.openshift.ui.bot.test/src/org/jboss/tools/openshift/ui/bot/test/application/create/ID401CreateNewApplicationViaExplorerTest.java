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
package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.junit.Test;

/**
 * Test capabilities of creating a new application via OpenShift Explorer.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID401CreateNewApplicationViaExplorerTest {

	public static String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testCreateNewApplicationViaExplorer() {
		ApplicationCreator newApplicationTemplate = new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, false);
		
		// Assertions are done inside of create method
		newApplicationTemplate.createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
}

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
package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test refresh connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID112RefreshConnectionTest {

	@Test
	public void testRefreshConnection() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();
		
		assertTrue("Connection is not refreshed - or there is no job for it", new JobIsRunning().test());
	}
	
}

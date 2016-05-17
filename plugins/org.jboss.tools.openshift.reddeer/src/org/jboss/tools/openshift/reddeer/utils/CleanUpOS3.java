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
package org.jboss.tools.openshift.reddeer.utils;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.After;
import org.junit.Test;

/** 
 * This "test" perform clean up. Clean up consists of deletion of projects 
 * for an existing OpenShift 3 connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CleanUpOS3 {
	
	@Test
	public void test() {
		// NOTHING TO DO
	}
	
	@After
	public void cleanUp() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = null;
		try {
			 connection = explorer.getOpenShift3Connection();
		} catch (JFaceLayerException ex) {
			// There is no connection with such username, nothing happens
		}
		
		if (connection != null) {
			connection.refresh();
			connection.getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME).delete();
			
			connection.refresh();
			connection.getProject(DatastoreOS3.PROJECT2).delete();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}
	}
}

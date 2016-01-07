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

import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.SecureStorage;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.Test;

/**
 * Test capabilities of secured storage for OpenShift Tools plugin.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID110SecurityStorageTest {
	
	@Test
	public void testPasswordsSecuredStorage() {
		SecureStorage.storeOpenShiftPassword(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, ServerType.OPENSHIFT_2);
		
		SecureStorage.verifySecureStorageOfPassword(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER.substring(8), true, ServerType.OPENSHIFT_2);
		
		SecureStorage.removeOpenShiftPassword(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, ServerType.OPENSHIFT_2);
		
		SecureStorage.verifySecureStorageOfPassword(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER.substring(8), false, ServerType.OPENSHIFT_2);
	}	
}

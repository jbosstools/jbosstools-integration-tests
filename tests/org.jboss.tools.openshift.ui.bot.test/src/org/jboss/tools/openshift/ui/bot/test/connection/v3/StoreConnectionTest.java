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
package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import org.jboss.tools.openshift.reddeer.requirement.OpenShiftConnectionRequirement.RequiredBasicConnection;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.SecureStorage;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.Test;

@RequiredBasicConnection()
public class StoreConnectionTest {

	@Test
	public void shouldStoreAndRemovePassword() {
		SecureStorage.storeOpenShiftPassword(DatastoreOS3.USERNAME, DatastoreOS3.SERVER, ServerType.OPENSHIFT_3);
		SecureStorage.verifySecureStorageOfPassword(
				DatastoreOS3.USERNAME, DatastoreOS3.SERVER.substring(8), true, ServerType.OPENSHIFT_3);

		SecureStorage.removeOpenShiftPassword(DatastoreOS3.USERNAME, DatastoreOS3.SERVER, ServerType.OPENSHIFT_3);
		SecureStorage.verifySecureStorageOfPassword(
				DatastoreOS3.USERNAME, DatastoreOS3.SERVER.substring(8), false, ServerType.OPENSHIFT_3);
	}	
}

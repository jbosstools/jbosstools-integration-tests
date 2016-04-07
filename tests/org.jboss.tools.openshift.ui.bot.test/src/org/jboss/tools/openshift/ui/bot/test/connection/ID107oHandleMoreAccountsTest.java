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

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Only OpenShift Online test! Test capabilites of handling more accounts in
 * OpenShift Explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID107oHandleMoreAccountsTest {

	@Test
	@RunIf(conditionClass = OS2CredentialsExist.class)
	public void testMoreAccountInOpenShiftExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		
		try {
			new DefaultShell("Loading OpenShift 2 connection details");
		} catch (RedDeerException ex) {
		}
		
		explorer.openConnectionShell();
		explorer.connectToOpenShift2(DatastoreOS2.X_SERVER,
				DatastoreOS2.X_USERNAME, DatastoreOS2.X_PASSWORD, false, false, false);
		
		try {
			explorer.getOpenShift2Connection(DatastoreOS2.X_USERNAME, DatastoreOS2.X_SERVER);
			// PASS
		} catch (JFaceLayerException ex) {
			fail("Connection has not been created. It is not listed in OpenShift explorer");
		}
	}
}

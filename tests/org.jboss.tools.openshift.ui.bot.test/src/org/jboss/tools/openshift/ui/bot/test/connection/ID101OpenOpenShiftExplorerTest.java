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

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test opening OpenShift Explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID101OpenOpenShiftExplorerTest {

	@Test
	public void testOpenOpenShiftExplorerView() {
		try {
			OpenShiftExplorerView explorer = new OpenShiftExplorerView();
			explorer.open();
			// PASS
		} catch (CoreLayerException ex) {
			fail("OpenShift Explorer view cannot be opened.");
		}
	}
}

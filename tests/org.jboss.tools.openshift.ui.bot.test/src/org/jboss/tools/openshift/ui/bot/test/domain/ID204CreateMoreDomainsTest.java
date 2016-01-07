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

import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.junit.Test;

/**
 * Test creation of more domains on one connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID204CreateMoreDomainsTest {
	
	@Test
	public void testCreateMoreDomains() {
		ID201NewDomainTest.createDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.SECOND_DOMAIN);
	}
}

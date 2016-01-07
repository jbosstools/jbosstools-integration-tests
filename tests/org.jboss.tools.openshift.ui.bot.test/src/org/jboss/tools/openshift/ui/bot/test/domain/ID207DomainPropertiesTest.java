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

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test validity of domain properties in Property view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID207DomainPropertiesTest {

	@Test
	public void testDomainProperties() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.PROPERTIES).select();
		
		PropertiesView properties = new PropertiesView();
		String suffix = properties.getProperty("Suffix").getPropertyValue();
		String id = properties.getProperty("Id").getPropertyValue();
		String fullName = properties.getProperty("Full Name").getPropertyValue();
		
		assertTrue("Domain name is not equaled with property domain name.",
				DatastoreOS2.DOMAIN.equals(id));
		
		assertTrue("Full name property is not correct. Was" + fullName
				+ " but was expected " + DatastoreOS2.DOMAIN + "." + suffix,
				(DatastoreOS2.DOMAIN + "." + suffix).equals(fullName));
	}
}

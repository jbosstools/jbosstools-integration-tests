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

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.Test;

public class ConnectionPropertiesTest {
	
	@Test
	public void testConnectionProperties() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection();
		connection.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.PROPERTIES).select();
		
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.activate();
		
		assertTrue("Property host is not valid. Was '" + propertiesView.getProperty("Host").getPropertyValue() 
				+ "' but was expected '" + DatastoreOS3.SERVER + "'", propertiesView.getProperty("Host").
					getPropertyValue().equals(DatastoreOS3.SERVER));
		assertTrue("Property user name inot valid. Was '" + propertiesView.getProperty("User Name").getPropertyValue() 
				+ "' but was expected '" + DatastoreOS3.USERNAME + "'", propertiesView.getProperty("User Name").
					getPropertyValue().equals(DatastoreOS3.USERNAME));
	}
}

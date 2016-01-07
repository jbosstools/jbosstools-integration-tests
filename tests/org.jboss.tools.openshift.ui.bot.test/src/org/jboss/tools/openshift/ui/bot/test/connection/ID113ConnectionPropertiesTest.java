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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

/**
 * Test whether connection properties are shown ok.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID113ConnectionPropertiesTest {

	@Test
	public void testConnectionProperties() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.PROPERTIES).select();
		
		PropertiesView properties = new PropertiesView();
		String hostValue = properties.getProperty("Host").getPropertyValue();
		String persistedKeyValue =  properties.getProperty("Persisted Key").getPropertyValue();
		String usernameValue = properties.getProperty("Username").getPropertyValue();
		
		assertTrue("Invalid host for connection. Was \'" + hostValue + "\', but was expected "
				+ DatastoreOS2.SERVER,
				hostValue.contains(DatastoreOS2.SERVER));
		
		String parsedPersistedKey = "https://" + DatastoreOS2.USERNAME.replace("@", "%40");
		parsedPersistedKey += "@" + DatastoreOS2.SERVER.substring(8);
		assertTrue("Invalid persisted key for connection. Was \'" + persistedKeyValue + "\', "
				+ "but was expected \'" + parsedPersistedKey + "\'",
				persistedKeyValue.equals(parsedPersistedKey));
		
		assertTrue("Invalid host for connection. Was \'" + usernameValue + "\', but was expected"
				+ DatastoreOS2.USERNAME,
				usernameValue.equals(DatastoreOS2.USERNAME));	
		
		explorer.open();
		new WaitWhile(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.LONG);
	}
}

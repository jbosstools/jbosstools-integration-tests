package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
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
		
		explorer.getConnection(Datastore.USERNAME).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.PROPERTIES).select();
		
		PropertiesView properties = new PropertiesView();
		String hostValue = properties.getProperty("Host").getPropertyValue();
		String persistedKeyValue =  properties.getProperty("Persisted Key").getPropertyValue();
		String usernameValue = properties.getProperty("Username").getPropertyValue();
		
		assertTrue("Invalid host for connection. Was \'" + hostValue + "\', but was expected "
				+ Datastore.SERVER,
				hostValue.contains(Datastore.SERVER));
		
		String parsedPersistedKey = "https://" + Datastore.USERNAME.replace("@", "%40");
		parsedPersistedKey += "@" + Datastore.SERVER.substring(8);
		assertTrue("Invalid persisted key for connection. Was \'" + persistedKeyValue + "\', "
				+ "but was expected \'" + parsedPersistedKey + "\'",
				persistedKeyValue.equals(parsedPersistedKey));
		
		assertTrue("Invalid host for connection. Was \'" + usernameValue + "\', but was expected"
				+ Datastore.USERNAME,
				usernameValue.equals(Datastore.USERNAME));	
		
		explorer.open();
		new WaitWhile(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.LONG);
	}
}

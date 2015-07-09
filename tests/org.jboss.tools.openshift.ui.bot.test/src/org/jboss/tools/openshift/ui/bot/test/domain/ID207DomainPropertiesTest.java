package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
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
		explorer.getDomain(Datastore.USERNAME, Datastore.DOMAIN).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.PROPERTIES).select();
		
		PropertiesView properties = new PropertiesView();
		String suffix = properties.getProperty("Suffix").getPropertyValue();
		String id = properties.getProperty("Id").getPropertyValue();
		String fullName = properties.getProperty("Full Name").getPropertyValue();
		
		assertTrue("Domain name is not equaled with property domain name.",
				Datastore.DOMAIN.equals(id));
		
		assertTrue("Full name property is not correct. Was" + fullName
				+ " but was expected " + Datastore.DOMAIN + "." + suffix,
				(Datastore.DOMAIN + "." + suffix).equals(fullName));
	}
}

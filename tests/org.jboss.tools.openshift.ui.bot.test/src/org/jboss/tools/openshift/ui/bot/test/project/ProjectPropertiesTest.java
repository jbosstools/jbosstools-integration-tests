package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.Test;

public class ProjectPropertiesTest {

	@Test
	public void testProjectProperties() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		OpenShiftProject project = connection.getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		project.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.PROPERTIES).select();
		
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.activate();
		
		assertTrue("Property displayed name is not correct. Property is " +
				propertiesView.getProperty("Annotations", "openshift.io/display-name").getPropertyValue() +
				" but was expected " + DatastoreOS3.PROJECT1_DISPLAYED_NAME, propertiesView.getProperty(
				"Annotations", "openshift.io/display-name").getPropertyValue().equals(DatastoreOS3.PROJECT1_DISPLAYED_NAME)); 
		assertTrue("Property name is not correct. Property is " + 
				propertiesView.getProperty("Basic", "Name").getPropertyValue() + " but was expected " +
				DatastoreOS3.PROJECT1, propertiesView.getProperty(
				"Basic", "Name").getPropertyValue().equals(DatastoreOS3.PROJECT1));
		assertTrue("Property namespace is not correct. Property is " + 
				propertiesView.getProperty("Basic", "Namespace").getPropertyValue() + " but was expected " +
				DatastoreOS3.PROJECT1, propertiesView.getProperty(
				"Basic", "Namespace").getPropertyValue().equals(DatastoreOS3.PROJECT1));
	}
	
}

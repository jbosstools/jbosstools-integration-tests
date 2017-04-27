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
package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftConnectionRequirement.RequiredBasicConnection;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftProjectRequirement.RequiredProject;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShiftProject;
import org.junit.Test;

@RequiredBasicConnection
@RequiredProject
public class ProjectPropertiesTest {

	@Test
	public void testProjectProperties() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection();
		OpenShiftProject project = connection.getProject(DatastoreOS3.TEST_PROJECT);
		project.select();
		project.openProperties();
		project.selectTabbedProperty("Details");
		
		PropertySheet propertiesView = new PropertySheet();
		String displayedName = propertiesView.getProperty("Annotations", "openshift.io/display-name").getPropertyValue();
		String name = propertiesView.getProperty("Basic", "Name").getPropertyValue();
		String namespace = propertiesView.getProperty("Basic", "Namespace").getPropertyValue();
		
		assertTrue("Property displayed name is not correct. Property is " + displayedName +
				" but was expected " + DatastoreOS3.TEST_PROJECT, 
				displayedName.equals(DatastoreOS3.TEST_PROJECT)); 
		assertTrue("Property name is not correct. Property is " + name
				 + " but was expected " + DatastoreOS3.TEST_PROJECT, name.equals(DatastoreOS3.TEST_PROJECT));
		assertTrue("Property namespace is not correct. Property is " + namespace 
				 + " but was expected " + DatastoreOS3.TEST_PROJECT, namespace.equals(DatastoreOS3.TEST_PROJECT));
	}
	
}

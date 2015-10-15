package org.jboss.tools.openshift.ui.bot.test.util;

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.junit.After;
import org.junit.Test;

/** 
 * This "test" perform clean up. Clean up consists of deletion of projects 
 * for an existing OpenShift 3 connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CleanUpOS3 {

	@Test
	public void test() {
		// NOTHING TO DO
	}
	
	@After
	public void cleanUp() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = null;
		try {
			 connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		} catch (JFaceLayerException ex) {
			// There is no connection with such username, nothing happens
		}
		
		if (connection != null) {
			connection.select();
			connection.expand();
			
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
			List<OpenShiftProject> projects = connection.getAllProjects();
			if (!projects.isEmpty()) {
				for (OpenShiftProject project: projects) {
					project.delete(); 
					connection.refresh();
				}
			}
		}
	}
}

package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import org.jboss.reddeer.junit.execution.TestMethodShouldRun;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreV3;
import org.junit.runners.model.FrameworkMethod;

/**
 * Run new connection test only if credentials for specific test are available.
 * @author mlabuda
 *
 */
public class ConnectionCredentialsExists implements TestMethodShouldRun {

	@Override
	public boolean shouldRun(FrameworkMethod method) {
		if (method.getName().contains("OAuth")) {
			return DatastoreV3.OPENSHIFT_SERVER2 != null && DatastoreV3.OPENSHIFT_USER2 != null &&
					DatastoreV3.OPENSHIFT_TOKEN2 != null;
		} else {
			return DatastoreV3.OPENSHIFT_SERVER != null && DatastoreV3.OPENSHIFT_USER != null &&
					DatastoreV3.OPENSHIFT_PASSWORD != null; 
		}
	}	
}	

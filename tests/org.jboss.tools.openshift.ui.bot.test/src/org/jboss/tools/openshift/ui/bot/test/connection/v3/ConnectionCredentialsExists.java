package org.jboss.tools.openshift.ui.bot.test.connection.v3;

import org.jboss.reddeer.junit.execution.TestMethodShouldRun;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.runners.model.FrameworkMethod;

/**
 * Run new connection test only if credentials for specific test are available.
 * @author mlabuda@redhat.com
 *
 */
public class ConnectionCredentialsExists implements TestMethodShouldRun {

	@Override
	public boolean shouldRun(FrameworkMethod method) {
		if (method.getName().contains("OAuth")) {
			return DatastoreOS3.TOKEN != null;
		} else {
			return DatastoreOS3.USERNAME != null && DatastoreOS3.PASSWORD != null; 
		}
	}	
}	

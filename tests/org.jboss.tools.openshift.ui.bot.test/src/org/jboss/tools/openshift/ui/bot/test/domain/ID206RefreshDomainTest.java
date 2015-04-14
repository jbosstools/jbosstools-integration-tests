package org.jboss.tools.openshift.ui.bot.test.domain;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test refreshment of a domain.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID206RefreshDomainTest {

	@Test
	public void testRefreshDomain() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getDomain(Datastore.USERNAME, Datastore.DOMAIN);
		
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
}

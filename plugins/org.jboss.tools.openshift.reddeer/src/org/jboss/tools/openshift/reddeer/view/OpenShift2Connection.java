package org.jboss.tools.openshift.reddeer.view;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.TreeItem;

public class OpenShift2Connection extends AbstractOpenShiftConnection {

	public OpenShift2Connection(TreeItem connectionItem) {
		super(connectionItem);
	}
	
	/** 
	 * Gets domain with specified user name and domain name.
	 * 
	 * @param domain domain name
	 * @return OpenShift 2 domain
	 */
	public Domain getDomain(String domain) {
		activateOpenShiftExplorerView();
		item.select();
		item.expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		return new Domain(treeViewerHandler.getTreeItem(item, domain));
	}
}

package org.jboss.tools.openshift.reddeer.view;

import org.jboss.reddeer.swt.api.TreeItem;

public class Domain extends AbstractOpenShiftExplorerItem {

	public Domain(TreeItem domainItem) {
		super(domainItem);
	}

	/**
	 * Get OpenShift 2 application.
	 * 
	 * @param name name of application without application type 
	 * @return OpenShift 2 application
	 */
	public OpenShift2Application getApplication(String name) {
		return new OpenShift2Application(treeViewerHandler.getTreeItem(item, name));
	}
}
	
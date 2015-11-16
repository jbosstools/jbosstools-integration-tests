package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
	
public class OpenShiftApplicationExists extends AbstractWaitCondition {

	private String username;
	private String server;
	private String domain;
	private String application;
	
	private OpenShiftExplorerView explorer;
	
	public OpenShiftApplicationExists(String username, String server, String domain, String application) {
		this.username = username;
		this.server = server;
		this.domain = domain;
		this.application = application;
		
		explorer = new OpenShiftExplorerView();
		explorer.open();
	}

	@Override
	public boolean test() {
		explorer.activate();
		
		try {
			explorer.getOpenShift2Connection(username, server).getDomain(domain).getApplication(application);
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}

	@Override
	public String description() {
		return "OpenShift Application with name " + application + " exists.";
	}
}

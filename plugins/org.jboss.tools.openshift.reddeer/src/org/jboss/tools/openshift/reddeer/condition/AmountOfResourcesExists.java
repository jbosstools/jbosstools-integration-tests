package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;

public class AmountOfResourcesExists extends AbstractWaitCondition {

	private OpenShiftProject project;
	private Resource resource;
	private int amount;
	
	public AmountOfResourcesExists(Resource resource, int amount) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		project = explorer.getOpenShift3Connection().getProject();
		this.resource = resource;
		this.amount = amount;
	}

	@Override
	public boolean test() {
		return project.getOpenShiftResources(resource).size() == amount;
	}

	@Override
	public String description() {
		return "Waiting for existence of " + amount + " " + resource.toString() + " resource(s).";  
	}
}

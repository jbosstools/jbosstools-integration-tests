package org.jboss.tools.openshift.reddeer.enums;

public enum ResourceState {

	UNSPECIFIED(""),
	PENDING("Pending"),
	RUNNING("Running"),
	COMPLETE("Complete"),
	SUCCEEDED("Succeeded"),
	FAILED("Failed");
	
	private String state;
	
	private ResourceState(String state) {
		this.state = state;
	}
	
	public String toString() {
		return state;
	}
}

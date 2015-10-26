package org.jboss.tools.openshift.reddeer.enums;

public enum Resource {

	BUILD_CONFIG("Build Configs"),
	BUILD("Builds"),
	DEPLOYMENT_CONFIG("Deployment Configs"),
	IMAGE_STREAM("Image Streams"),
	POD("Pods"),
	REPLICATION_CONTROLLER("Replication Controllers"),
	ROUTE("Routes"),
	SERVICE("Services");
	
	private final String text;
	
	private Resource(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}

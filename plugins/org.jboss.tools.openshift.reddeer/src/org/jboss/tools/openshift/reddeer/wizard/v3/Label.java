package org.jboss.tools.openshift.reddeer.wizard.v3;

/**
 * OpenShift label.
 * 
 * @author mlabuda@redhat.com
 */
public class Label {

	private String name;
	private String value;
	
	/**
	 * Creates new OpenShift label with specified name and value.
	 * 
	 * @param name name of label
	 * @param value value of label
	 */
	public Label(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
}

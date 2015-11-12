package org.jboss.tools.openshift.reddeer.wizard.v3;

/**
 * OpenShift Template parameter.
 * 
 * @author mlabuda@redhat.com
 */
public class TemplateParameter {

	private String name;
	private String value;
	
	/**
	 * Creates new template parameter with specified name and value.
	 * 
	 * @param name name of parameter
	 * @param value value of parameter
	 */
	public TemplateParameter(String name, String value) {
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

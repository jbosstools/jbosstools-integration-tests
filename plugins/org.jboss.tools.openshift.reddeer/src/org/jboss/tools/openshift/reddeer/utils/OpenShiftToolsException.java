package org.jboss.tools.openshift.reddeer.utils;

/**
 * 
 * Exception for handling exceptional situations in OpenShift tests.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenShiftToolsException extends RuntimeException {

	private static final long serialVersionUID = 13L;

	public OpenShiftToolsException() {
		super();
	}

	public OpenShiftToolsException(String msg) {
		super(msg);
	}
}

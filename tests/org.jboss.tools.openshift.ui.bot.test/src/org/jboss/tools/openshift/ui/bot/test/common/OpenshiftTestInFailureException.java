package org.jboss.tools.openshift.ui.bot.test.common;

public class OpenshiftTestInFailureException extends RuntimeException {
	
	public OpenshiftTestInFailureException(String message) {
		super(message);
	}
	
	public OpenshiftTestInFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}

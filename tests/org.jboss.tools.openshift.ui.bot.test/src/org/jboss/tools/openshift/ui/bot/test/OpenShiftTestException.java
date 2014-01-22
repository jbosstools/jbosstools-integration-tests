package org.jboss.tools.openshift.ui.bot.test;

public class OpenShiftTestException extends Exception {

	private static final long serialVersionUID = -4941409809525256859L;

	public OpenShiftTestException(String string) {
		super(string);
	}

	@Override
	public String toString() {
		return "*** OpenShift SWTBot Tests: " + super.toString() + " ***";
	}
}

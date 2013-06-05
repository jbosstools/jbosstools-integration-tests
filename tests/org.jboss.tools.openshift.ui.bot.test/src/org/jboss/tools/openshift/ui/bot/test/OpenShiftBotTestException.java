package org.jboss.tools.openshift.ui.bot.test;

public class OpenShiftBotTestException extends Exception {

	private static final long serialVersionUID = -4941409809525256859L;

	public OpenShiftBotTestException(String string) {
		super(string);
	}

	@Override
	public String toString() {
		return "*** OpenShift SWTBot Tests: " + super.toString() + " ***";
	}
}

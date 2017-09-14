package org.jboss.tools.cdi.reddeer.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.ide.eclipse.as.reddeer.server.family.JBossFamily;

public class ServerMatcher extends BaseMatcher<Object>{
	
	JBossFamily family;
	
	private ServerMatcher(JBossFamily family) {
		this.family = family;
	}

	public static ServerMatcher AS() {
		return new ServerMatcher(JBossFamily.AS);
	}
	
	public static ServerMatcher WildFly() {
		return new ServerMatcher(JBossFamily.WILDFLY);
	}
	
	public boolean matches(Object arg0) {
		return arg0.equals(family);
	}
	
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
	}
}

package org.jboss.tools.jbpm.ui.bot.test.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMRequirement.JBPM;

/**
 * 
 * @author apodhrad
 * 
 */

public class JBPMRequirement implements Requirement<JBPM> {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface JBPM {

	}

	@Override
	public boolean canFulfill() {
		return JBPMSuite.getJBPMRuntime() != null;
	}

	@Override
	public void fulfill() {
		// nothing to do here
	}

	@Override
	public void setDeclaration(JBPM declaration) {
		// nothing to do here
	}

}
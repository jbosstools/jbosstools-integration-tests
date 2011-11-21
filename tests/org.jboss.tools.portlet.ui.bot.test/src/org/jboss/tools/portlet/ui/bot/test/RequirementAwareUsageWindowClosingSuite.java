package org.jboss.tools.portlet.ui.bot.test;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.junit.runners.model.Statement;

/**
 * Normal {@link RequirementAwareSuite} with the special added functionality of 
 * closing the usage window. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RequirementAwareUsageWindowClosingSuite extends
		RequirementAwareSuite {

	public RequirementAwareUsageWindowClosingSuite(Class<?> klass)
			throws Throwable {
		super(klass);
	}

	@Override
	protected Statement withBeforeClasses(Statement statement) {
		SWTBotFactory.getJbt().closeReportUsageWindowIfOpened(false);
		return super.withBeforeClasses(statement);
	}
}

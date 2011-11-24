package org.jboss.tools.portlet.ui.bot.test.core;

import static org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.test.template.RunPortletOnServerTemplate;

/**
 * Performs Run on Server on the java portlet project and checks if there is no exception in the
 * console.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunJavaPortletOnServer extends RunPortletOnServerTemplate {

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
}

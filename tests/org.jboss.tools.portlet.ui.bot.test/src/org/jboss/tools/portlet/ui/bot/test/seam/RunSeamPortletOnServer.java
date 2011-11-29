package org.jboss.tools.portlet.ui.bot.test.seam;

import static org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.test.template.RunPortletOnServerTemplate;

/**
 * Performs Run on Server on the jsf portlet project and checks if there is no exception in the
 * console.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunSeamPortletOnServer extends RunPortletOnServerTemplate {

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
}

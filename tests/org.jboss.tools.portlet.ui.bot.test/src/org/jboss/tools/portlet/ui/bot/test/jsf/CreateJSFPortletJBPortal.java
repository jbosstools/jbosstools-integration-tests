package org.jboss.tools.portlet.ui.bot.test.jsf;

import static org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.test.template.CreateJSFSeamPortletJBPortalTemplate;

/**
 * Creates a new jsf portlet for 4.x runtime and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJSFPortletJBPortal extends CreateJSFSeamPortletJBPortalTemplate {

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
}

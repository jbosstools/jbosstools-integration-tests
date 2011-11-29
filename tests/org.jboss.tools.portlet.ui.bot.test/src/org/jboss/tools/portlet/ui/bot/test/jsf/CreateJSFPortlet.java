package org.jboss.tools.portlet.ui.bot.test.jsf;

import static org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.test.template.CreateJSFSeamPortletTemplate;

/**
 * Creates a new jsf portlet and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJSFPortlet extends CreateJSFSeamPortletTemplate {

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
}

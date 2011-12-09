package org.jboss.tools.portlet.ui.bot.test.jsf;

import static org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.test.template.CreateJSFSeamPortletRuntime5xTemplate;

/**
 * Creates a new jsf portlet for 5.x runtime and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJSFPortletRuntime5x extends CreateJSFSeamPortletRuntime5xTemplate {

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
}

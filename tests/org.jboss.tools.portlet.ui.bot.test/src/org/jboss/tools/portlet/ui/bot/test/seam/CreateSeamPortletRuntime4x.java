package org.jboss.tools.portlet.ui.bot.test.seam;

import static org.jboss.tools.portlet.ui.bot.test.seam.CreateSeamPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.test.template.CreateJSFSeamPortletRuntime4xTemplate;

/**
 * Creates a new seam portlet for 4.x runtime and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateSeamPortletRuntime4x extends CreateJSFSeamPortletRuntime4xTemplate {

	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
}

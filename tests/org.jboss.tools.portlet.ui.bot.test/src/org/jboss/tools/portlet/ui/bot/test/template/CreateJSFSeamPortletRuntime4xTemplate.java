package org.jboss.tools.portlet.ui.bot.test.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;

/**
 * Creates a new jsf portlet with 4.x runtime and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="4.3", state=ServerState.Present))
public abstract class CreateJSFSeamPortletRuntime4xTemplate extends CreateJSFSeamPortletTemplate {

	@Override
	protected List<String> getExpectedFiles() {
		return Arrays.asList(
				JSF_FOLDER + "edit.jsp",
				JSF_FOLDER + "view.jsp",
				JSF_FOLDER + "help.jsp",
				DEFAULT_OBJECTS_XML, 
				PORTLET_INSTANCES_XML, 
				JBOSS_APP_XML, 
				JBOSS_PORTLET_XML
				);
	}

	@Override
	protected List<String> getNonExpectedFiles() {
		return Collections.emptyList();
	}
}

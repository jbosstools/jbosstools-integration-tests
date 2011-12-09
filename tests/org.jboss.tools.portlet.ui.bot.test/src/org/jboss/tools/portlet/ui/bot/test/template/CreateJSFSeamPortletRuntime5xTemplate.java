package org.jboss.tools.portlet.ui.bot.test.template;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;

@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="5.0", operator=">", state=ServerState.Present))
public abstract class CreateJSFSeamPortletRuntime5xTemplate extends CreateJSFSeamPortletTemplate {

	@Override
	protected List<String> getExpectedFiles() {
		return Arrays.asList(
				JSF_FOLDER + "edit.jsp",
				JSF_FOLDER + "view.jsp",
				JSF_FOLDER + "help.jsp"
				);
	}

	@Override
	protected List<String> getNonExpectedFiles() {
		return Arrays.asList(
				DEFAULT_OBJECTS_XML, 
				PORTLET_INSTANCES_XML, 
				JBOSS_APP_XML, 
				JBOSS_PORTLET_XML);
	}
}

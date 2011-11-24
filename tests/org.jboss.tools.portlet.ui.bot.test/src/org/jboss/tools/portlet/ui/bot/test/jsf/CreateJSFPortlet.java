package org.jboss.tools.portlet.ui.bot.test.jsf;

import static org.jboss.tools.portlet.ui.bot.test.jsf.CreateJSFPortletProject.PROJECT_NAME;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JSFPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletTemplate;

/**
 * Creates a new jsf portlet and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJSFPortlet extends CreatePortletTemplate {
	
	private static final String FACES_CLASS_NAME = "javax.portlet.faces.GenericFacesPortlet";
	
	private static final String JSF_FOLDER = "WebContent/jsf/";
	
	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}

	@Override
	protected AbstractPortletCreationTask getCreatePortletTask() {
		return new JSFPortletCreationTask();
	}

	@Override
	protected List<String> getExpectedFiles() {
		return Arrays.asList(
				JSF_FOLDER + "edit.jsp",
				JSF_FOLDER + "view.jsp",
				JSF_FOLDER + "help.jsp");
	}

	@Override
	protected List<XMLNode> getExpectedXMLNodes() {
		return Arrays.asList(new XMLNode("portlet-app/portlet/portlet-class", FACES_CLASS_NAME));
	}
}

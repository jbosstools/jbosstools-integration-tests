package org.jboss.tools.portlet.ui.bot.test.core;

import static org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject.PROJECT_NAME;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JavaPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletTemplate;

/**
 * Creates a new java portlet and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJavaPortlet extends CreatePortletTemplate {

	public static final String CLASS_NAME = "UITestingJavaPortlet";
	
	private static final String PACKAGE_NAME = "org.jboss.tools.tests.ui.portlet";
	
	private static final String SOURCE_FILE_NAME = "src";
	
	private static final String CLASS_FILE = SOURCE_FILE_NAME + "/" + PACKAGE_NAME + "/" + CLASS_NAME + ".java";
	
	private static final String FULL_CLASS_NAME = PACKAGE_NAME + "." + CLASS_NAME;
	
	@Override
	protected String getProjectName() {
		return PROJECT_NAME;
	}
	
	protected AbstractPortletCreationTask getCreatePortletTask() {
		JavaPortletCreationTask task = new JavaPortletCreationTask();
		task.setProject(PROJECT_NAME);
		task.setPackageName(PACKAGE_NAME);
		task.setClassName(CLASS_NAME);
		return task;
	}
	
	@Override
	protected List<String> getExpectedFiles() {
		return Arrays.asList(
				DEFAULT_OBJECTS_XML,
				PORTLET_INSTANCES_XML,
				CLASS_FILE);
	}
	
	@Override
	protected List<String> getNonExpectedFiles() {
		return Arrays.asList(
				JSF_FOLDER,
				JBOSS_APP_XML,
				JBOSS_PORTLET_XML);
	}
	
	@Override
	protected List<XMLNode> getExpectedXMLNodes() {
		return Arrays.asList(new XMLNode("portlet-app/portlet/portlet-class", FULL_CLASS_NAME));
	}
}

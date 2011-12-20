package org.jboss.tools.portlet.ui.bot.test.template;

import static org.hamcrest.Matchers.not;
import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.file;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.containsNodes;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.exist;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.junit.Test;

/**
 * template test that creates a portlet (specified by subclasses) and asserts 
 * if the right files have been generated. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class CreatePortletTemplate extends SWTTaskBasedTestCase {

	protected static final String WEB_INF = "WebContent/WEB-INF/";

	protected static final String DEFAULT_OBJECTS_XML = WEB_INF + "default-object.xml";

	protected static final String PORTLET_INSTANCES_XML = WEB_INF + "portlet-instances.xml";

	protected static final String JBOSS_APP_XML = WEB_INF + "jboss-app.xml";

	protected static final String JBOSS_PORTLET_XML = WEB_INF + "jboss-portlet.xml";

	protected static final String JSF_FOLDER = "WebContent/jsf/";

	protected abstract String getProjectName();

	protected abstract AbstractPortletCreationTask getCreatePortletTask();

	protected abstract List<String> getExpectedFiles();

	protected abstract List<String> getNonExpectedFiles();

	protected abstract List<XMLNode> getExpectedXMLNodes();

	@Test
	public void testCreate(){
		doPerform(getCreatePortletTask());

		doAssertThatInWorkspace(0, isNumberOfErrors());
		doAssertThatInWorkspace(getExpectedWorkspaceFiles(), exist());
		if (getNonExpectedWorkspaceFiles().size() > 0){
			doAssertThatInWorkspace(getNonExpectedWorkspaceFiles(), not(exist()));
		}
		doAssertThatInWorkspace(file(getProjectName(), WEB_INF + "portlet.xml"), 
				containsNodes(getExpectedXMLNodes()));
	}

	private List<WorkspaceFile> getExpectedWorkspaceFiles(){
		return wrap(getExpectedFiles());
	}

	private List<WorkspaceFile> getNonExpectedWorkspaceFiles(){
		return wrap(getNonExpectedFiles());
	}

	private List<WorkspaceFile> wrap(List<String> files){
		List<WorkspaceFile> workspaceFiles = new ArrayList<WorkspaceFile>();

		for (String file : files){
			workspaceFiles.add(new WorkspaceFile(getProjectName(), file));
		}

		return workspaceFiles;
	}
}

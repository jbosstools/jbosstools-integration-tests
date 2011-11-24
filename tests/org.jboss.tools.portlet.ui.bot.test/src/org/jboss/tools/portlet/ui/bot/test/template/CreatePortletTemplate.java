package org.jboss.tools.portlet.ui.bot.test.template;

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

	protected abstract String getProjectName();
	
	protected abstract AbstractPortletCreationTask getCreatePortletTask();
	
	protected abstract List<String> getExpectedFiles();
	
	protected abstract List<XMLNode> getExpectedXMLNodes();
	
	@Test
	public void testCreate(){
		doPerform(getCreatePortletTask());
		
		doAssertThat(0, isNumberOfErrors());
		doAssertThat(getExpectedWorkspaceFiles(), exist());
		doAssertThat(file(getProjectName(), "WebContent/WEB-INF/portlet.xml"), 
				containsNodes(getExpectedXMLNodes()));
	}
	
	private List<WorkspaceFile> getExpectedWorkspaceFiles(){
		List<WorkspaceFile> expectedWorkspaceFiles = new ArrayList<WorkspaceFile>();
		
		for (String file : getExpectedFiles()){
			expectedWorkspaceFiles.add(new WorkspaceFile(getProjectName(), file));
		}
		
		return expectedWorkspaceFiles;
	}
}

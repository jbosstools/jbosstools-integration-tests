package org.jboss.tools.portlet.ui.bot.test.example;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.file;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.server.MarkFileAsDeployableTask;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;


/**
 * Tests the Java portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(db=@DB(required=true), seam=@Seam(version="2.0"), server=@Server(required=true, state=ServerState.Running, type=ServerType.EPP))
public class SeamPortletExample extends AbstractPortletExampleTest {

	private static final String PROJECT_NAME = "testseamportlet";
	
	private static final WorkspaceFile DATASOURCE_FILE = file(PROJECT_NAME, "resources/" + PROJECT_NAME + "-ds.xml");
	
	@Override
	public String getExampleCategory() {
		return "Portlet";
	}
	
	@Override
	public String getExampleName() {
		return "JBoss Seam Portlet Example";
	}
	
	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME};
	}
	
	@Override
	protected void executeExample() {
		doPerform(new MarkFileAsDeployableTask(DATASOURCE_FILE));
		super.executeExample();
	}
}

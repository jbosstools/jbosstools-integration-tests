package org.jboss.tools.portlet.ui.bot.test.example;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.file;
import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.portlet;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.server.RunningFileOnServerTask;
import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;


/**
 * Tests the Seam portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(db=@DB, seam=@Seam, server=@Server(version="4.3", state=ServerState.Running, type=ServerType.EPP))
public class SeamPortletExampleRuntime4x extends AbstractPortletExampleRuntime4xTest {

	private static final String PROJECT_NAME = "testseamportlet";
	
	private static final WorkspaceFile DATASOURCE_FILE = file(PROJECT_NAME, "resources/" + PROJECT_NAME + "-ds.xml");
	
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
		doPerform(new RunningFileOnServerTask(DATASOURCE_FILE));
		super.executeExample();
	}
	
	@Override
	protected PortletDefinition getPortletDefinition() {
		return portlet("TestSeamPortlet", "Test JBoss Seam Portlet");
	}
}

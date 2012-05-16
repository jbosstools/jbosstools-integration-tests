package org.jboss.tools.portlet.ui.bot.test.example;

import org.jboss.tools.ui.bot.ext.config.Annotations.DB;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;


/**
 * Tests the JSF portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(db=@DB, seam=@Seam, server=@Server(version="5.0", operator=">", state=ServerState.Running, type=ServerType.EPP))
public class SeamPortletExampleGatein extends AbstractPortletExampleGatein {
	
	private static final String PROJECT_NAME_EAR = "seam-portlet-ear";

	private static final String PROJECT_NAME_EJB = "seam-portlet-ejb";

	private static final String PROJECT_NAME_WAR = "seam-portlet-web";

	@Override
	public String getExampleName() {
		return "JBoss Portlet Bridge - Seam Portlet";
	}

	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME_EAR, PROJECT_NAME_EJB, PROJECT_NAME_WAR};
	}
}

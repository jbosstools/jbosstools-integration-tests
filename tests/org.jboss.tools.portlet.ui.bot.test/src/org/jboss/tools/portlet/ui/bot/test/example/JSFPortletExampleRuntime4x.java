package org.jboss.tools.portlet.ui.bot.test.example;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.portlet;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;


/**
 * Tests the JSF portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JSFPortletExampleRuntime4x extends AbstractPortletExampleRuntime4xTest {

	private static final String PROJECT_NAME = "testjsfportlet";
	
	@Override
	public String getExampleName() {
		return "JBoss JSF Portlet Example";
	}
	
	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME};
	}
	
	@Override
	protected PortletDefinition getPortletDefinition() {
		return portlet("TestJSFPortlet", "Test JBoss JSF Portlet");
	}
}

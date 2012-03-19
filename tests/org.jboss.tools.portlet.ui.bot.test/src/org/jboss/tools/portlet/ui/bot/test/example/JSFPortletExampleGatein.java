package org.jboss.tools.portlet.ui.bot.test.example;

/**
 * Tests the JSF portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JSFPortletExampleGatein extends AbstractPortletExampleGatein {

	private static final String PROJECT_NAME = "JSFPortlet";
	
	@Override
	public String getExampleName() {
		return "JBoss JSF RI Portlet";
	}
	
	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME};
	}
}

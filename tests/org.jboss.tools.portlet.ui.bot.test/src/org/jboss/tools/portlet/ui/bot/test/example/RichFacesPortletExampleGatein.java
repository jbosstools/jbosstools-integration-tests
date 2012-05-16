package org.jboss.tools.portlet.ui.bot.test.example;


/**
 * Tests the JSF portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RichFacesPortletExampleGatein extends AbstractPortletExampleGatein {

	private static final String PROJECT_NAME_IN_WORKSPACE = "RichfacesPortlet";
	
	@Override
	public String getExampleName() {
		return "JBoss Portlet Bridge - RichFaces Application";
	}
	
	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME_IN_WORKSPACE};
	}
}

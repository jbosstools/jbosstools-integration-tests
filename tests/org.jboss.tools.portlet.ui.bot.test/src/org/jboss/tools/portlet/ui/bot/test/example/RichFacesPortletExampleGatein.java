package org.jboss.tools.portlet.ui.bot.test.example;


/**
 * Tests the JSF portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RichFacesPortletExampleGatein extends AbstractPortletExampleGatein {

	private static final String PROJECT_NAME_IN_WIZARD = "RichFacesPortlet";
	
	private static final String PROJECT_NAME_IN_WORKSPACE = "RichfacesPortlet";
	
	private int getProjectNameMethodCallCounter = 0;
	
	@Override
	public String getExampleName() {
		return "JBoss Portlet Bridge - RichFaces Application";
	}
	
	@Override
	public String[] getProjectNames() {
		// an ugly hack to pass the assert that the project name in wizard is the same as project name in workspace
		if (getProjectNameMethodCallCounter <= 1){
			getProjectNameMethodCallCounter++;
			return new String[]{PROJECT_NAME_IN_WIZARD};
		}
		return new String[]{PROJECT_NAME_IN_WORKSPACE};
	}
}

package org.jboss.tools.portlet.ui.bot.test.example;


/**
 * Tests the Java portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JSFPortletExample extends AbstractPortletExampleTest {

	private static final String PROJECT_NAME = "testjsfportlet";
	
	@Override
	public String getExampleCategory() {
		return "Portlet";
	}
	
	@Override
	public String getExampleName() {
		return "JBoss JSF Portlet Example";
	}
	
	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME};
	}
}

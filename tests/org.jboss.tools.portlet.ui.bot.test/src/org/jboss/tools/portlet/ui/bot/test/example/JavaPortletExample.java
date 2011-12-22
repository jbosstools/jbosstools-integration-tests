package org.jboss.tools.portlet.ui.bot.test.example;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.portlet;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;


/**
 * Tests the Java portlet example. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JavaPortletExample extends AbstractPortletExampleTest {

	private static final String PROJECT_NAME = "testjavaportlet";
	
	@Override
	public String getExampleCategory() {
		return "Portlet";
	}
	
	@Override
	public String getExampleName() {
		return "JBoss Java Portlet Example";
	}
	
	@Override
	public String[] getProjectNames() {
		return new String[]{PROJECT_NAME};
	}
	
	@Override
	protected PortletDefinition getPortletDefinition() {
		return portlet("default/TestJavaPortlet", "TestJavaPortlet");
	}
}

package org.jboss.tools.portlet.ui.bot.entity;

/**
 * Convenient factory method for creating entity objects. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class EntityFactory {

	private EntityFactory() {
		// static factory methods
	}
	
	public static WorkspaceFile file(String project, String filePath){
		return new WorkspaceFile(project, filePath);
	}
	
	public static PortletDefinition portlet(String page){
		return new PortletDefinition(page);
	}
	
	public static PortletDefinition portlet(String page, String displayName){
		return new PortletDefinition(page, displayName);
	}
}

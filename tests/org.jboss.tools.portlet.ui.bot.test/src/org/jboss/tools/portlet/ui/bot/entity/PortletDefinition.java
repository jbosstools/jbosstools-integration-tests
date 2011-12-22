package org.jboss.tools.portlet.ui.bot.entity;

/**
 * Describes a portlet. If the display name is not specified, the name is used as display name. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PortletDefinition {

	private String page;
	
	private String displayName;

	public PortletDefinition(String name) {
		this(name, name);
	}
	
	public PortletDefinition(String name, String displayName) {
		this.page = name;
		this.displayName = displayName;
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return "Portlet " + getPage() + "[" + getDisplayName() + "]";
	}
}

package org.jboss.tools.portlet.ui.bot.entity;

/**
 * Holds values for project facets. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class FacetDefinition {

	private String category;

	private String name;
	
	private String version;
	
	public FacetDefinition(String name) {
		super();
		this.name = name;
	}

	public FacetDefinition(String name, String category) {
		super();
		this.category = category;
		this.name = name;
	}
	
	public FacetDefinition(String name, String category, String version) {
		super();
		this.category = category;
		this.name = name;
		this.version = version;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "Facet " + getCategory() + "/" + getName() + " in version " + getVersion();
	}
}
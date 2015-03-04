package org.jboss.tools.hibernate.reddeer.entity;

/**
 * Holds values for project facets. 
 * 
 */
public class FacetDefinition {

	private String category;

	private String name;
	
	private String version;
	
	/**
	 * Initializes facet definition 
	 * @param name given definition name
	 */
	public FacetDefinition(String name) {
		super();
		this.name = name;
	}

	/**
	 * Initialize facet definition
	 * @param name given name
	 * @param category given facet category
	 */
	public FacetDefinition(String name, String category) {
		super();
		this.category = category;
		this.name = name;
	}
	/**
	 * Initialize facet definition
	 * @param name given anme
	 * @param category given category
	 * @param version given version
	 */
	public FacetDefinition(String name, String category, String version) {
		super();
		this.category = category;
		this.name = name;
		this.version = version;
	}

	/**
	 * Return facet category
	 * @return facet category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets facet category
	 * @param category given category 
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Return facet name
	 * @return facet name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets facet name
	 * @param name given facet name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets facet version
	 * @return facet version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Set facet version
	 * @param version given facet version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "Facet " + getCategory() + "/" + getName() + " in version " + getVersion();
	}
}
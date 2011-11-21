package org.jboss.tools.portlet.ui.bot.test.task.facet;

public class FacetDefinition {

	private String category;

	private String name;
	
	public FacetDefinition(String name) {
		super();
		this.name = name;
	}

	public FacetDefinition(String name, String category) {
		super();
		this.category = category;
		this.name = name;
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
}
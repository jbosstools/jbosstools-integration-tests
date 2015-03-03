package org.jboss.tools.hibernate.reddeer.entity;

import org.jboss.tools.hibernate.reddeer.entity.FacetDefinition;

/**
 * Set of facet definitions
 * @author jpeterka
 *
 */
public class Facets {

	public static final FacetDefinition JAVA_FACET = new FacetDefinition("Java", null, "1.6");
	
	public static final FacetDefinition JPA20 = new FacetDefinition("JPA", null, "2.0");
	
	public static final FacetDefinition JPA21 = new FacetDefinition("JPA", null, "2.1");
}

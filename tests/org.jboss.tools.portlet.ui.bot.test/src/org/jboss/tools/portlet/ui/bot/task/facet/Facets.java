package org.jboss.tools.portlet.ui.bot.task.facet;

import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;

public class Facets {

	public static final FacetDefinition JAVA_FACET = new FacetDefinition("Java", null, "1.6");
	
	public static final FacetDefinition JSF_FACET = new FacetDefinition("JavaServer Faces");
	
	private static final String JBOSS_FACET_CATEGORY = "JBoss Portlets";
	
	public static final FacetDefinition CORE_PORTLET_FACET = new FacetDefinition("JBoss Core Portlet", JBOSS_FACET_CATEGORY);
	
	public static final FacetDefinition JSF_PORTLET_FACET = new FacetDefinition("JBoss JSF Portlet", JBOSS_FACET_CATEGORY);
	
	public static final FacetDefinition SEAM_PORTLET_FACET = new FacetDefinition("JBoss Seam Portlet", JBOSS_FACET_CATEGORY);

}

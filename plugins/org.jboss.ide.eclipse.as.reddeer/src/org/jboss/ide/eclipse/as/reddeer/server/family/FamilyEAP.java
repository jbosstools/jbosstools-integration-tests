package org.jboss.ide.eclipse.as.reddeer.server.family;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Server family: Enterprise Application Platform
 * 
 * @author psrna, Radoslav Rabara
 *
 */
@XmlRootElement(name="familyEAP", namespace="http://www.jboss.org/NS/ServerReq")
public class FamilyEAP implements ServerFamily {

	private final String category = "JBoss Enterprise Middleware";
	
	private final String label = "JBoss Enterprise Application Platform";
	
	@XmlAttribute(name="version")
	private String version;
	
	@Override
	public String getCategory() {
		return category;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getVersion() {
		return version;
	}
}

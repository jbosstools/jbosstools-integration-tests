package org.jboss.ide.eclipse.as.reddeer.server.family;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Server family: WildFly
 * 
 * @author Radoslav Rabara
 *
 */
@XmlRootElement(name="familyWildFly", namespace="http://www.jboss.org/NS/ServerReq")
public class FamilyWildFly implements ServerFamily {

	private final String category = "JBoss Community";
	
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

package org.jboss.ide.eclipse.as.reddeer.server.family;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.reddeer.requirements.server.IServerFamily;

/**
 * Server family: Enterprise Application Platform
 * 
 * @author psrna, Radoslav Rabara
 *
 */
@XmlRootElement(name="familyEAP", namespace="http://www.jboss.org/NS/ServerReq")
public class FamilyEAP implements IServerFamily {

	private final String category = "Red Hat JBoss Middleware";
	
	private final String label = "Red Hat JBoss Enterprise Application Platform";
	
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

package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.ide.eclipse.as.reddeer.server.family.*;
import org.jboss.reddeer.requirements.server.IServerFamily;
import org.jboss.reddeer.requirements.server.IServerReqConfig;

/**
 * 
 * @author psrna, Radoslav Rabara
 *
 */

@XmlRootElement(name="server-requirement", namespace="http://www.jboss.org/NS/ServerReq")
public class ServerRequirementConfig implements IServerReqConfig {
	
	private String runtime;
	
	@XmlElementWrapper(name="type", namespace="http://www.jboss.org/NS/ServerReq")
	@XmlElements({
		@XmlElement(name="familyAS", namespace="http://www.jboss.org/NS/ServerReq", type = FamilyAS.class),
		@XmlElement(name="familyEAP", namespace="http://www.jboss.org/NS/ServerReq", type = FamilyEAP.class),
		@XmlElement(name="familyWildFly", namespace="http://www.jboss.org/NS/ServerReq", type = FamilyWildFly.class)
	})
	private List<IServerFamily> family;
	
	public IServerFamily getServerFamily(){
		return this.family.get(0); //always: size() == 1 
	}
	
	public String getRuntime() {
		return runtime;
	}

	@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	
	public boolean equals(Object arg) {
		if(arg == null || !(arg instanceof ServerRequirementConfig))
			return false;
		if(arg == this)
			return true;
		ServerRequirementConfig conf = (ServerRequirementConfig) arg;
		IServerFamily family1 = this.getServerFamily();
		IServerFamily family2 = conf.getServerFamily();
		if(!runtime.equals(conf.runtime) || (family1 == null && family2 != null))
			return false;
		return family1.getLabel().equals(family2.getLabel()) && family1.getVersion().equals(family2.getVersion());
	}
}
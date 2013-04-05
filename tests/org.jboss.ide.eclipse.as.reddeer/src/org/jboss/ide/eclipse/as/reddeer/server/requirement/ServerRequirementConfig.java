package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="server-requirement", namespace="http://www.jboss.org/NS/ServerReq")
public class ServerRequirementConfig {
	
	private String runtime;

	public String getRuntime() {
		return runtime;
	}

	@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	
	
}

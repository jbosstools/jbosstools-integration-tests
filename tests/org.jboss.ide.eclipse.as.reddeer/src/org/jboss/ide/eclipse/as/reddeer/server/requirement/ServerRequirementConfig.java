package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="server-requirement", namespace="http://www.jboss.org/NS/ServerReq")
public class ServerRequirementConfig {
	
	private String version;
	
	private String runtime;

	public String getVersion() {
		return version;
	}

	@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getRuntime() {
		return runtime;
	}

	@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
}

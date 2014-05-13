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

@XmlRootElement(name="jboss-server-requirement", namespace="http://www.jboss.org/NS/ServerReq")
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
	
	@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
	private Remote remote;
	
	public Remote getRemote(){
		return remote;
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
	
	@XmlRootElement(name="remote", namespace="http://www.jboss.org/NS/ServerReq")
	public static class Remote {
		

		private String host;
		private String remoteServerHome;
		private String username;
		private String password;
		private boolean isExternallyManaged;
		
		public String getHost() {
			return host;
		}
		
		@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
		public void setHost(String hostname) {
			this.host = hostname;
		}
		
		public String getRemoteServerHome(){
			return remoteServerHome;
		}
		
		@XmlElement(namespace="http://www.jboss.org/NS/ServerReq", name="remote-server-home")
		public void setRemoteServerHome(String home){
			this.remoteServerHome = home;
		}
		
		public String getUsername(){
			return username;
		}
		
		@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
		public void setUsername(String username){
			this.username = username;
		}
		
		public String getPassword(){
			return password;
		}
		
		@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
		public void setPassword(String password){
			this.password = password;
		}
		
		public boolean getIsExternallyManaged(){
			return isExternallyManaged;
		}
		
		@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
		public void setIsExternallyManaged(boolean isExternallyManaged){
			this.isExternallyManaged = isExternallyManaged;
		}
	}
	
}
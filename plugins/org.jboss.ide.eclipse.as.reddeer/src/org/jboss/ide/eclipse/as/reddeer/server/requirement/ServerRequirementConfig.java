package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author psrna, Radoslav Rabara
 *
 */

@XmlRootElement(name="server-requirement", namespace="http://www.jboss.org/NS/ServerReq")
public class ServerRequirementConfig {
	
	private String runtime;
	
	@XmlElementWrapper(name="type", namespace="http://www.jboss.org/NS/ServerReq")
	@XmlElements({
		@XmlElement(name="familyAS", namespace="http://www.jboss.org/NS/ServerReq", type = FamilyAS.class),
		@XmlElement(name="familyEAP", namespace="http://www.jboss.org/NS/ServerReq", type = FamilyEAP.class)
	})
	private List<ServerFamily> family;
	
	public ServerFamily getServerFamily(){
		return this.family.get(0); //always: size() == 1 
	}
	
	public String getRuntime() {
		return runtime;
	}

	@XmlElement(namespace="http://www.jboss.org/NS/ServerReq")
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	
	
	@XmlRootElement(name="familyAS", namespace="http://www.jboss.org/NS/ServerReq")
	public static class FamilyAS implements ServerFamily {

		private final String category = "JBoss Community";
		
		private final String label = "JBoss AS";
		
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

		@XmlAttribute(name="version")
		public void setVersion(String version){
			this.version = version;
		}
		
	}
	
	@XmlRootElement(name="familyEAP", namespace="http://www.jboss.org/NS/ServerReq")
	public static class FamilyEAP implements ServerFamily {

		private final String category = "JBoss Enterprise Middleware";
		
		private final String label = "JBoss Enterprise Application Platform";
		
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
		
		@XmlAttribute(name="version")
		public void setVersion(String version){
			this.version = version;
		}


	}
	
	public interface ServerFamily {
		
		public String getCategory();
		
		public String getLabel();
		
		public String getVersion();
		
	}
	
	public boolean equals(Object arg) {
		if(arg == null || !(arg instanceof ServerRequirementConfig))
			return false;
		if(arg == this)
			return true;
		ServerRequirementConfig conf = (ServerRequirementConfig) arg;
		ServerFamily family1 = this.getServerFamily();
		ServerFamily family2 = conf.getServerFamily();
		if(!runtime.equals(conf.runtime) || (family1 == null && family2 != null))
			return false;
		return family1.getLabel().equals(family2.getLabel()) && family1.getVersion().equals(family2.getVersion());
	}
}
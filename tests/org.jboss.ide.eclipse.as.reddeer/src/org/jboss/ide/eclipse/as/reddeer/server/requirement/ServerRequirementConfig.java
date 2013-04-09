package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author psrna
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

		private String label = "JBoss AS";
		private String version;	
		
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

		private String label = "JBoss Enterprise Application Platform";
		private String version;
		
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
		
		public String getLabel();
		
		public String getVersion();
		
	}
	
}

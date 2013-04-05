package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;

public abstract class ServerRequirement implements Requirement<Server>, CustomConfiguration<ServerRequirementConfig> {

	private ServerRequirementConfig config;
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {
	}
	
	
	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		System.out.println("Hello from server requirement");
	}

	@Override
	public void setDeclaration(Server declaration) {
	}
	

	@Override
	public Class getConfigurationClass() {
		return ServerRequirementConfig.class;
	}

	@Override
	public void setConfiguration(ServerRequirementConfig config) {
		this.config = config;
	}

	public ServerRequirementConfig getConfig(){
		return this.config;
	}

	
}

package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.NewServerWizardDialog;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.DefineNewServerWizardPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.JBossRuntimeWizardPage;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;

/**
 * 
 * @author psrna
 *
 */

public class ServerRequirement implements Requirement<Server>, CustomConfiguration<ServerRequirementConfig> {

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

		NewServerWizardDialog serverW = new NewServerWizardDialog();
		serverW.open();
		
		DefineNewServerWizardPage sp = new DefineNewServerWizardPage(serverW);

		String serverName = config.getServerFamily().getLabel() + " " 
				  + config.getServerFamily().getVersion() + " Server";
		String serverType = config.getServerFamily().getLabel() + " " 
				  + config.getServerFamily().getVersion();

		sp.setName(serverName);
		sp.selectType(serverType);
		serverW.next();
		
		String runtimeName = config.getServerFamily().getLabel() + " " 
				   + config.getServerFamily().getVersion() + " Runtime";
		
		JBossRuntimeWizardPage rp = new JBossRuntimeWizardPage();
		rp.setRuntimeName(runtimeName);
		rp.setRuntimeDir(config.getRuntime());
		
		serverW.finish();
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

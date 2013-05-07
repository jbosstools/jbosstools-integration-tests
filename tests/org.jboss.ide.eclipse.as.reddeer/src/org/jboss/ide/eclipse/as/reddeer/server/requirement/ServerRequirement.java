package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.NewServerWizardDialog;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.DefineNewServerWizardPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.JBossRuntimeWizardPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;

/**
 * 
 * @author psrna
 *
 */

public class ServerRequirement implements Requirement<Server>, CustomConfiguration<ServerRequirementConfig> {

	private ServerRequirementConfig config;
	private Server server;
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {
		boolean started() default true;
	}
	
	
	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		
		setupServerAdapter();
		if(server.started()){
			//TODO
		}
		
	}

	@Override
	public void setDeclaration(Server server) {
		this.server = server;
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
		
	public String getServerTypeLabelText(){
		return config.getServerFamily().getLabel() + " " 
	         + config.getServerFamily().getVersion();
	}
	
	public String getServerNameLabelText(){
		return getServerTypeLabelText() + " Server";
	}

	public String getRuntimeNameLabelText(){
		return getServerTypeLabelText() + " Runtime";
	}
	
	public void setupServerAdapter(){
		
		NewServerWizardDialog serverW = new NewServerWizardDialog();
		serverW.open();
		
		DefineNewServerWizardPage sp = new DefineNewServerWizardPage(serverW);

		sp.setName(getServerNameLabelText());
		sp.selectType(getServerTypeLabelText());
		serverW.next();
		
		JBossRuntimeWizardPage rp = new JBossRuntimeWizardPage();
		rp.setRuntimeName(getRuntimeNameLabelText());
		rp.setRuntimeDir(config.getRuntime());
		
		serverW.finish();
		
	}
	
}

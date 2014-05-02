package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.NewServerWizardDialog;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.JBossRuntimeWizardPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerAdapterPage;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerWizardPageWithErrorCheck;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.junit.requirement.CustomConfiguration;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.requirements.server.ConfiguredServerInfo;
import org.jboss.reddeer.requirements.server.ServerReqBase;
import org.jboss.reddeer.requirements.server.ServerReqState;

/**
 * 
 * @author psrna, Radoslav Rabara
 *
 */

public class ServerRequirement extends ServerReqBase implements Requirement<JBossServer>, CustomConfiguration<ServerRequirementConfig> {

	private static final Logger LOGGER = Logger.getLogger(ServerRequirement.class);
	
	private static ConfiguredServerInfo lastServerConfiguration;
	
	private ServerRequirementConfig config;
	private JBossServer server;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface JBossServer {
		ServerReqState state() default ServerReqState.RUNNING;
		ServerReqType type() default ServerReqType.ANY;
		ServerReqVersion version() default ServerReqVersion.EQUAL;
	}
	
	
	@Override
	public boolean canFulfill() {
		//requirement can be fulfilled only when required server's type and version matches to
		//configured server's type and version
		return ServerMatcher.matchServerFamily(server.type().getFamily(), config.getServerFamily()) &&
				ServerMatcher.matchServerVersion(server.type().getVersion(), server.version(),
				config.getServerFamily().getVersion());
	}

	@Override
	public void fulfill() {
		if(lastServerConfiguration != null) {
			boolean differentConfig = !config.equals(lastServerConfiguration.getConfig());
			if (differentConfig) {
				removeLastRequiredServer(lastServerConfiguration);
				lastServerConfiguration = null;
			}
		}
		if (lastServerConfiguration == null || !isLastConfiguredServerPresent(lastServerConfiguration)) {
			LOGGER.info("Setup server");
			setupServerAdapter();
			lastServerConfiguration = new ConfiguredServerInfo(getServerNameLabelText(config), config);
		}
		setupServerState(server.state(), lastServerConfiguration);
	}
	
	
	@Override
	public void setDeclaration(JBossServer server) {
		this.server = server;
	}

	@Override
	public Class<ServerRequirementConfig> getConfigurationClass() {
		return ServerRequirementConfig.class;
	}

	@Override
	public void setConfiguration(ServerRequirementConfig config) {
		this.config = config;
	}

	public ServerRequirementConfig getConfig() {
		return this.config;
	}
	

	protected void setupServerAdapter() {
		NewServerWizardDialog serverW = new NewServerWizardDialog();
		try {
			serverW.open();
			
			NewServerWizardPageWithErrorCheck sp = new NewServerWizardPageWithErrorCheck();
	
			sp.selectType(config.getServerFamily().getCategory(),
					getServerTypeLabelText(config));
			sp.setName(getServerNameLabelText(config));
			
			sp.checkErrors();
			
			serverW.next();
			
			NewServerAdapterPage ap = new NewServerAdapterPage();
			ap.checkErrors();
			
			serverW.next();
			
			JBossRuntimeWizardPage rp = new JBossRuntimeWizardPage();
			rp.setRuntimeName(getRuntimeNameLabelText(config));
			rp.setRuntimeDir(config.getRuntime());
			
			rp.checkErrors();
			
			serverW.finish();
		} catch(RuntimeException e) {
			serverW.cancel();
			throw e;
		} catch(AssertionError e) {
			serverW.cancel();
			throw e;
		}
	}

}

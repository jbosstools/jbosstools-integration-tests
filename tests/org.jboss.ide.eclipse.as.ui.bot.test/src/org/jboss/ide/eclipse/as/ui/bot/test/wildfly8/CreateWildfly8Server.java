package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqOperator;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.ui.bot.test.template.CreateServerTemplate;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.ui.bot.ext.entity.XMLConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@CleanWorkspace
@Server(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY, version="8.0", operator=ServerReqOperator.GREATER_OR_EQUAL)
public class CreateWildfly8Server extends CreateServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 
	
	@Override
	protected void assertEditorPorts() {
		assertThat("8080", is(editor.getWebPort()));
		assertThat("9990", is(editor.getManagementPort()));		
	}

	@Override
	protected void assertViewPorts(List<XMLConfiguration> configurations) {
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Management", "${jboss.management.http.port:9990}")));
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Web", "${jboss.http.port:8080}")));
		assertThat(configurations, hasItem(new XMLConfiguration("Port Offset", "${jboss.socket.binding.port-offset:0}")));
	}

}

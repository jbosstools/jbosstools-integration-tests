package org.jboss.ide.eclipse.as.ui.bot.test.eap6x;

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
@Server(state=ServerReqState.PRESENT, type=ServerReqType.EAP, version="6.1", operator=ServerReqOperator.GREATER_OR_EQUAL)
public class CreateEAP6xServer extends CreateServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	@Override
	protected String getServerName() {
		return requirement.getServerNameLabelText();
	} 
	
	@Override
	protected void assertEditorPorts() {
		assertThat("8080", is(editor.getWebPort()));
		assertThat("9999", is(editor.getManagementPort()));
	}

	@Override
	protected void assertViewPorts(List<XMLConfiguration> configurations) {
		assertThat(configurations, hasItem(new XMLConfiguration(
				"JBoss Management", "${jboss.management.native.port:9999}")));
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Web",
				"8080")));
	}

}

package org.jboss.ide.eclipse.as.ui.bot.test.eap6x;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqVersion;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.XMLConfiguration;
import org.jboss.ide.eclipse.as.ui.bot.test.template.CreateServerTemplate;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.server.ServerReqState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@CleanWorkspace
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.EAP6_1plus, version=ServerReqVersion.GREATER_OR_EQUAL)
public class CreateEAP6xServer extends CreateServerTemplate {

	@Override
	protected void assertEditorPorts() {
		assertThat(editor.getWebPort(), is("8080"));
		assertThat(editor.getManagementPort(), is("9999"));		
	}

	@Override
	protected void assertViewPorts(List<XMLConfiguration> configurations) {
		assertThat(configurations, hasItem(new XMLConfiguration(
				"JBoss Management", "${jboss.management.native.port:9999}")));
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Web",
				"8080")));
	}
}

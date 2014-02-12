package org.jboss.ide.eclipse.as.ui.bot.test.as70;



import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.Server;
import org.jboss.ide.eclipse.as.reddeer.server.view.XMLConfiguration;
import org.jboss.ide.eclipse.as.ui.bot.test.template.CreateServerTemplate;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 *
 * @see CreateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@CleanWorkspace
@Server(state=ServerReqState.PRESENT, type=ServerReqType.AS, version="7.0")
public class CreateAS70Server extends CreateServerTemplate {

	@Override
	protected void assertEditorPorts() {
		assertThat("8080", is(editor.getWebPort()));
		assertThat("9999", is(editor.getManagementPort()));		
	}

	@Override
	protected void assertViewPorts(List<XMLConfiguration> configurations) {
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Management", "9999")));
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Web", "8080")));		
	}
}

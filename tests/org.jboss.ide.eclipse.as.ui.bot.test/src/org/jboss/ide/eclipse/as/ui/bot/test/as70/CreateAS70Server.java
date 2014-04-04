package org.jboss.ide.eclipse.as.ui.bot.test.as70;



import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
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
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_0)
public class CreateAS70Server extends CreateServerTemplate {

	@Override
	protected void assertEditorPorts() {
		assertThat(editor.getWebPort(), is("8080"));
		assertThat(editor.getManagementPort(), is("9999"));		
	}

	@Override
	protected void assertViewPorts(List<XMLConfiguration> configurations) {
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Management", "9999")));
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Web", "8080")));		
	}
}

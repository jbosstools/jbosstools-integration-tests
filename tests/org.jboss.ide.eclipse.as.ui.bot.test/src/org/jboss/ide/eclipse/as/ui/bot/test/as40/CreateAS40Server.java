package org.jboss.ide.eclipse.as.ui.bot.test.as40;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.XMLConfiguration;
import org.jboss.ide.eclipse.as.ui.bot.test.template.CreateServerTemplate;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.fail;

/**
*
* @see CreateServerTemplate
* @author Lucia Jelinkova
*
*/
@CleanWorkspace

@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS4_0)
public class CreateAS40Server extends CreateServerTemplate {

	@Override
	protected void assertEditorPorts() {
		assertThat(editor.getWebPort(), is("8080"));
		assertThat(editor.getJNDIPort(), is("1099"));		
	}

	@Override
	protected void assertViewPorts(List<XMLConfiguration> configurations) {
		for (XMLConfiguration config : configurations){
			assertValueIsNumber(config);
		}
	}

	private void assertValueIsNumber(XMLConfiguration config){
		try {
			Integer.parseInt(config.getValue());
		} catch (NumberFormatException e){
			fail(config + " does not a numeric value");
		}
	}
}

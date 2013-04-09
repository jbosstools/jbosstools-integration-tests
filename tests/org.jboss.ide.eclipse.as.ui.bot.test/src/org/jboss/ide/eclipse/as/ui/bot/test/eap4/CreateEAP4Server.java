package org.jboss.ide.eclipse.as.ui.bot.test.eap4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.List;

import org.jboss.ide.eclipse.as.ui.bot.test.template.CreateServerTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.entity.XMLConfiguration;

/**
*
* @see CreateServerTemplate
* @author Lucia Jelinkova
*
*/
@Require(server=@Server(type=ServerType.EAP, version="4.3", state=ServerState.Present))
public class CreateEAP4Server extends CreateServerTemplate {

	@Override
	protected void assertEditorPorts() {
		assertThat("8080", is(editor.getWebPort()));
		assertThat("1099", is(editor.getJNDIPort()));		
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

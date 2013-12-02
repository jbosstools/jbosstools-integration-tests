package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

import java.util.List;

import org.jboss.ide.eclipse.as.ui.bot.test.template.CreateServerTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.entity.XMLConfiguration;

@Require(server=@Server(type=ServerType.WildFly, version="8", state=ServerState.Present))
public class CreateWildfly8Server extends CreateServerTemplate {

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

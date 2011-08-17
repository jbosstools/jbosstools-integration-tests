package org.jboss.tools.esb.ui.bot.tests.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.SOA,state=ServerState.Running))
public class HelloWorldFileAction extends ESBExampleTest {
	@Override
	public String getExampleName() {
		return "JBoss ESB HelloWorld File Action Example - ESB";
	}
	@Override
	public String[] getProjectNames() {
		return new String[] {"helloworld_file_action","helloworld_file_action_client"};
	}
	@Override
	protected void executeExample() {
		fail("Example execution not yet implemented");
	}
}

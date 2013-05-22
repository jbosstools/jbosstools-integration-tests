package org.jboss.tools.switchyard.ui.bot.test.suite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerLabel;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;

/**
 * This requirement ensures that a given server will be running or stopped.
 * 
 * @author apodhrad
 * 
 */
public class ServerRequirement implements Requirement<Server> {

	private Server serverConf;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Server {

		Type type();

		State state();
	}

	@Override
	public boolean canFulfill() {
		return SwitchyardSuite.getServerName() != null;
	}

	@Override
	public void fulfill() {
		org.jboss.reddeer.eclipse.wst.server.ui.view.Server server = null;
		server = new ServersView().getServer(SwitchyardSuite.getServerName());

		ServerLabel serverLabel = server.getLabel();
		// Server should be running
		if (serverConf.state().equals(State.RUNNING)) {
			if (serverLabel.getState().equals(ServerState.STOPPED)) {
				server.start();
			}
		}
		// Server should not be running
		if (serverConf.state().equals(State.NOT_RUNNING)) {
			if (serverLabel.getState().equals(ServerState.STARTED)) {
				server.stop();
			}
		}
	}

	@Override
	public void setDeclaration(Server serverConf) {
		this.serverConf = serverConf;
	}

	public enum Type {
		ALL
	}

	public enum State {
		PRESENT, RUNNING, NOT_RUNNING, DISABLED
	}

}

package org.jboss.ide.eclipse.as.reddeer.server.view;

import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;

/**
 * Represents the Servers view with JBoss server specific options. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossServerView extends ServersView {

	@Override
	public JBossServer getServer(String name) {
		Server server = super.getServer(name);
		if (!(server instanceof JBossServer)){
			throw new IllegalStateException("Unexpected Server subtype. Expected: " + JBossServer.class + " but was: " + server.getClass());
		}
		return (JBossServer) server;
	}
	
	@Override
	protected Server createServer(TreeItem item) {
		return new JBossServer(item);
	}
}

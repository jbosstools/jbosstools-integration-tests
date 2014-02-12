package org.jboss.ide.eclipse.as.reddeer.server.view;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;

/**
 * Represents a module assigned to JBoss server {@link JBossServer} and contains
 * operations specific for this kind of server. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossServerModule extends ServerModule {

	public JBossServerModule(TreeItem item) {
		super(item);
	}

	public void openWebPage(){
		treeItem.select();
		new ContextMenu("Show In", "Web Browser").select();
	}
}

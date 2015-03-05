package org.jboss.ide.eclipse.as.reddeer.server.view;

import org.jboss.ide.eclipse.as.reddeer.server.editor.ServerModuleWebPageEditor;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServerModule;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
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

	/**
	 * @deprecated Use {@link #JBossServerModule(TreeItem, JBossServerView)}
	 * @param item
	 */
	public JBossServerModule(TreeItem item) {
		super(item);
	}
	
	protected JBossServerModule(TreeItem item, ServersView view) {
		super(item, view);
	}

	public ServerModuleWebPageEditor openWebPage(){
		activate();
		new ContextMenu("Show In", "Web Browser").select();
		return new ServerModuleWebPageEditor(getLabel().getName());
	}
}

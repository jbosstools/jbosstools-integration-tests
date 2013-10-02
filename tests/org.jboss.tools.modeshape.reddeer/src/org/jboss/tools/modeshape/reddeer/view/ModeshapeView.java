package org.jboss.tools.modeshape.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.modeshape.reddeer.wizard.ModeshapeServerWizard;

/**
 * ModeShape View
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeView extends WorkbenchView {

	public static final String TOOLBAR_ADD_SERVER = "Create a new server";
	public static final String TOOLBAR_EDIT_SERVER = "Edit server properties";
	public static final String TOOLBAR_DELETE_SERVER = "Delete server from the server registry";
	public static final String TOOLBAR_RECONNECT_SERVER = "Reconnect to the selected server";

	public ModeshapeView() {
		super("ModeShape", "ModeShape");
	}

	/**
	 * Click on context menu 'New Server'.
	 * 
	 * @return ModeshapeServerWizard
	 */
	public ModeshapeServerWizard newServer() {
		open();
		new ContextMenu("New Server").select();
		return new ModeshapeServerWizard();
	}

	/**
	 * Return all created ModeShape servers.
	 * 
	 * @return List of already created ModeShape servers
	 */
	public List<String> getServers() {
		open();
		List<String> servers = new ArrayList<String>();
		for (TreeItem server : new DefaultTree().getItems()) {
			servers.add(server.getText());
		}
		return servers;
	}

	/**
	 * Add new ModeShape server if it doesn't exist yet.
	 * 
	 * @param url
	 *            URL of ModeShape server
	 * @param user
	 *            User
	 * @param password
	 *            Password
	 */
	public void addServer(String url, String user, String password) {
		if (getServers().contains(url)) {
			return;
		}
		newServer().activate().setUrl(url).setUser(user).setPassword(password).testServerConnection().finish();
	}
	
	/**
	 * Create new publish area
	 * @param url
	 * @param repository
	 * @param workspace
	 * @param publishArea 
	 */
	public void addPublishArea(String url, String repository, String workspace, String publishArea){
		if (getPublishAreas(url, repository, workspace).contains(publishArea)){
			return;
		}
		new DefaultTreeItem(url, repository, workspace).select();
		new ContextMenu("New Publish Area").select();
		new DefaultText().setText(publishArea);
		new PushButton("OK").click();
	}
	
	public List<String> getPublishAreas(String url, String repository, String workspace){
		List<String> publishAreas = new ArrayList<String>();
		for (TreeItem pa : new DefaultTreeItem(url, repository, workspace).getItems()) {
			publishAreas.add(pa.getText());
		} 
		return publishAreas;
	}

}

package org.jboss.tools.modeshape.rest.ui.bot.ext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapePublishDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapeServerDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.util.ModeshapeWebdav;
import org.jboss.tools.modeshape.rest.ui.bot.ext.view.ModeshapeExplorer;
import org.jboss.tools.modeshape.rest.ui.bot.ext.view.ModeshapeView;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GeneralProjectExplorer;
import org.jboss.tools.ui.bot.ext.gen.IView;

/**
 * This class represents a bot for modeshape operations.
 * 
 * @author apodhrad
 *
 */
public class ModeshapeBot extends SWTBotExt {

	public static final String DEFAULT_SERVER_URL = "http://localhost:8080/modeshape-rest";
	public static final String DEFAULT_REPOSITORY = "repository";
	public static final String DEFAULT_WORKSPACE = "default";
	public static final String DEFAULT_PUBLISH_AREA = "/files";
	public static final String DEFAULT_USER = "admin";
	public static final String DEFAULT_PASSWORD = "admin";

	private String user;
	private String password;
	private String serverUrl;
	private String repository;
	private String workspace;
	private String publishArea;

	protected ModeshapeWebdav modeshapeWebdav;
	protected ModeshapeExplorer modeshapeExplorer;
	protected ModeshapeView modeshapeView;

	public ModeshapeBot() {
		super();

		setAuthorization(DEFAULT_USER, DEFAULT_PASSWORD);
		setServerUrl(DEFAULT_SERVER_URL);
		setRepository(DEFAULT_REPOSITORY);
		setWorkspace(DEFAULT_WORKSPACE);
		setPublishArea(DEFAULT_PUBLISH_AREA);

		modeshapeView = new ModeshapeView();
		modeshapeWebdav = new ModeshapeWebdav();
		
		setModeshapeExplorer(GeneralProjectExplorer.LABEL);
	}

	public void setModeshapeExplorer(IView view) {
		modeshapeExplorer = new ModeshapeExplorer(view);
	}

	public void setAuthorization(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getPublishArea() {
		return publishArea;
	}

	public void setPublishArea(String publishArea) {
		this.publishArea = publishArea;
	}

	public void createModeShapeServer() {
		modeshapeView.show();
		ModeshapeServerDialog dialog = modeshapeView.newServer();
		dialog.setUrl(serverUrl);
		dialog.setUser(user);
		dialog.setPassword(password);
		assertTrue("Couldn't connect to the server", dialog.testServerConnection());
		dialog.finishWithWait();
		assertTrue("Created server not visible in ModeShape view.",
				modeshapeView.containsServer(serverUrl));
		modeshapeView.reconnectServer(serverUrl);
	}

	public void deleteModeShapeServer() {
		modeshapeView.show();
		modeshapeView.deleteServer(serverUrl);
		shell("Confirm Delete Server").bot().button("OK").click();
		assertFalse("Created server is still visible in ModeShape view.",
				modeshapeView.containsServer(serverUrl));
	}

	public void publishFile(String... path) {
		modeshapeExplorer.publish(path);
		fillPublishDialog("Publish to ModeShape");
	}

	public void unpublishFile(String... path) {
		modeshapeExplorer.unpublish(path);
		fillPublishDialog("Unpublish from ModeShape");
	}

	public boolean isFilePublished(String path) throws IOException {
		URL url = modeshapeWebdav.computeUrl(repository, workspace, publishArea, path);
		return modeshapeWebdav.isFileAvailable(url, user, password);
	}

	private void fillPublishDialog(String title) {
		ModeshapePublishDialog publishDialog = new ModeshapePublishDialog(shell(title));
		publishDialog.setServer(serverUrl);
		publishDialog.setJcrRepository(repository);
		publishDialog.setJcrWorkspace(workspace);
		publishDialog.setPublishArea(publishArea);
		publishDialog.finishWithWait();
	}
}

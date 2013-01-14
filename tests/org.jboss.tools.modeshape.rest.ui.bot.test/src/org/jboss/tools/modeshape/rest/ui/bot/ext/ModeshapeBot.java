package org.jboss.tools.modeshape.rest.ui.bot.ext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapeLocationDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapePublishDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapeServerDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.util.ModeshapeWebdav;
import org.jboss.tools.modeshape.rest.ui.bot.ext.view.ModeshapeExplorer;
import org.jboss.tools.modeshape.rest.ui.bot.ext.view.ModeshapeView;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
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

	private static final Logger log = Logger.getLogger(ModeshapeBot.class);

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
		this(getDefaultRepository());
	}

	public ModeshapeBot(String repository) {
		super();

		this.user = DEFAULT_USER;
		this.password = DEFAULT_PASSWORD;
		this.serverUrl = DEFAULT_SERVER_URL;
		this.workspace = DEFAULT_WORKSPACE;
		this.publishArea = DEFAULT_PUBLISH_AREA;
		this.repository = repository;

		modeshapeView = new ModeshapeView();
		modeshapeWebdav = new ModeshapeWebdav();

		setModeshapeExplorer(GeneralProjectExplorer.LABEL);
	}

	private static String getDefaultRepository() {
		String repository = TestConfigurator.currentConfig.getProperty("MODESHAPE");
		if (repository != null) {
			return repository.split(",")[0];
		}
		return DEFAULT_REPOSITORY;
	}

	public void setModeshapeExplorer(IView view) {
		modeshapeExplorer = new ModeshapeExplorer(view);
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getRepository() {
		return repository;
	}

	public String getWorkspace() {
		return workspace;
	}

	public String getPublishArea() {
		return publishArea;
	}

	public void createModeShapeServer() {
		log.info("Create Modeshape server with url " + serverUrl);

		modeshapeView.show();
		// Don't create new server if it already exists
		if (!modeshapeView.containsServer(serverUrl)) {
			ModeshapeServerDialog dialog = modeshapeView.newServer();
			dialog.setUrl(serverUrl);
			dialog.setUser(user);
			dialog.setPassword(password);
			assertTrue("Couldn't connect to the server", dialog.testServerConnection());
			dialog.finishWithWait();
			assertTrue("Created server not visible in ModeShape view.",
					modeshapeView.containsServer(serverUrl));
		}
		modeshapeView.reconnectServer(serverUrl);
	}

	public void deleteModeShapeServer() {
		log.info("Delete Modeshape server with url " + serverUrl);
		modeshapeView.show();
		modeshapeView.deleteServer(serverUrl);
		shell("Confirm Delete Server").bot().button("OK").click();
		assertFalse("Created server is still visible in ModeShape view.",
				modeshapeView.containsServer(serverUrl));
	}

	public void publishFile(String... path) {
		log.info("Publish " + toString(path));
		modeshapeExplorer.publish(path);
		fillPublishDialog("Publish to ModeShape");
	}

	public void unpublishFile(String... path) {
		log.info("Unpublish " + toString(path));
		modeshapeExplorer.unpublish(path);
		fillPublishDialog("Unpublish from ModeShape");
	}

	public boolean isFilePublished(String path) throws IOException {
		URL url = modeshapeWebdav.computeUrl(repository, workspace, publishArea, path);
		return isFilePublished(url);
	}

	public boolean isFilePublished(URL url) throws IOException {
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

	public String getPublishLocation(String... path) {
		String publishLocation = null;

		log.info("Show publish location for " + toString(path));
		modeshapeExplorer.showPublishedLocations(path);

		ModeshapeLocationDialog locationDialog = new ModeshapeLocationDialog(
				shell("Published Locations"));
		assertEquals("Server URL doesn't match", serverUrl, locationDialog.getServerUrl());
		assertEquals("User doesn't match", user, locationDialog.getUser());
		assertEquals("Repository doesn't match", repository, locationDialog.getRepository());
		assertEquals("Workspace doesn't match", workspace, locationDialog.getWorkspace());
		publishLocation = locationDialog.getPublishedUrl();
		locationDialog.clickOK();

		return publishLocation;
	}

	private String toString(String... path) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < path.length; i++) {
			result.append(path[i]).append("/");
		}
		return result.toString();
	}
}

package org.jboss.tools.modeshape.rest.ui.bot.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapePublishDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapeServerDialog;
import org.jboss.tools.modeshape.rest.ui.bot.ext.view.ModeshapeProjectExplorer;
import org.jboss.tools.modeshape.rest.ui.bot.ext.view.ModeshapeView;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.GeneralFile;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.GeneralProject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author psrna
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "Java EE")
public class Publishing extends SWTTestExt {

	public static final String SERVER_REST_URL = "http://localhost:8080/modeshape-rest";
	public static final String SERVER_WEBDAV_URL = "http://localhost:8080/modeshape-webdav";
	public static final String PROJECT_NAME = "testproject";
	public static final String FILE_NAME = "testfile.txt";
	public static final String FILE_CONTENT = "testcontent";
	public static final String PUBLISH_URL = SERVER_REST_URL + "/repository/default/items/files/"
			+ PROJECT_NAME + "/" + FILE_NAME;
	public static final String PUBLISH_WEB_URL = SERVER_WEBDAV_URL
			+ "/repository/default/files/" + PROJECT_NAME + "/" + FILE_NAME;

	private static final ModeshapeProjectExplorer modeshapeExplorer = new ModeshapeProjectExplorer();

	@BeforeClass
	public static void createTestFile() {
		// Create a general project
		SWTBotNewObjectWizard projectWizard = new SWTBotNewObjectWizard();
		projectWizard.open(GeneralProject.LABEL);
		projectWizard.bot().textWithLabel(GeneralProject.TEXT_PROJECT_NAME).setText(PROJECT_NAME);
		projectWizard.finishWithWait();
		// Create a file
		SWTBotNewObjectWizard fileWizard = new SWTBotNewObjectWizard();
		fileWizard.open(GeneralFile.LABEL);
		fileWizard.bot().textWithLabel(GeneralFile.TEXT_FILE_NAME).setText(FILE_NAME);
		fileWizard.finishWithWait();
		// Edit the file
		SWTBotEclipseEditor fileEditor = projectExplorer.openFile(PROJECT_NAME, FILE_NAME)
				.toTextEditor();
		fileEditor.setText(FILE_CONTENT);
		fileEditor.saveAndClose();

		assertTrue("The test file wasn't created!",
				projectExplorer.isFilePresent(PROJECT_NAME, FILE_NAME));
	}

	@Test
	public void createModeShapeServer() {
		ModeshapeView modeshapeView = new ModeshapeView();
		modeshapeView.show();
		ModeshapeServerDialog dialog = modeshapeView.addServer();
		dialog.setUrl(SERVER_REST_URL);
		dialog.setUser("admin");
		dialog.setPassword("admin");
		assertTrue("Couldn't connect to the server", dialog.testServerConnection());
		dialog.finishWithWait();
		assertTrue("Created server not visible in ModeShape view.",
				modeshapeView.containsServer(SERVER_REST_URL));
	}

	@Test
	public void publishFile() {
		modeshapeExplorer.publish(FILE_NAME, PROJECT_NAME);

		ModeshapePublishDialog publishDialog = new ModeshapePublishDialog(
				bot.shell("Publish to ModeShape"));

		assertEquals(SERVER_REST_URL, publishDialog.getServer());
		assertTrue(publishDialog.canFinish());
		publishDialog.finishWithWait();

		assertTrue("HTTP Response code must be 200 after publishing.",
				HttpURLConnection.HTTP_OK == testPublishedFile());
	}

	@Test
	public void publishedLocations() {
		modeshapeExplorer.showPublishedLocations(FILE_NAME, PROJECT_NAME);

		SWTBotShell shell = bot.shell("Published Locations");
		shell.activate();

		SWTBotTable table = shell.bot().table();
		assertEquals(SERVER_REST_URL, table.cell(0, 0));
		assertEquals("admin", table.cell(0, 1));
		assertEquals("repository", table.cell(0, 2));
		assertEquals("default", table.cell(0, 3));
		assertEquals(PUBLISH_URL, table.cell(0, 4));

		open.finish(shell.bot(), IDELabel.Button.OK);

	}

	@Test
	public void unpublishFile() {
		modeshapeExplorer.unpublish(FILE_NAME, PROJECT_NAME);

		ModeshapePublishDialog dialog = new ModeshapePublishDialog(
				bot.shell("Unpublish from ModeShape"));

		assertEquals(SERVER_REST_URL, dialog.getServer());
		assertEquals("repository", dialog.getJcrRepository());
		assertEquals("default", dialog.getJcrWorkspace());

		dialog.finishWithWait();

		assertTrue("HTTP Response code must be 404 after unpublishing.",
				HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile());

	}

	@AfterClass
	public static void afterClass() {
		ModeshapeView view = new ModeshapeView();
		view.deleteServer(SERVER_REST_URL);
	}

	/**
	 * 
	 * @return response code
	 */
	private int testPublishedFile() {

		try {

			URL url = new URL(PUBLISH_WEB_URL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setAllowUserInteraction(false);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String authorization = Properties.USER + ":" + Properties.PASSWORD;
			String encodedAuthorization = new String(Base64.encode(authorization.getBytes()));
			conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

			conn.connect();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// if HTTP_OK, test content
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				assertEquals(FILE_CONTENT, sb.toString());
			}

			// return response code
			return conn.getResponseCode();

		} catch (IOException e) {
			fail("Unable to test the published file.");
			log.error(e.getMessage());
		}
		return -1;
	}

}

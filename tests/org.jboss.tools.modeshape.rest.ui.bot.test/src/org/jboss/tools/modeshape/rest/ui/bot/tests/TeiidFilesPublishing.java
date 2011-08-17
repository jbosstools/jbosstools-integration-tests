package org.jboss.tools.modeshape.rest.ui.bot.tests;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.waitForShell;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * 
 * @author psrna
 *
 */
@Require(server=@Server(type=ServerType.SOA,version="5.1"), perspective="Teiid Designer")
public class TeiidFilesPublishing extends SWTTestExt{
	
	private static final String CONNERR_MSG = "Unable to connect using the specified server properties." +
	  										  "The server properties could be invalid or the server may be offline.";
	
	public static void importTeiidPartsModelExample(){
		
		bot.menu(IDELabel.Menu.HELP)
		   .menu("Project Examples...").click();
		
		bot.waitUntil(Conditions.shellIsActive("Progress Information"));
		bot.waitUntil(Conditions.shellCloses(bot.shell("Progress Information")));
		
		bot.shell("Invalid Sites").activate();
		bot.button("OK").click();
		
		
		SWTBotShell shell = bot.shell("New Project Example");
		shell.activate();
		shell.bot().tree(0).expandNode("Teiid Designer")
		                   .select("Parts Model Project Set Example");
		
		open.finish(shell.bot(), IDELabel.Button.FINISH);
		
		bot.waitUntil(Conditions.shellIsActive("Downloading..."));
		bot.waitUntil(Conditions.shellCloses(bot.shell("Downloading...")));
	}
	
	@BeforeClass
	public static void beforeClass(){
		importTeiidPartsModelExample();
		
	}
	
	@Test
	public void createModeShapeServer(){

		openModeshapeView();

		SWTBotView view = bot.viewByTitle("ModeShape");
		assertTrue("ModeShape View must be active", view.isActive());

		view.toolbarButton("Create a new server").click();
		SWTBotShell shell = bot.shell("New Server");
		shell.activate();

		shell.bot().textWithLabel("URL:").typeText(Properties.URL);
		shell.bot().textWithLabel("User:").typeText(Properties.USER);
		shell.bot().textWithLabel("Password:").typeText(Properties.PASSWORD);

		shell.bot().button("Test").click();
		shell = bot.shell("Test Server Connection");
		shell.activate();

		String msg = shell.bot().label(1).getText();
		assertTrue(CONNERR_MSG, msg.equals("Successfully connected using the specified server properties."));

		open.finish(bot.activeShell().bot(), IDELabel.Button.OK);
		shell = bot.shell("New Server");
		shell.activate();

		open.finish(shell.bot()); 

		assertTrue("Created server not visible in ModeShape view.", view.bot().tree().getTreeItem(Properties.URL).isVisible());	
			
	}
		
	private void openModeshapeView(){
		
		bot.menu(IDELabel.Menu.WINDOW)
		   .menu(IDELabel.Menu.SHOW_VIEW)
		   .menu(IDELabel.Menu.OTHER).click();		

		SWTBotShell shell = bot.shell("Show View");
		shell.activate();
		shell.bot().tree().expandNode("ModeShape", false).select("ModeShape");

		open.finish(bot.activeShell().bot(), IDELabel.Button.OK);
	}
	
	
	@Test
	public void publishFiles(){
		
		bot.viewByTitle("Model Explorer").show();
		SWTBotView view = bot.viewByTitle("Model Explorer");
		SWTBot viewBot = view.bot();
		SWTBotTreeItem node = eclipse.selectTreeLocation(viewBot, Properties.TEIID_PROJECT_NAME);

		ContextMenuHelper.prepareTreeItemForContextMenu(projectExplorer.bot().tree(),node);
		ContextMenuHelper.clickContextMenu(projectExplorer.bot().tree(), "ModeShape", "Publish");
		
		SWTBotShell shell = bot.shell("Publish to ModeShape");
		shell.activate();
		
		SWTBotCombo serverCombo = shell.bot().comboBoxWithLabel("Server:");
		SWTBotCombo repoCombo = shell.bot().comboBoxWithLabel("JCR Repository:");
		SWTBotCombo workspaceCombo = shell.bot().comboBoxWithLabel("JCR Workspace:");
		workspaceCombo.setSelection(Properties.WORKSPACE);
		
		assertTrue("URL mismatch.", serverCombo.getText().equals(Properties.URL));
		assertTrue("Repository mismatch.", repoCombo.getText().equals(Properties.REPOSITORY));
		assertTrue("Workspace mismatch.", workspaceCombo.getText().equals(Properties.WORKSPACE));
		
		open.finish(shell.bot());
		bot.waitUntil(Conditions.shellCloses(shell));
		
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.TEIID_PROJECT_DESC));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.PARTS_SOURCE_A));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.PARTS_SOURCE_B));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.PARTS_VIRTUAL));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.DATA_1));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.DATA_2));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.DATA_3));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.DATA_4));
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile(Properties.DATA_5));
		
	}
	
	@Test
	public void unpublishFiles(){
		
		SWTBotView view = bot.viewByTitle("Model Explorer");
		SWTBot viewBot = view.bot();
		SWTBotTreeItem node = eclipse.selectTreeLocation(viewBot, Properties.TEIID_PROJECT_NAME);
		
		ContextMenuHelper.prepareTreeItemForContextMenu(projectExplorer.bot().tree(),node);
		ContextMenuHelper.clickContextMenu(projectExplorer.bot().tree(), "ModeShape", "Unpublish");
		
		SWTBotShell shell = bot.shell("Unpublish from ModeShape");
		shell.activate();
		
		SWTBotCombo serverCombo = shell.bot().comboBoxWithLabel("Server:");
		SWTBotCombo repoCombo = shell.bot().comboBoxWithLabel("JCR Repository:");
		SWTBotCombo workspaceCombo = shell.bot().comboBoxWithLabel("JCR Workspace:");
		workspaceCombo.setSelection(Properties.WORKSPACE);
		
		assertTrue("URL mismatch.", serverCombo.getText().equals(Properties.URL));
		assertTrue("Repository mismatch.", repoCombo.getText().equals(Properties.REPOSITORY));
		assertTrue("Workspace mismatch.", workspaceCombo.getText().equals(Properties.WORKSPACE));
		
		open.finish(shell.bot());
		bot.waitUntil(Conditions.shellCloses(shell));
			
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.TEIID_PROJECT_DESC));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.PARTS_SOURCE_A));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.PARTS_SOURCE_B));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.PARTS_VIRTUAL));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.DATA_1));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.DATA_2));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.DATA_3));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.DATA_4));
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile(Properties.DATA_5));

	}
	
	/**
	 * 
	 * @return response code
	 */
	private int testPublishedFile(String filename){
		
		try {
			
			URL url = new URL(Properties.WEBDAV_URL 		+ "/"       + 
							  Properties.REPOSITORY 		+ "/"       + 
							  Properties.WORKSPACE  		+ "/files/" + 
							  Properties.TEIID_PROJECT_NAME + "/"       + filename);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setAllowUserInteraction(false);
		    conn.setUseCaches(false);
		    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						
			String authorization = Properties.USER + ":" + Properties.PASSWORD;
			String encodedAuthorization= new String(Base64.encode(authorization.getBytes()));
			conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
			
			conn.connect();
			
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				//if HTTP_OK, test content
				
				File file = new File(Platform.getLocation() + "/" + Properties.TEIID_PROJECT_NAME + "/" + filename);
				byte[] file_input;
				byte[] server_input;
				InputStream is = conn.getInputStream();
				
				file_input = Utils.read(file);
				server_input = Utils.read(is,(int) file.length());
				
				assertTrue("File content mismatch.", Arrays.equals(file_input, server_input));
			}

			//return response code
			return conn.getResponseCode();
			
		} catch (IOException e) {
			fail("Unable to test the published file.");
			log.error(e.getMessage());
		} 
		return -1;
	}
	

	@AfterClass
	public static void afterClass(){
		
		bot.viewByTitle("ModeShape").show();
		SWTBot mbot = bot.viewByTitle("ModeShape").bot();
		mbot.tree().select(Properties.URL);
		bot.toolbarButtonWithTooltip("Delete server from the server registry").click();
		SWTBotShell shell = bot.shell("Confirm Delete Server");
		
		open.finish(shell.bot(),IDELabel.Button.OK);
	}
	
}

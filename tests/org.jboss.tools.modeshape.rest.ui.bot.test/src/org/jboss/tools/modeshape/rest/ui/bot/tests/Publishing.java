package org.jboss.tools.modeshape.rest.ui.bot.tests;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.GeneralProject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * 
 * @author psrna
 *
 */
@Require(server=@Server(type=ServerType.ALL, state=ServerState.Running),perspective="Java EE")
public class Publishing extends SWTTestExt{
	
	private static final String CONNERR_MSG = "Unable to connect using the specified server properties." +
    										  "The server properties could be invalid or the server may be offline.";
	
	
	public static void createResource(){
		
		SWTBot wiz = open.newObject(ActionItem.NewObject.GeneralProject.LABEL);
		wiz.textWithLabel(GeneralProject.TEXT_PROJECT_NAME).setText(Properties.PROJECT_NAME);
		open.finish(wiz);
		assertTrue(projectExplorer.existsResource(Properties.PROJECT_NAME));
		
		wiz = open.newObject(ActionItem.NewObject.GeneralFile.LABEL);
		wiz.tree().select(Properties.PROJECT_NAME);
		wiz.textWithLabel(ActionItem.NewObject.GeneralFile.TEXT_FILE_NAME).setText(Properties.FILE_NAME);
		open.finish(wiz);
		assertTrue(projectExplorer.isFilePresent(Properties.PROJECT_NAME, Properties.FILE_NAME));	
		
		bot.editorByTitle(Properties.FILE_NAME).close();
		
		String projectLocation = SWTUtilExt.getPathToProject(Properties.PROJECT_NAME);
		try {
			FileWriter fstream = new FileWriter(projectLocation + "/" + Properties.FILE_NAME);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(Properties.FILE_CONTENT);
			out.close();
		} catch (IOException e) {
			fail("Unable to write to file.");
			log.error(e.getMessage());
		}
		
		bot.viewByTitle("Project Explorer").show();
		bot.viewByTitle("Project Explorer").setFocus();
		SWTBot viewBot = bot.viewByTitle("Project Explorer").bot();
		
		SWTBotTreeItem node = SWTEclipseExt.selectTreeLocation(viewBot, Properties.PROJECT_NAME);

		ContextMenuHelper.prepareTreeItemForContextMenu(viewBot.tree(), node);
		ContextMenuHelper.clickContextMenu(viewBot.tree(), "Refresh");

	}
	
	@BeforeClass
	public static void beforeClass(){
		createResource();
	}
	
	
	@Test
	public void createModeShapeServer(){

		openModeshapeView();

		SWTBotView view = bot.viewByTitle("ModeShape");
		assertTrue("ModeShape View must be active", view.isActive());

		view.toolbarButton("Create a new server").click();
		SWTBotShell shell = bot.shell("New Server");
		shell.activate();

		shell.bot().textWithLabel("URL:").setText(Properties.URL);
		shell.bot().textWithLabel("User:").setText(Properties.USER);
		shell.bot().textWithLabel("Password:").setText(Properties.PASSWORD);

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
	public void publishFile(){

		SWTBotView view = bot.viewByTitle("Project Explorer");
		SWTBot viewBot = view.bot();
		SWTBotTreeItem node = eclipse.selectTreeLocation(viewBot, Properties.PROJECT_NAME, Properties.FILE_NAME);

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
		
		assertTrue("HTTP Response code must be 200 after publishing.", HttpURLConnection.HTTP_OK == testPublishedFile());
	}
	
	
	@Test
	public void publishedLocations(){
		
		SWTBotView view = bot.viewByTitle("Project Explorer");
		SWTBot viewBot = view.bot();
		SWTBotTreeItem node = eclipse.selectTreeLocation(viewBot, Properties.PROJECT_NAME, Properties.FILE_NAME);
		
		ContextMenuHelper.prepareTreeItemForContextMenu(projectExplorer.bot().tree(),node);
		ContextMenuHelper.clickContextMenu(projectExplorer.bot().tree(), "ModeShape", "Show Published Locations");
		
		SWTBotShell shell = bot.shell("Published Locations");
		shell.activate();
		
		SWTBotTable table = shell.bot().table();
		assertTrue("Url mismatch.", table.cell(0, 0).equals(Properties.URL));
		assertTrue("User mismatch.", table.cell(0, 1).equals(Properties.USER));
		assertTrue("Repository mismatch.", table.cell(0, 2).equals(Properties.REPOSITORY));
		assertTrue("Workspace mismatch.", table.cell(0, 3).equals(Properties.WORKSPACE));
		
		String expectedPublishedUrl = Properties.URL          + "/"             + 
		                              Properties.REPOSITORY   + "/"             + 
		                              Properties.WORKSPACE    + "/items/files/" + 
		                              Properties.PROJECT_NAME + "/"             + 
		                              Properties.FILE_NAME;
		
		assertTrue("Published Url mismatch.", table.cell(0, 4).equals(expectedPublishedUrl));
		
		open.finish(shell.bot(), IDELabel.Button.OK);
		
	}

	
	
	@Test
	public void unpublishFile(){
		
		SWTBotView view = bot.viewByTitle("Project Explorer");
		SWTBot viewBot = view.bot();
		SWTBotTreeItem node = eclipse.selectTreeLocation(viewBot, Properties.PROJECT_NAME, Properties.FILE_NAME);
		
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
		
		assertTrue("HTTP Response code must be 404 after unpublishing.", HttpURLConnection.HTTP_NOT_FOUND == testPublishedFile());
		
	}
		
	@AfterClass
	public static void afterClass(){
		
		bot.viewByTitle("ModeShape").show();
		SWTBotView botView = bot.viewByTitle("ModeShape");
		botView.bot().tree().select(Properties.URL);
		botView.toolbarButton("Delete server from the server registry").click();
		SWTBotShell shell = bot.shell("Confirm Delete Server");
		
		open.finish(shell.bot(),IDELabel.Button.OK);
	}
	
	
		
	/**
	 * 
	 * @return response code
	 */
	private int testPublishedFile(){
		
		try {
			
			URL url = new URL(Properties.WEBDAV_URL   + "/"       + 
							  Properties.REPOSITORY   + "/"       + 
							  Properties.WORKSPACE    + "/files/" + 
							  Properties.PROJECT_NAME + "/"       + 
							  Properties.FILE_NAME);
			
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
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
			
				while ((line = rd.readLine()) != null){
					sb.append(line);
				}
				assertTrue("File content mismatch.", sb.toString().equals(Properties.FILE_CONTENT));
			}

			//return response code
			return conn.getResponseCode();
			
		} catch (IOException e) {
			fail("Unable to test the published file.");
			log.error(e.getMessage());
		} 
		return -1;
	}
		
}

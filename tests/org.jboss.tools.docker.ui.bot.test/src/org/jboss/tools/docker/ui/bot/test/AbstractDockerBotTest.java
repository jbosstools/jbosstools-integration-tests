/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.docker.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.docker.reddeer.core.ui.wizards.DockerConnectionWizard;
import org.jboss.tools.docker.reddeer.perspective.DockerPerspective;
import org.jboss.tools.docker.reddeer.ui.ConnectionItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.junit.After;
import org.junit.BeforeClass;

/**
 * 
 * @author jkopriva
 *
 */

public abstract class AbstractDockerBotTest {
	
	@BeforeClass 
	public static void beforeClass(){
		new WorkbenchShell().maximize();
	}
	
	@After
	public void cleanUp(){
		cleanupShells();
	}
	
	private static void cleanupShells() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}
	
	
	protected String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                           new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	
	protected ArrayList<String> getIds(String stringWithIds) {

		ArrayList<String> idList = new ArrayList<String>();

		if (stringWithIds == null || stringWithIds.equals(""))
			return idList;
		
		idList = new ArrayList<String>(Arrays.asList(stringWithIds.split("\\r?\\n")));
		
		return idList;
	}
	
	protected static void openDockerPerspective(){
		new DockerPerspective().open();
		try{
			new ShellWithTextIsActive("Docker Explorer");
		} catch (SWTLayerException ex){
			fail("Docker Explorer not found in Docker tooling perspective");
		}
	}
	
	
	protected static void createConnection(){
		String dockerServerURI = System.getProperty("dockerServerURI");
		createConnection(dockerServerURI);
	}
	
	protected static void createConnection(String dockerServer){
		DockerConnectionWizard connectionWizard = new DockerConnectionWizard();
		connectionWizard.open();
		connectionWizard.setTcpConnection(dockerServer);
		connectionWizard.finish();
	}
	
	protected static void deleteConnection() {
		deleteConnection(System.getProperty("dockerServerURI"));
	}
	
	protected static void deleteConnection(String dockerServer) {
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		new DefaultToolItem("Remove Connection").click();
	}
	
	protected static void deleteImage(String imageName) {
		deleteImage(System.getProperty("dockerServerURI"),imageName);
	}
	
	protected static void deleteImage(String dockerServer, String imageName) {
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem imagesItem = dc.getItem("Images");
		imagesItem.select();
		imagesItem.expand();
		for (TreeItem item : imagesItem.getItems()) {
			if (item.getText().contains(imageName)) {
				item.select();
				new ContextMenu("Remove").select();
				new WaitUntil(new ShellWithTextIsActive("Confirm Remove Image"), TimePeriod.NORMAL);
				new PushButton("OK").click();
				new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
			}
		}
	}
	
	protected static boolean imageIsDeployed(String imageName){
		return imageIsDeployed(System.getProperty("dockerServerURI"),imageName);
	}
	
	protected static boolean imageIsDeployed(String dockerServer, String imageName){
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem imagesItem = dc.getItem("Images");
		imagesItem.select();
		imagesItem.expand();
		for (TreeItem item : imagesItem.getItems()) {
			if (item.getText().contains(imageName)) {
				return true;
			}
		}
		return false;
		
	}
	
	protected static void deleteContainer(String containerName) {
		deleteContainer(System.getProperty("dockerServerURI"),containerName);
	}
	
	
	protected static void deleteContainer(String dockerServer, String containerName) {
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem containersItem = dc.getItem("Containers");
		containersItem.select();
		containersItem.expand();
		for (TreeItem item : containersItem.getItems()) {
			if (item.getText().contains(containerName)) {
				item.select();
				Menu contextMenu = new ContextMenu("Remove");
				if (!contextMenu.isEnabled()) {
					new ContextMenu("Stop").select();
					new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
					item.select();
					contextMenu = new ContextMenu("Remove");
				}
				contextMenu.select();
				new WaitUntil(new ShellWithTextIsActive("Confirm Remove Container"), TimePeriod.NORMAL);
				new PushButton("OK").click();
				new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
			}
		}
	}
	
	
	protected void pullImage(String imageName){
		pullImage(System.getProperty("dockerServerURI"),imageName);
	}
	
	
	protected void pullImage(String dockerServerURI, String imageName){
		DockerExplorer de = new DockerExplorer();
		de.open();
		de.getConnection(dockerServerURI);
		new ContextMenu("Pull...").select();
		new WaitUntil(new ShellWithTextIsActive("Pull Image"), TimePeriod.NORMAL);
		new LabeledText("Name:").setText(imageName);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	
	
	protected void checkBrowserForErrorPage(BrowserView browser) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		if(browser.getText().contains("Unable")){
			browser.refreshPage();
		}
		assertFalse("Browser contains text 'Status 404'\n Console output:\n" + consoleView.getConsoleText(),
				browser.getText().contains("Status 404") || browser.getText().contains("404 - Not Found")|| browser.getText().contains("Forbidden")|| browser.getText().contains("404"));
		assertFalse(
				"Browser contains text 'Error processing request'\n Console output:\n" + consoleView.getConsoleText(),
				browser.getText().contains("Error processing request"));
	}
	
	protected String createURL(String tail){
		return System.getProperty("dockerServerURI").replaceAll("tcp://","").replaceAll("\\:(.*)", tail);
	}
	
	

}

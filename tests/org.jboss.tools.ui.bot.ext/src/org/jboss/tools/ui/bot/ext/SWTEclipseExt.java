 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.ui.bot.ext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;
import org.jboss.tools.ui.bot.ext.entity.JavaClassEntity;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.jboss.tools.ui.bot.ext.types.EntityType;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.PerspectiveType;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ui.bot.ext.types.IDELabel.PreferencesDialog;

/**
 * Provides Eclipse common operation based on SWTBot element operations
 * @author jpeterka
 *
 */
public class SWTEclipseExt {

  private SWTWorkbenchBot bot;
  private SWTUtilExt swtUtilExt;
  private Logger log = Logger.getLogger(SWTEclipseExt.class);
	
	public SWTWorkbenchBot getBot() {
    return bot;
  }

  public SWTUtilExt getSwtUtilExt() {
    return swtUtilExt;
  }

  public Logger getLog() {
    return log;
  }

  public SWTEclipseExt(SWTWorkbenchBot bot) {
		this.bot = bot;
		swtUtilExt = new SWTUtilExt(bot);
	}
	
	// ------------------------------------------------------------
	// View related methods
	// ------------------------------------------------------------
	/**
	 * Close view by text
	 */
	public void closeView(String view) {
		bot.viewByTitle(view).close();
	}

	/**
	 * Show view
	 * 
	 * @param type
	 */
	public SWTBot showView(ViewType type) {
		bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.SHOW_VIEW).menu(
				IDELabel.Menu.OTHER).click();
		bot.tree().expandNode(type.getGroupLabel()).expandNode(type.getViewLabel()).select();
		bot.button(IDELabel.Button.OK).click();
		
		SWTBot viewBot = bot.viewByTitle(type.getViewLabel()).bot();
		return viewBot;		
	}
	// ------------------------------------------------------------
	// Perspective related methods
	// ------------------------------------------------------------
	/**
	 * Open Perspective
	 * 
	 * @param type
	 */
	public void openPerspective(PerspectiveType type) {
		String perspectiveLabel = "";

		switch (type) {
			case JAVA: perspectiveLabel = IDELabel.SelectPerspectiveDialog.JAVA; break;
			case HIBERNATE: perspectiveLabel = IDELabel.SelectPerspectiveDialog.HIBERNATE;break;
			case SEAM: perspectiveLabel = IDELabel.SelectPerspectiveDialog.SEAM;break;
			case WEB_DEVELOPMENT:perspectiveLabel = IDELabel.SelectPerspectiveDialog.WEB_DEVELOPMENT; break;
			case DB_DEVELOPMENT: perspectiveLabel = IDELabel.SelectPerspectiveDialog.DB_DEVELOPMENT; break;
			default: fail("Unknown perspective to open");
		}
		
		bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.OPEN_PERSPECTIVE).menu(
				IDELabel.Menu.OTHER).click();
		bot.table().select(perspectiveLabel);

		// Another approach
		 SWTBotShell openPerpectiveShell = bot.shell("Open Perspective");
		 openPerpectiveShell.activate();

		bot.button(IDELabel.Button.OK).click();
	}

	// ------------------------------------------------------------
	// Create related methods
	// ------------------------------------------------------------	
	/**
	 * Create Java Project desribed with propriate entity
	 * @param entity
	 */
	public void createJavaProject(JavaProjectEntity entity) {
		// NewWizard
		createNew(EntityType.JAVA_PROJECT);
		waitForShell(IDELabel.Shell.NEW_JAVA_PROJECT);

		// JavaProjectWizard
		bot.textWithLabel(IDELabel.JavaProjectWizard.PROJECT_NAME).setText(
				entity.getProjectName());
		bot.button(IDELabel.Button.FINISH).click();

		// Wait for shell closing JavaProjectWizard
		waitForClosedShell(IDELabel.Shell.NEW_JAVA_PROJECT);
		swtUtilExt.waitForNonIgnoredJobs();
	}
	
	// ------------------------------------------------------------
	// Create related methods
	// ------------------------------------------------------------		
	/**
	 * Create new Java Class described with JavaClassEntity
	 * @param entity
	 */
	public void createJavaClass(JavaClassEntity entity) {
		createNew(EntityType.JAVA_CLASS);
		waitForShell(IDELabel.Shell.NEW_JAVA_CLASS);
		
		bot.textWithLabel(IDELabel.NewClassCreationWizard.PACKAGE_NAME).setText(entity.getPackageName());	
		bot.textWithLabel(IDELabel.NewClassCreationWizard.CLASS_NAME).setText(entity.getClassName());
		bot.button(IDELabel.Button.FINISH).click();
		
		waitForClosedShell(IDELabel.Shell.NEW_JAVA_CLASS);
		swtUtilExt.waitForNonIgnoredJobs();
	}
	
	/**
	 * Remove entity from package explorer
	 * @param projectName
	 * @param path
	 */
	public void removeFile(String projectName, String... path) {
		// Open Package Explorer and aim the Project
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);

		// Go through path 
		for ( String nodeName: path ) {
			item = item.expandNode(nodeName);
		}

		// Delete File
		item.select().contextMenu("Delete").click();		
		bot.button(IDELabel.Button.OK).click();		
	}
	

	/**
	 * Open File in Package Explorer
	 * @param projectName
	 * @param path
	 */
	public void openFile(String projectName, String... path) {
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);
		StringBuilder builder = new StringBuilder(projectName);
		
		// Go through path 
		for ( String nodeName: path ) {
			item = item.expandNode(nodeName);
			builder.append("/" + nodeName);
		}
		
		item.select().doubleClick();
		log.info("File Opened:" + builder.toString());
	}
	
	// ------------------------------------------------------------
	// Navigation related
	// ------------------------------------------------------------		
	/**
	 * Select element in tree
	 * @return 
	 */
	public SWTBotTreeItem selectTreeLocation(String... path) {		
		return selectTreeLocation(bot, path);
	}	
	/**
	 * Select element in tree with given bot
	 * @return 
	 */	
	public SWTBotTreeItem selectTreeLocation(SWTBot bot, String... path) {

		SWTBot viewBot = bot;
		
		SWTBotTreeItem item = null; 
		// Go through path 
		for ( String nodeName: path ) {
			if ( item == null ) {
				item = viewBot.tree().expandNode(nodeName);
			} else {
				item = item.expandNode(nodeName);	
			}
			log.info(nodeName);			
		}
		return item.select();
	}
		
	// ------------------------------------------------------------
	// Subroutines
	// ------------------------------------------------------------

	/**
	 * Invoke Create new entity dialog (it means File -> New -> EntityType -> Next )
	 */
	public void createNew(EntityType entityType) {
		
		bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.NEW).menu(IDELabel.Menu.OTHER).click();
		waitForShell(IDELabel.Shell.NEW);

		String entityLabel = entityType.getEntityLabel();
		SWTBotTree tree = bot.tree();
		Iterator<String> itGroupsNodes = entityType.getGroupsLabels().iterator();
		// if there are group labels defined expand groups nodes
		if (itGroupsNodes.hasNext()){
		  SWTBotTreeItem groupTreeItem = tree.expandNode(itGroupsNodes.next());
		  while (itGroupsNodes.hasNext()){
		    groupTreeItem = groupTreeItem.expandNode(itGroupsNodes.next());
		  }
		  groupTreeItem.select(entityLabel);
		}
		else{
		  tree.select(entityLabel); 
		}
		bot.button(IDELabel.Button.NEXT).click();
	}
	
	/**
	 * Wait for appearance shell of given name
	 * @param shellName
	 */
	public void waitForShell(String shellName) {		
		Matcher<Shell> matcher = WidgetMatcherFactory
		.withText(shellName);
		bot.waitUntil(Conditions.waitForShell(matcher));
		
	}
	
	/**
	 * Waits for closed shell with shell name
	 * @param shellName
	 */
	public void waitForClosedShell(String shellName) {
		bot.waitWhile(Conditions.shellIsActive(shellName));
	}

	public void waitForClosedShell(SWTBotShell shell) {
		while (shell.isActive()) {
			bot.sleep(200);
			log.info("Waiting for closing shell...");
		}
	}
	
	/**
	 * Assert same content of file in project explorer with content of file in resources
	 * @param pluginId
	 * @param projectName
	 * @param path
	 */
	public void assertSameContent(String pluginId, String projectName, String... path) {
		File file = swtUtilExt.getResourceFile(pluginId, path);
		String resourceContent = swtUtilExt.readTextFile(file);
		
		openAsText(projectName, path);
		SWTBotEclipseEditor editor = bot.editorByTitle(path[path.length - 1]).toTextEditor();
		String fileContent = editor.getText();
		
		assertEquals(resourceContent, fileContent );
	}
	
	/**
	 * Open file from Package Explorer as text
	 * @param projectName
	 * @param path
	 */
	public void openAsText(String projectName, String... path) {
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);
		
		// Go through path 
		for ( String nodeName: path ) {
			log.info(nodeName);
			item = item.expandNode(nodeName);
		}		
	
		item.select();		
	}

	/**
	 * Replace class content from content of class from resource
	 * @param pluginId
	 * @param path
	 */
	public void setClassContentFromResource(boolean save, String pluginId, String... path) {		 
		SWTBotEclipseEditor editor;
		editor = bot.editorByTitle(path[path.length - 1]).toTextEditor();
		editor.selectRange(0, 0, editor.getLineCount());
		File file = swtUtilExt.getResourceFile(pluginId, path);
		String content = swtUtilExt.readTextFile(file);
		editor.setText(content);
		if (save)
			editor.save();	
	}
  /**
   * Define new Server Runtime only if it's not specified yet
   * @param runtimeName
   * @param runtimeGroup
   * @param runtimeType
   * @param runtimeHomeDir
   */
	public void addServerRuntime (String runtimeName, String runtimeGroup, 
	  String runtimeType, String runtimeHomeDir){
	  
	  bot.menu(IDELabel.Menu.WINDOW).menu(IDELabel.Menu.PREFERENCES).click();
	  bot.shell(IDELabel.Shell.PREFERENCES).activate();
	  bot.tree().expandNode(IDELabel.PreferencesDialog.SERVER_GROUP)
	    .select(PreferencesDialog.RUNTIME_ENVIRONMENTS);
	  SWTBotTable tbRuntimeEnvironments = bot.table();
	  boolean createRuntime = true;
	  // first check if Environment doesn't exist
	  int numRows = tbRuntimeEnvironments.rowCount();
	  if (numRows > 0){
	    int currentRow = 0;
	    while (createRuntime && currentRow < numRows){
	      if (tbRuntimeEnvironments.cell(currentRow, 0).equalsIgnoreCase(runtimeName)){
	        createRuntime = false;
	      }
	      else {
	        currentRow++;
	      }  
	    }
	  }
	  // create Server Runtime
	  if (createRuntime){
	    bot.button(IDELabel.Button.ADD).click();
	    bot.shell(IDELabel.Shell.NEW_SERVER_RUNTIME_ENVIRONMENT).activate();
      bot.tree().expandNode(runtimeGroup).select(runtimeType);
      bot.button("Next >").click();
	    bot.textWithLabel(IDELabel.JBossServerRuntimeDialog.NAME).setText(runtimeName);
	    bot.textWithLabel(IDELabel.JBossServerRuntimeDialog.HOME_DIRECTORY).setText(runtimeHomeDir);
	    bot.button(IDELabel.Button.FINISH).click();
	  }
	  bot.button(IDELabel.Button.OK).click();
	}
	/**
	 * Test if tree contains item with itemLabel
	 * @param tree
	 * @param itemLabel
	 * @return
	 */
	public static boolean treeContainsItemWithLabel(SWTBotTree tree, String itemLabel){
	  boolean containsItem = false;
    try {
      tree.getTreeItem(itemLabel);
      containsItem = true;
    } catch (WidgetNotFoundException e) {
    }
    return containsItem;
	}
	/**
	 * if Open Associated Perspective Shell is opened close it 
	 * and depend on switchPerspective parameter change current perspective
	 * @param switchPerspective
	 */
	public void closeOpenAssociatedPerspectiveShellIfOpened(boolean switchPerspective){
	  try{
	    bot.shell(IDELabel.Shell.OPEN_ASSOCIATED_PERSPECTIVE).activate();
	    bot.button(switchPerspective ? IDELabel.Button.YES : IDELabel.Button.NO).click();
	  } catch (WidgetNotFoundException wnfe){
	    // do nothing
	  }
	}
	/**
	 * Close Warning Window if is opened.
	 * Press Continue or Cancel button dependent on pressContinueButton
	 * @param pressContinueButton
	 */
  public void closeWarningWindowIfOpened(boolean pressContinueButton){
    try{
      bot.shell(IDELabel.Shell.WARNING).activate();
      bot.button(pressContinueButton ? IDELabel.Button.CONTINUE : IDELabel.Button.CANCEL).click();
    } catch (WidgetNotFoundException wnfe){
      // do nothing
    }
  }
	public static void closeWarningWindowIfOpened(SWTWorkbenchBot bot, boolean pressContinueButton){
	  closeWarningWindowIfOpened(bot, pressContinueButton);
	}
	
}

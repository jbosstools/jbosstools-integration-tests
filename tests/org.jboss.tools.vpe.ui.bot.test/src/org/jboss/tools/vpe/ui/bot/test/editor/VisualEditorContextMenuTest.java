/*******************************************************************************

 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;
/**
 * Tests Visual Editor Context Menu  
 * @author vlado pakan
 *
 */
public class VisualEditorContextMenuTest extends VPEEditorTestCase {
  
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
    "<%@ taglib uri=\"http://richfaces.org/rich\" prefix=\"rich\"%>\n" +
    "<html>\n" +
    "  <body>\n" +
    "  plain text  \n" +
    "    <h:outputText value=\"Studio\" />\n" +
    "    <rich:calendar></rich:calendar>\n" +
    "    <h:inputText/>\n" + 
    "    <h:inputText/>\n" +
    "  </body>\n" +
    "</html>";
  
  private static final String TEST_PAGE_NAME = "VisualEditorContextMenuTest.jsp";
  
  private SWTBotExt botExt = null;
  
	public VisualEditorContextMenuTest() {
		super();
		botExt = new SWTBotExt();
	}

	public void testVisualEditorContextMenu(){
	  eclipse.maximizeActiveShell();
    createJspPage(VisualEditorContextMenuTest.TEST_PAGE_NAME);
    final SWTBotEclipseEditor jspTextEditor = botExt.editorByTitle(VisualEditorContextMenuTest.TEST_PAGE_NAME)
      .toTextEditor();
    final SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(VisualEditorContextMenuTest.TEST_PAGE_NAME,botExt);
    checkEditMenuFunctionality(webBrowser, jspTextEditor);
    checkInsertMenuFunctionality(webBrowser, jspTextEditor);
    checkContextMenuOfEmptyPage(webBrowser, jspTextEditor);
    checkContextMenuOfPlainText(webBrowser, jspTextEditor);
    checkContextMenuOfComponent(webBrowser, jspTextEditor);
    checkInsertMenuContent(webBrowser);
    jspTextEditor.close();
	}
	/**
	 * Checks Context Menu of Empty JSP Page
	 * @param webBrowser
	 * @param jspTextEditor
	 */
	private void checkContextMenuOfEmptyPage(SWTBotWebBrowser webBrowser , SWTBotEclipseEditor jspTextEditor){
    jspTextEditor.setText("");
    jspTextEditor.save();
    jspTextEditor.setFocus();
    jspTextEditor.selectRange(0, 0, 0);
    // Test Context menu for empty page
    checkMenuContent(webBrowser,new String[] {"",
      SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL,
      SWTBotWebBrowser.INSERT_BEFORE_MENU_LABEL,
      SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,
      SWTBotWebBrowser.INSERT_INTO_MENU_LABEL,
      SWTBotWebBrowser.REPLACE_WITH_MENU_LABEL,
      "",
      SWTBotWebBrowser.ZOOM_MENU_LABEL,
      "",
      SWTBotWebBrowser.PREFERENCES_MENU_LABEL,
      "",      
      SWTBotWebBrowser.PASTE_MENU_LABEL});

	}
	/**
	 * Checks Context Menu of Plain Text selected within Visual Editor
	 * @param webBrowser
	 * @param jspTextEditor
	 */
	private void checkContextMenuOfPlainText(SWTBotWebBrowser webBrowser , SWTBotEclipseEditor jspTextEditor){
	  jspTextEditor.setText(VisualEditorContextMenuTest.PAGE_TEXT);
    jspTextEditor.save();
    jspTextEditor.setFocus();
    jspTextEditor.selectRange(4, 5, 0);
    checkMenuContent(webBrowser,new String[] {SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (body)",
      "",
      SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL,
      SWTBotWebBrowser.INSERT_BEFORE_MENU_LABEL,
      SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,
      SWTBotWebBrowser.INSERT_INTO_MENU_LABEL,
      SWTBotWebBrowser.REPLACE_WITH_MENU_LABEL,
      SWTBotWebBrowser.EXTERNALIZE_STRING_MENU_LABEL,
      "",
      SWTBotWebBrowser.ZOOM_MENU_LABEL,
      "",
      SWTBotWebBrowser.PREFERENCES_MENU_LABEL,
      "",      
      SWTBotWebBrowser.CUT_MENU_LABEL,
      SWTBotWebBrowser.COPY_MENU_LABEL,
      SWTBotWebBrowser.PASTE_MENU_LABEL});
	}
	 /**
   * Checks Context Menu of Component selected within Visual Editor
   * @param webBrowser
   * @param jspTextEditor
   */
  private void checkContextMenuOfComponent(SWTBotWebBrowser webBrowser , SWTBotEclipseEditor jspTextEditor){
    jspTextEditor.setFocus();
    jspTextEditor.selectRange(5, 23, 0);
    checkMenuContent(webBrowser,new String[] {"<h:outputText> Attributes",
      SWTBotWebBrowser.PARENT_TAG_MENU_LABEL + " (body)",
      "",
      SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL,
      SWTBotWebBrowser.INSERT_BEFORE_MENU_LABEL,
      SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,
      SWTBotWebBrowser.INSERT_INTO_MENU_LABEL,
      SWTBotWebBrowser.REPLACE_WITH_MENU_LABEL,
      SWTBotWebBrowser.STRIP_TAG_MENU_LABEL,
      SWTBotWebBrowser.EXTERNALIZE_STRING_MENU_LABEL,
      "",
      SWTBotWebBrowser.ZOOM_MENU_LABEL,
      "",
      SWTBotWebBrowser.PREFERENCES_MENU_LABEL,
      "",
      SWTBotWebBrowser.CUT_MENU_LABEL,
      SWTBotWebBrowser.COPY_MENU_LABEL,
      SWTBotWebBrowser.PASTE_MENU_LABEL});
  }
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  /**
   * Check content of Visual Editor Context Menu
   * @param jspTextEditor
   * @param expectedMenuLabels
   */
	private void checkMenuContent(SWTBotWebBrowser webBrowser , String[] expectedMenuLabels){
	  webBrowser.setFocus();
	  bot.sleep(Timing.time2S());
    String[] menuLabels = ContextMenuHelper.getMenuItemLabels(webBrowser.getTopMenu(webBrowser.getSelectedDomNode(), 
        SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL));
    assertMenuContent(menuLabels , expectedMenuLabels);
	}
	/**
	 * Asserts equality of arrays containing menu items labels and expected menu items labels
	 * @param menuItems
	 * @param expectedMenuItems
	 */
	private void assertMenuContent (String[] menuItems , String[] expectedMenuItems){
	  assertTrue("Visual Editor Context Menu has wrong content:\n" +
	      "It has items:\n" +
	      VisualEditorContextMenuTest.displayFormattedArrayContent(menuItems) +
	      "Expected items are:\n" +
	      VisualEditorContextMenuTest.displayFormattedArrayContent(expectedMenuItems)
	    ,VisualEditorContextMenuTest.menuItemsLabelsEquals(menuItems, expectedMenuItems));
	}
	/**
	 * Returns formatted interpretation of stringArray used for displaying strinArray content
	 * @param stringArray
	 * @return
	 */
	private static String displayFormattedArrayContent (String[] stringArray){
	  StringBuffer sb = new StringBuffer("");
	  
	  if (stringArray != null){
	    if (stringArray.length > 0){
	      for (String item : stringArray){
	        sb.append(item);
	        sb.append("\n");
	      }
	    }
	    else{
	      sb.append("<empty>\n");
	    }
	  }
	  else{
	    sb.append("<null>\n");
	  }
	  
	  return sb.toString();
  }
	/**
	 * Compare menu items labels stored in string arrays
	 * @param menuLabels1
	 * @param menuLabels2
	 * @return
	 */
	private static boolean menuItemsLabelsEquals (String[] menuLabels1 , String[] menuLabels2){
	  
	  boolean areEqual = false;
	  
	  if (menuLabels1 == null && menuLabels2 == null){
	    areEqual = true;
	  }
	  else if (menuLabels1 != null 
	    && menuLabels2 != null
	    && menuLabels1.length == menuLabels2.length){
	    boolean areEqualYet = true;
	    int index = 0;
	    while (areEqualYet && (index < menuLabels1.length)){
	      if (!ContextMenuHelper.trimMenuItemLabel(menuLabels1[index])
	             .equals(menuLabels2[index])){
	        areEqualYet = false;
	      }
	      index++;
	    }
	    if (areEqualYet){
	      areEqual = true;
	    }
	  }

	  return areEqual;
	  
	}

	/**
	 * Checks Context Menu Content for Insert* Menus and synchronization of these 
	 * menus with JBoss Tools Palette Menus 
	 * @param webBrowser
	 */
	private void checkInsertMenuContent(final SWTBotWebBrowser webBrowser){
	  // Gets Complete Palette Actions Structure
	  MenuItemEntity paletteRootEntity = new MenuItemEntity("root");
	  paletteRootEntity.setChildren(getPaletteActionsStructure());
	  MenuItemEntity contextMenuRootEntity = new MenuItemEntity("root");
	  // Gets Complete Context Menu Structure
	  contextMenuRootEntity.setChildren(getContextMenuStructure(SWTBotWebBrowser.INSERT_AROUND_MENU_LABEL,webBrowser));
	  MenuItemEntity filteredPaleteRootEntity =  filterPaletteMenuForInsertAroundAction(paletteRootEntity);
	  assertTrue("Palette content is different than Context Menu Items for inserting\n" +
	      "Palette  content:\n" + filteredPaleteRootEntity +
	      "\n Context Menu Content:\n" + contextMenuRootEntity,
	    filteredPaleteRootEntity.equals(contextMenuRootEntity));
	  // Gets Complete Context Menu Structure
	  contextMenuRootEntity.setChildren(getContextMenuStructure(SWTBotWebBrowser.INSERT_BEFORE_MENU_LABEL,webBrowser));
    assertTrue("Palette content is different than Context Menu Items for inserting\n" +
	      "Palette  content:\n" + paletteRootEntity +
	      "\n Context Menu Content:\n" + contextMenuRootEntity,
	    paletteRootEntity.equals(contextMenuRootEntity));
	  // Gets Complete Context Menu Structure
	  contextMenuRootEntity.setChildren(getContextMenuStructure(SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,webBrowser));
    assertTrue("Palette content is different than Context Menu Items for inserting\n" +
        "Palette  content:\n" + paletteRootEntity +
        "\n Context Menu Content:\n" + contextMenuRootEntity,
      paletteRootEntity.equals(contextMenuRootEntity));
    // Gets Complete Context Menu Structure
 	  contextMenuRootEntity.setChildren(getContextMenuStructure(SWTBotWebBrowser.INSERT_INTO_MENU_LABEL,webBrowser));
    assertTrue("Palette content is different than Context Menu Items for inserting\n" +
        "Palette  content:\n" + paletteRootEntity +
        "\n Context Menu Content:\n" + contextMenuRootEntity,
      paletteRootEntity.equals(contextMenuRootEntity));
    // Gets Complete Context Menu Structure
    contextMenuRootEntity.setChildren(getContextMenuStructure(SWTBotWebBrowser.REPLACE_WITH_MENU_LABEL,webBrowser));
    assertTrue("Palette content is different than Context Menu Items for inserting\n" +
        "Palette  content:\n" + paletteRootEntity +
        "\n Context Menu Content:\n" + contextMenuRootEntity,
      paletteRootEntity.equals(contextMenuRootEntity));
	}
	/**
	 * Returns complete Palette Actions Structure in data structure 
	 * which can be used for comparing with Context Menu Structure
	 * @return
	 */
	private List<MenuItemEntity> getPaletteActionsStructure(){
	  LinkedList<MenuItemEntity> result = new LinkedList<MenuItemEntity>(); 
    List<PaletteContainer> paletteContainers = SWTBotWebBrowser.getPaletteRootContainers(botExt);
    MenuItemEntity mainGroup = null;
    for (PaletteContainer pc : paletteContainers){
      String[] containerLabelParts = pc.getLabel().split(" ");
      if (mainGroup == null || !mainGroup.getLabel().equals(containerLabelParts[0])){
        mainGroup = new MenuItemEntity(containerLabelParts[0]);
        mainGroup.setChildren(new LinkedList<MenuItemEntity>());
        result.add(mainGroup);
      }
      MenuItemEntity technologyGroup = new MenuItemEntity(containerLabelParts[1]);
      mainGroup.getChildren().add(technologyGroup);
      List<PaletteEntry> pes = SWTBotWebBrowser.getPaletteContainerItems(pc);
      LinkedList<MenuItemEntity> paletteContainerEntries = new LinkedList<MenuItemEntity>();
      for (PaletteEntry pe : pes){
        paletteContainerEntries.add(new MenuItemEntity(pe.getLabel()));
      }
      technologyGroup.setChildren(paletteContainerEntries);
    }   
    return result;
	}
	 /**
   * Returns complete Menu Items Structure in data structure 
   * which can be used for comparing with Context Menu Structure
   * @param topMenuLabel
   * @param webBrowser
   * @return
   */
  private List<MenuItemEntity> getContextMenuStructure(final String topMenuLabel , SWTBotWebBrowser webBrowser){
    final LinkedList<MenuItemEntity> result = new LinkedList<MenuItemEntity>(); 
    final Menu topMenu = webBrowser.getTopMenu(webBrowser.getSelectedDomNode(), topMenuLabel);
    
    UIThreadRunnable.syncExec(new VoidResult() {
      public void run() {
        ContextMenuHelper.clickContextMenu(topMenu, topMenuLabel);
        MenuItem useMenuItem = ContextMenuHelper.getContextMenu(topMenu, topMenuLabel, false);
        Menu useMenu = ContextMenuHelper.showMenuOfMenuItem(useMenuItem);
        for (String firstLevelMenuItemLabel : ContextMenuHelper.getMenuItemLabels(useMenu)){
          ContextMenuHelper.clickContextMenu(useMenu, firstLevelMenuItemLabel);
          MenuItem firstLevelMenuItem = ContextMenuHelper.getContextMenu(useMenu, firstLevelMenuItemLabel, false);
          MenuItemEntity firstLevelMenuItemEntity = new MenuItemEntity(firstLevelMenuItem.getText());
          result.add(firstLevelMenuItemEntity);
          Menu firstLevelMenu = ContextMenuHelper.showMenuOfMenuItem(firstLevelMenuItem);
          LinkedList<MenuItemEntity> firstMenuItemEntities = new LinkedList<MenuItemEntity>();
          firstLevelMenuItemEntity.setChildren(firstMenuItemEntities);
          for (String secondLevelMenuItemLabel : ContextMenuHelper.getMenuItemLabels(firstLevelMenu)){
            ContextMenuHelper.clickContextMenu(firstLevelMenu, secondLevelMenuItemLabel);
            MenuItem secondLevelMenuItem = ContextMenuHelper.getContextMenu(firstLevelMenu, secondLevelMenuItemLabel, false);
            MenuItemEntity secondLevelMenuItemEntity = new MenuItemEntity(secondLevelMenuItem.getText());
            firstMenuItemEntities.add(secondLevelMenuItemEntity);
            Menu secondLevelMenu = ContextMenuHelper.showMenuOfMenuItem(secondLevelMenuItem);
            LinkedList<MenuItemEntity> secondMenuItemEntities = new LinkedList<MenuItemEntity>();
            secondLevelMenuItemEntity.setChildren(secondMenuItemEntities);
            for (String thirdLevelMenuItemLabel : ContextMenuHelper.getMenuItemLabels(secondLevelMenu)){
              // trim name space and <,> characters
              String trimedLabel = thirdLevelMenuItemLabel.split(":")[1].replaceFirst(">", "");
              secondMenuItemEntities.add(new MenuItemEntity(trimedLabel));
            }
          }
        }
        ContextMenuHelper.hideMenuNonRecursively(useMenu);
        ContextMenuHelper.hideMenuRecursively(topMenu);
      }
    });
    return result;
  }
	/**
	 * Filter Menu Item Entities which are not added to Insert Around Context Menu Action
	 * @param rootMenuItemEntity
	 * @return
	 */
  private MenuItemEntity filterPaletteMenuForInsertAroundAction(MenuItemEntity rootMenuItemEntity){
    MenuItemEntity result = new MenuItemEntity(rootMenuItemEntity.getLabel());
    LinkedList<MenuItemEntity> newRootChildren = new LinkedList<MenuItemEntity>();
    result.setChildren(newRootChildren);
    for (MenuItemEntity mieFirstLevel : rootMenuItemEntity.getChildren()){
      String firstLevelMenuItemLabel = mieFirstLevel.getLabel();
      MenuItemEntity mieNewFirstLevel = new MenuItemEntity(firstLevelMenuItemLabel);
      newRootChildren.add(mieNewFirstLevel);
      LinkedList<MenuItemEntity> newFirstLevelChildren = new LinkedList<MenuItemEntity>();
      mieNewFirstLevel.setChildren(newFirstLevelChildren);
      for (MenuItemEntity mieSecondLevel : mieFirstLevel.getChildren()){
        String secondLevelMenuItemLabel = mieSecondLevel.getLabel();
        MenuItemEntity mieNewSecondLevel = new MenuItemEntity(secondLevelMenuItemLabel);
        newFirstLevelChildren.add(mieNewSecondLevel);
        LinkedList<MenuItemEntity> newSecondLevelChildren = new LinkedList<MenuItemEntity>();
        mieNewSecondLevel.setChildren(newSecondLevelChildren);
        for (MenuItemEntity mieThirdLevel : mieSecondLevel.getChildren()){
          String thirdLevelMenuItemLabel = mieThirdLevel.getLabel();
          if (thirdLevelMenuItemLabel.endsWith(" taglib") ||
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("Ajax4Jsf") && thirdLevelMenuItemLabel.equals("queue")) ||
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("RichFaces") && thirdLevelMenuItemLabel.equals("menuSeparator")) || 
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("Seam") && thirdLevelMenuItemLabel.equals("convertEnum")) || 
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("Seam") && thirdLevelMenuItemLabel.equals("convertDateTime")) || 
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("Seam") && thirdLevelMenuItemLabel.equals("enumItem")) || 
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("Seam") && thirdLevelMenuItemLabel.equals("fileUpload")) || 
              (firstLevelMenuItemLabel.equals("JBoss") && secondLevelMenuItemLabel.equals("Seam") && thirdLevelMenuItemLabel.equals("validate")) || 
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("commandButton")) || 
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("graphicImage")) || 
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("inputHidden")) ||
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("inputSecret")) ||
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("inputText")) ||
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("message")) ||
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("messages")) ||
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("outputText")) ||
              (firstLevelMenuItemLabel.equals("JSF") && secondLevelMenuItemLabel.equals("HTML") && thirdLevelMenuItemLabel.equals("selectBooleanCheckbox"))){
            // Skip these Menu Items              
          }
          else{
            MenuItemEntity mieNewThirdLevel = new MenuItemEntity(thirdLevelMenuItemLabel);
            newSecondLevelChildren.add(mieNewThirdLevel);
          }
        }
      }
    }
    
    return result;
  }
  /**
   * Checks Edit Menu functionality i.e. Cut, Copy and Paste Actions
   * @param webBrowser
   * @param jspTextEditor
   */
  private void checkEditMenuFunctionality(SWTBotWebBrowser webBrowser , SWTBotEclipseEditor jspTextEditor){
    jspTextEditor.setText(VisualEditorContextMenuTest.PAGE_TEXT);
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    webBrowser.setFocus();
    botExt.sleep(Timing.time5S());
    // Test Cut
    nsIDOMNode calendarNode = webBrowser.getDomNodeByTagName("INPUT",0);
    botExt.sleep(Timing.time2S());
    webBrowser.selectDomNode(calendarNode,0);
    botExt.sleep(Timing.time2S());
    webBrowser.clickContextMenu(calendarNode, IDELabel.Menu.CUT);
    botExt.sleep(Timing.time2S());
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    String sourceEditorText = jspTextEditor.getText();
    assertTrue ("Source Editor should not contain text '<rich:calendar></rich:calendar>'\nSource Editor Text: " +
        sourceEditorText,
      !sourceEditorText.contains("<rich:calendar></rich:calendar>"));
    // Test Paste
    webBrowser.setFocus();
    nsIDOMNode inputTextNode = webBrowser.getDomNodeByTagName("INPUT",0);
    webBrowser.selectDomNode(inputTextNode,0);
    botExt.sleep(Timing.time2S());
    webBrowser.clickContextMenu(inputTextNode, IDELabel.Menu.PASTE);
    botExt.sleep(Timing.time2S());    
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    sourceEditorText = jspTextEditor.getText();
    assertTrue ("Source Editor should contain text '<rich:calendar></rich:calendar>'\nSource Editor Text: " +
        sourceEditorText,
      sourceEditorText.contains("<rich:calendar></rich:calendar>"));
    // Test Copy
    calendarNode = webBrowser.getDomNodeByTagName("INPUT",0);
    webBrowser.setFocus();
    webBrowser.selectDomNode(calendarNode,0);
    botExt.sleep(Timing.time2S());
    webBrowser.clickContextMenu(calendarNode, IDELabel.Menu.COPY);
    botExt.sleep(Timing.time2S());
    inputTextNode = webBrowser.getDomNodeByTagName("INPUT",1);
    webBrowser.selectDomNode(inputTextNode,0);
    botExt.sleep(Timing.time2S());
    webBrowser.clickContextMenu(inputTextNode, IDELabel.Menu.PASTE);
    botExt.sleep(Timing.time2S());
    jspTextEditor.save();
    sourceEditorText = jspTextEditor.getText();
    assertTrue ("Source Editor should not contain text '<h:inputText/>'\nSource Editor Text: " +
        sourceEditorText,
      !sourceEditorText.contains("<h:inputText/>"));
    // Test editing using keyboard shortcuts
    jspTextEditor.setFocus();
    jspTextEditor.setText(VisualEditorContextMenuTest.PAGE_TEXT);
    jspTextEditor.save();
    bot.sleep(Timing.time2S());
    jspTextEditor.selectRange(6, 9, 0);
    bot.sleep(Timing.time2S());
    webBrowser.setFocus();
    // Test Cut
    System.out.println(1);
    calendarNode = webBrowser.getDomNodeByTagName("INPUT",0);
    bot.sleep(Timing.time2S());
    webBrowser.selectDomNode(calendarNode,0);
    botExt.sleep(Timing.time2S());
    webBrowser.setFocus();
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'x'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_X,KeyEvent.VK_CONTROL);  
    }
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    sourceEditorText = jspTextEditor.getText();
    assertTrue ("Source Editor should not contain text '<rich:calendar></rich:calendar>'\nSource Editor Text: " +
        sourceEditorText,
      !sourceEditorText.contains("<rich:calendar></rich:calendar>"));
    // Test Paste
    webBrowser.setFocus();
    inputTextNode = webBrowser.getDomNodeByTagName("INPUT",0);
    botExt.sleep(Timing.time2S());
    webBrowser.selectDomNode(inputTextNode,0);
    botExt.sleep(Timing.time2S());
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'v'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_V,KeyEvent.VK_CONTROL);
    }
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    sourceEditorText = jspTextEditor.getText();
    assertTrue ("Source Editor should contain text '<rich:calendar></rich:calendar>'\nSource Editor Text: " +
        sourceEditorText,
      sourceEditorText.contains("<rich:calendar></rich:calendar>"));
    // Test Copy
    webBrowser.setFocus();
    botExt.sleep(Timing.time2S());
    calendarNode = webBrowser.getDomNodeByTagName("INPUT",0);
    botExt.sleep(Timing.time2S());
    webBrowser.selectDomNode(calendarNode,0);
    botExt.sleep(Timing.time2S());
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'c'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_C,KeyEvent.VK_CONTROL);
    }
    inputTextNode = webBrowser.getDomNodeByTagName("INPUT",1);
    botExt.sleep(Timing.time2S());
    webBrowser.selectDomNode(inputTextNode,0);
    botExt.sleep(Timing.time2S());
    if (SWTJBTExt.isRunningOnMacOs()){
      bot.shells()[0].pressShortcut(SWT.COMMAND, 'v'); 
    }
    else{
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_V,KeyEvent.VK_CONTROL);
    }
    jspTextEditor.save();
    botExt.sleep(Timing.time2S());
    sourceEditorText = jspTextEditor.getText();
    assertTrue ("Source Editor should not contain text '<h:inputText/>'\nSource Editor Text: " +
        sourceEditorText,
      !sourceEditorText.contains("<h:inputText/>"));
    
  }
  /**
   * Checks Insert Menu Functionality
   * @param webBrowser
   * @param jspTextEditor
   */
  private void checkInsertMenuFunctionality(SWTBotWebBrowser webBrowser , SWTBotEclipseEditor jspTextEditor){
    
  }
}

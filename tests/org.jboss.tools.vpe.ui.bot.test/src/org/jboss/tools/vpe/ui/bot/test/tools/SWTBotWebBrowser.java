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

package org.jboss.tools.vpe.ui.bot.test.tools;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.ui.IEditorReference;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.ObjectMultiPageEditorBot;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.MozillaEventAdapter;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIEditor;
import org.mozilla.interfaces.nsISelection;
import org.mozilla.interfaces.nsISelectionController;
import org.mozilla.interfaces.nsISelectionListener;
import org.mozilla.interfaces.nsISupports;
import org.w3c.dom.Node;

/**
 * Helper to work with web browser used by JBoss Tools contained in JSP MultiPage Editor
 * @author Vladimir Pakan
 *
 */
public class SWTBotWebBrowser {
  public static final String INSERT_AROUND_MENU_LABEL = "Insert around";
  public static final String INSERT_BEFORE_MENU_LABEL = "Insert before";
  public static final String INSERT_AFTER_MENU_LABEL = "Insert after";
  public static final String INSERT_INTO_MENU_LABEL = "Insert into";
  public static final String REPLACE_WITH_MENU_LABEL = "Replace with"; 
  public static final String STRIP_TAG_MENU_LABEL = "Strip Tag"; 
  public static final String JSF_MENU_LABEL = "JSF";
  public static final String HTML_MENU_LABEL = "HTML";
  public static final String H_OUTPUT_TEXT_TAG_MENU_LABEL = "<h:outputText>";
  
  private Display display;
  private IVisualEditor visualEditor;
  private MozillaEditor mozillaEditor;
  private SWTBot bot;
  
  public SWTBotWebBrowser (String title, SWTBot bot){
    ObjectMultiPageEditorBot objectMultiPageEditorBot = new ObjectMultiPageEditorBot(title);
    IEditorReference ref = objectMultiPageEditorBot.getEditorReference();
    JSPMultiPageEditor multiPageEditor = null;
    if (ref.getPart(true) instanceof JSPMultiPageEditor) {
      multiPageEditor = (JSPMultiPageEditor)ref.getPart(true);
    }
    assertNotNull(multiPageEditor);
    this.bot = bot;
    this.visualEditor = multiPageEditor.getVisualEditor();
    this.mozillaEditor = ((VpeEditorPart)multiPageEditor.getVisualEditor()).getVisualEditor();
    this.display = getBrowser().getDisplay();
  }
   
  /**
   * For debug purposes. Displays formatted node
   * @param node
   * @param depth
   */
  @SuppressWarnings("unused")
  private static void displayNsIDOMNode(nsIDOMNode node , int depth) {
    System.out.println("");
    System.out.print(fillString(' ', depth) + "<" + node.getNodeName() + " ");

    // display node's attributes
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      nsIDOMNamedNodeMap modelAttributes = node.getAttributes();
      for (int i = 0; i < modelAttributes.getLength(); i++) {
        nsIDOMNode modelAttr = modelAttributes.item(i);
        System.out.print(modelAttr.getNodeName() + "=" + modelAttr.getNodeValue().replaceAll("\n", "") + " ");
      }
    }
    System.out.println(">");
    if (node.getNodeValue() != null){
      System.out.println(fillString(' ', depth + 2) + node.getNodeValue());
    }
    // display children
    nsIDOMNodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {

      nsIDOMNode child = children.item(i);

      // leave out empty text nodes in test dom model
      if ((child.getNodeType() == Node.TEXT_NODE)
          && ((child.getNodeValue() == null) || (child.getNodeValue().trim()
              .length() == 0)))
        continue;

      displayNsIDOMNode(child, depth + 2);

    }
    System.out.println(fillString(' ', depth) + "<" + node.getNodeName() + "/>");
  }
  /**
   * Displays complete browser DOM
   */
  public void displayWebBrowserDOM(){
    SWTBotWebBrowser.displayNsIDOMNode(mozillaEditor.getDomDocument(), 0);
  }
  /**
   * Returns browser DOM Document
   * @return
   */
  public nsIDOMDocument getNsIDOMDocument(){
    return mozillaEditor.getDomDocument();
  }
  /**
   * Selects node within visual editor
   * @param node
   * @param offset
   */
  public void selectDomNode (final nsIDOMNode node, final int offset){
    setFocus();
    final nsISelection selection = getSelectionController().getSelection(nsISelectionController.SELECTION_NORMAL);
    selection.removeAllRanges();
    selection.collapse(node,offset);
    display.syncExec(new Runnable() {
      public void run() {
        getMozillaEventAdapter().notifySelectionChanged(getNsIDOMDocument(), selection, nsISelectionListener.MOUSEDOWN_REASON);
      }
    });
    try {
      Thread.sleep(Timing.time5S());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  /**
   * Returns node corresponding to specified tagName
   * @param tagName
   * @param order - index of tagName tag in DOM model from all tagName nodes contained in model
   * @return
   */
  public nsIDOMNode getDomNodeByTagName(String tagName , int order) {

    nsIDOMNode result = null;
    
    nsIDOMNodeList nodeList = getNsIDOMDocument().getElementsByTagName(tagName);

    if (nodeList.getLength() > order){
      result = nodeList.item(order);
    }
    
    return result;
    
  }
  /**
   * Returns first node corresponding to specified tagName
   * @param tagName
   * @return
   */
  public nsIDOMNode getDomNodeByTagName(String tagName) {
    return getDomNodeByTagName(tagName, 0);
  }  
  /**
   * Returns nsIDOMWindow containing WebBrowser Content
   * @return
   */
  public nsIDOMWindow getContentDOMWindow (){
    return mozillaEditor.getXulRunnerEditor().getWebBrowser().getContentDOMWindow();
  }
  /**
   * Returns WebBrowser nsISelectionController
   * @return
   */
  public nsISelectionController getSelectionController(){
    return getEditor().getSelectionController();
  }
  /**
   * Returns WebBrowser nsIEditor
   * @return
   */
  public nsIEditor getEditor(){
    return mozillaEditor.getEditor();
    
  }
  /**
   * Returns Mozilla Event Adapter MozillaEventAdapter
   * @return
   */
  public MozillaEventAdapter getMozillaEventAdapter(){
    return mozillaEditor.getMozillaEventAdapter();
    
  }
  /**
   * Returns selected DOM Node
   * @return
   */
  public nsIDOMNode getSelectedDomNode (){
    nsIDOMNode result = null;
    display.syncExec(new Runnable() {
      public void run() {
        mozillaEditor.setFocus();
      }
    });
    nsISelection selection = getSelectionController().getSelection(nsISelectionController.SELECTION_NORMAL);
    if (selection != null){
      result = selection.getFocusNode();
    }
    return result;
  }
  /**
   * Sets focus to Web Browser
   */
  public void setFocus(){
    display.syncExec(new Runnable() {
      public void run() {
        mozillaEditor.setFocus();
      }
    });
  }
  /**
   * Returns associated SWT Browser
   * @return
   */
  public Browser getBrowser (){
    return mozillaEditor.getXulRunnerEditor().getBrowser(); 
  }
  /**
   * Fill string with length count with character ch 
   * @param ch
   * @param count
   * @return
   */
  private static String fillString (char ch, int count){
    String result = null;
    if (count > 0){
      char[] charArray = new char[count];
      Arrays.fill(charArray, ch);
      result = new String(charArray);
    }
    else {
      result = "";
    }
    return result;
  }
  /**
   * Returns associated Moziilla Editor
   * @return
   */
  public MozillaEditor getMozillaEditor() {
    return mozillaEditor;
  }
  /**
   * Clicks on Browser Context Menu
   * @param node
   * @param menus
   */
  public void clickContextMenu(final nsIDOMNode node , final String... menuLabels){
    // Create Context Menu Event
    final nsIDOMEvent domEvent = new nsIDOMEvent() {
      
      public nsISupports queryInterface(String arg0) {
        return XPCOM.queryInterface(node,nsISupports.class);
      }
      public void stopPropagation() {
      }
      public void preventDefault() {
      }
      public void initEvent(String arg0, boolean arg1, boolean arg2) {
      }
      public String getType() {
        return "contextmenu";
      }
      public double getTimeStamp() {
        return 0;
      }
      public nsIDOMEventTarget getTarget() {
        return XPCOM.queryInterface(node,nsIDOMEventTarget.class);
      }
      public int getEventPhase() {
        return 0;
      }
      
      public nsIDOMEventTarget getCurrentTarget() {
        return XPCOM.queryInterface(node,nsIDOMEventTarget.class);
      }
      public boolean getCancelable() {
        return false;
      }
      public boolean getBubbles() {
        return false;
      }
    };
    // Simulate Context Menu Event
    display.syncExec(new Runnable() {
      public void run() {
        getMozillaEventAdapter().handleEvent(domEvent);
      }
    });
    // Get Top Menu
    Menu topMenu = UIThreadRunnable.syncExec(new WidgetResult<Menu>() {
      public Menu run() {
        Menu result = null;
        Composite parent = mozillaEditor.getControl().getParent();
        while (!(parent instanceof Decorations)){
          parent = parent.getParent();
        }
        try {
          Field menusField = Decorations.class.getDeclaredField("menus");
          menusField.setAccessible(true);
          Object menusObject = menusField.get(parent);
          if (menusObject != null){
            Menu[] menus = (Menu[])menusObject;
            MenuItem topMenuItem = null;
            int index = menus.length - 1;
            while (topMenuItem == null && index >= 0){
              if (menus[index] != null){
                MenuItem[] menuItems = menus[index].getItems();
                int menuItemIndex = 0;
                while (topMenuItem == null && menuItemIndex < menuItems.length){
                  if (menuItems[menuItemIndex].getText().equals(menuLabels[0])){
                    topMenuItem = menuItems[menuItemIndex];
                  }
                  menuItemIndex++;
                }
              }
              index--;
            }
            if (topMenuItem != null){
              result = topMenuItem.getParent();
            }
          }
          else{
            throw new WidgetNotFoundException("Unable to find MenuItem with label " + menuLabels[0]);
          }
        } catch (SecurityException se) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + menuLabels[0],se);
        } catch (NoSuchFieldException nsfe) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + menuLabels[0],nsfe);
        } catch (IllegalArgumentException iae) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + menuLabels[0],iae);
        } catch (IllegalAccessException iace) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + menuLabels[0],iace);
        }
        return result;
      }});
    
    ContextMenuHelper.clickContextMenu(topMenu, menuLabels);

  }

}

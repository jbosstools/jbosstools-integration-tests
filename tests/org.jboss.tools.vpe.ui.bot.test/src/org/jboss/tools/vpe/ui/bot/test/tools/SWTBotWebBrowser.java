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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.gef.finder.finders.PaletteFinder;
import org.eclipse.swtbot.eclipse.gef.finder.matchers.ToolEntryLabelMatcher;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.model.ui.views.palette.PaletteCreator;
import org.jboss.tools.jst.jsp.editor.IVisualEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.PalettePageImpl;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.ReflectionsHelper;
import org.jboss.tools.ui.bot.ext.parts.ObjectMultiPageEditorBot;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.mozilla.MozillaEventAdapter;
import org.jboss.tools.vpe.ui.palette.PaletteAdapter;
import org.jboss.tools.vpe.xulrunner.util.XPCOM;
import org.mozilla.interfaces.nsIDOMAbstractView;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMEvent;
import org.mozilla.interfaces.nsIDOMEventTarget;
import org.mozilla.interfaces.nsIDOMMouseEvent;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMRange;
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
  public static final String PARENT_TAG_MENU_LABEL = "Parent Tag";
  public static final String INSERT_AROUND_MENU_LABEL = "Insert around";
  public static final String INSERT_BEFORE_MENU_LABEL = "Insert before";
  public static final String INSERT_AFTER_MENU_LABEL = "Insert after";
  public static final String INSERT_INTO_MENU_LABEL = "Insert into";
  public static final String REPLACE_WITH_MENU_LABEL = "Replace with"; 
  public static final String STRIP_TAG_MENU_LABEL = "Strip Tag"; 
  public static final String ZOOM_MENU_LABEL = "Zoom";
  public static final String CUT_MENU_LABEL = "Cut";
  public static final String COPY_MENU_LABEL = "Copy";
  public static final String PASTE_MENU_LABEL = "Paste";
  public static final String SETUP_VISUAL_TEMPLATE_FOR_MENU_LABEL = "Setup Visual Template for ";
  public static final String SELECT_THIS_TAG_MENU_LABEL = "Select This Tag";
  public static final String PREFERENCES_MENU_LABEL = "Preferences..";
  public static final String EXTERNALIZE_STRING_MENU_LABEL = "Externalize selected string...";
  public static final String TEAM_MENU_LABEL = "Team";

  public static final String JSF_MENU_LABEL = "JSF";
  public static final String JBOSS_MENU_LABEL = "JBoss";
  public static final String RICH_FACES_MENU_LABEL = "RichFaces";
  public static final String HTML_MENU_LABEL = "HTML";
  public static final String H_OUTPUT_TEXT_TAG_MENU_LABEL = "<h:outputText>";
  public static final String H_FORM_TAG_MENU_LABEL = "<h:form>";
  public static final String RICH_CALENDAR_TAG_MENU_LABEL = "<rich:calendar>";
  
  private static final String MOUSE_CLICK_EVENT_TYPE="click"; //$NON-NLS-1$^M
  private static final String CONTEXT_MENU_EVENT_TYPE="contextmenu"; //$NON-NLS-1$^M
  
  private Display display;
  private IVisualEditor visualEditor;
  private MozillaEditor mozillaEditor;
  private SWTBotExt bot;
  
  public SWTBotWebBrowser (String title, SWTBotExt bot){
    ObjectMultiPageEditorBot objectMultiPageEditorBot = new ObjectMultiPageEditorBot(title);
    IEditorReference ref = objectMultiPageEditorBot.getEditorReference();
    JSPMultiPageEditor multiPageEditor = null;
    if (ref.getPart(true) instanceof JSPMultiPageEditor) {
      multiPageEditor = (JSPMultiPageEditor)ref.getPart(true);
    }
    assertNotNull(multiPageEditor);
    this.bot = bot;
    this.visualEditor = multiPageEditor.getVisualEditor();
    this.mozillaEditor = ((VpeEditorPart)visualEditor).getVisualEditor();
    this.display = getBrowser().getDisplay();
  }
   
  /**
   * For debug purposes. Displays formatted node
   * @param node
   * @param depth
   */
  public static void displayNsIDOMNode(nsIDOMNode node , int depth) {
    System.out.println("");
    System.out.print(fillString(' ', depth) + "<" + node.getNodeName() + " ");

    // display node's attributes
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      nsIDOMNamedNodeMap modelAttributes = node.getAttributes();
      for (int i = 0; i < modelAttributes.getLength(); i++) {
        nsIDOMNode modelAttr = modelAttributes.item(i);
        System.out.print(modelAttr.getNodeName() + "=\"" + modelAttr.getNodeValue().replaceAll("\n", " ") + "\" ");
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
    System.out.println(fillString(' ', depth) + "</" + node.getNodeName() + ">");
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
   * Returns number of occurrences of specified tagName
   * @param tagName
   * @return
   */
  public long getDomNodeOccurenciesByTagName(String tagName) {

    return getNsIDOMDocument().getElementsByTagName(tagName).getLength();
    
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
    nsISelection selection = getSelection();
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
    Menu topMenu = getTopMenu(node, menuLabels[0]);
    ContextMenuHelper.clickContextMenu(topMenu, menuLabels);
  }
  /**
   * Returns node corresponding to specified tagName
   * @param parentNode
   * @param tagName
   * @param order - index of tagName tag in DOM model from all tagName nodes contained in model
   * @return
   */
  public nsIDOMNode getDomNodeByTagName(nsIDOMNode parentNode , String tagName, Integer order){
    List<nsIDOMNode> nodes = getDomNodesByTagName(parentNode,tagName);
    nsIDOMNode result = null;
    if (nodes != null && nodes.size() > order){
      result = nodes.get(order);
    }
    return result;
  }
  /**
   * Recursively search for node with specified tagName
   * @param parentNode
   * @param tagName
   * @return
   */
  public List<nsIDOMNode> getDomNodesByTagName(nsIDOMNode parentNode , String tagName){
    LinkedList<nsIDOMNode> result = new LinkedList<nsIDOMNode>();
    if (parentNode.getNodeName().equals(tagName)){
      result.add(parentNode);
    }  
    nsIDOMNodeList children = parentNode.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      nsIDOMNode child = children.item(i);
      // leave out empty text nodes in test dom model
      if ((child.getNodeType() == Node.TEXT_NODE)
          && ((child.getNodeValue() == null) || (child.getNodeValue().trim()
              .length() == 0)))
        continue;

      result.addAll(getDomNodesByTagName(child, tagName));

    }
    return result;
    
  }
  /**
   * Activate JBoss Tools Palette Tool with specified Label
   * @param toolLabel
   */
  public void activatePaletteTool (String toolLabel){
    
    SWTBotWebBrowser.activatePaletteTool(bot,toolLabel);

  }
  /**
   * Activate JBoss Tools Palette Tool with specified Label static version
   * @param toolLabel
   */
  public static void activatePaletteTool (SWTBotExt bot , String toolLabel){
    
    PaletteViewer paletteViewer = SWTBotWebBrowser.getPaletteViewer(bot);
    PaletteEntry paletteEntry = SWTBotWebBrowser.getPaletteEntry(paletteViewer , toolLabel);
    paletteViewer.setActiveTool((ToolEntry) paletteEntry);

  }
  /**
   * Returns true if node or it's child has node with specified name and attributes with specified values
   * @param node
   * @param searchName
   * @param attributeNames
   * @param attributeValues
   * @return
   */
  public boolean containsNodeWithNameAndAttributes(nsIDOMNode node, String searchName , String[] attributeNames , String[] attributeValues) {

    boolean result = false;

    String nodeName = node.getNodeName();
    if (nodeName != null && nodeName.equals(searchName)) {
      // Test Attributes
      if (attributeNames != null){
        boolean attributesAreEqual = true;
        nsIDOMNamedNodeMap attributesMap = node.getAttributes();
        for (int index = 0 ; index < attributeNames.length && attributesAreEqual; index++){
          nsIDOMNode attributeNode = attributesMap.getNamedItem(attributeNames[index]);
          if (attributeNode != null){
            if (!SWTBotWebBrowser.stripTextFromSpecChars(attributeNode.getNodeValue())
                  .equalsIgnoreCase(attributeValues[index])){
              attributesAreEqual = false;
            }
          }
          else{
            attributesAreEqual = false;
          }
        }
        if (attributesAreEqual){
          result = true;
        }
      }
      else{
        result = true;
      }
    } 
    
    if (!result) {
      nsIDOMNodeList children = node.getChildNodes();

      for (int i = 0; i < children.getLength() && !result; i++) {

        nsIDOMNode child = children.item(i);

        // leave out empty text nodes in test dom model
        if ((child.getNodeType() == Node.TEXT_NODE)
            && ((child.getNodeValue() == null) || (child.getNodeValue().trim()
                .length() == 0)))
          continue;

        result = containsNodeWithNameAndAttributes(child, searchName,attributeNames,attributeValues);
      }
    }
    
    return result;

  }
  /**
   * Returns true if node or it's child has node with specified name
   * @param node
   * @param searchName
   * @return
   */
  public boolean containsNodeWithNameAndAttributes(nsIDOMNode node, String searchName) {
    
    return containsNodeWithNameAndAttributes(node, searchName,null,null);
    
  }

  /**
   * Returns true if node or it's child has value searchText
   * @param node
   * @param searchText
   * @return
   */
  public boolean containsNodeWithValue(nsIDOMNode node, String searchText) {
	  boolean result = false;
	  String nodeValue = node.getNodeValue();

	  if (nodeValue != null && SWTBotWebBrowser.stripTextFromSpecChars(nodeValue).equals(searchText)) {
		  result = true;
	  } else {
		  nsIDOMNodeList children = node.getChildNodes();
		  for (int i = 0; i < children.getLength() && !result; i++) {
			  nsIDOMNode child = children.item(i);
			  // leave out empty text nodes in test dom model
			  if ((child.getNodeType() == Node.TEXT_NODE)
					  && ((child.getNodeValue() == null) || 
							  (child.getNodeValue().trim().length() == 0))) {
				  continue;
			  }
			  result = containsNodeWithValue(child, searchText);
		  }
	  }
	  return result;
  }
  /**
   * Returns true if visual editor contains node with value searchText
   * @param webBrowser
   * @param searchText
   * @return
   */
  public boolean containsNodeWithValue(SWTBotWebBrowser webBrowser, String searchText) {
    return containsNodeWithValue(webBrowser.getMozillaEditor().getDomDocument(), searchText);
  }
  /**
   * Returns Palette Viewer associated to JBoss Tools Palette
   * @return
   */
  private static PaletteViewer getPaletteViewer (SWTBotExt bot){
    SWTEclipseExt.showView(bot, ViewType.PALETTE);

    IViewReference ref = UIThreadRunnable
        .syncExec(new Result<IViewReference>() {
          public IViewReference run() {
            IViewReference ref = null;
            IViewReference[] viewReferences = null;
            viewReferences = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getViewReferences();
            for (IViewReference reference : viewReferences) {
              if (reference.getTitle().equals(ViewType.PALETTE.getViewLabel())) {
                return reference;
              }
            }
            return ref;
          }
        });
    // Find Palette Viewer dirty way
    PaletteView pv = (PaletteView)ref.getPart(true);
    PalettePageImpl ppi = (PalettePageImpl)pv.getCurrentPage();
    try {
      PaletteCreator pc = ReflectionsHelper.getPrivateFieldValue(
          PalettePageImpl.class, "paletteCreator", ppi, PaletteCreator.class);
      PaletteAdapter pa = ReflectionsHelper.getPrivateFieldValue(
          PaletteCreator.class, "paletteAdapter", pc, PaletteAdapter.class);
      PaletteViewer paletteViewer = ReflectionsHelper.getPrivateFieldValue(
          PaletteAdapter.class, "viewer", pa, PaletteViewer.class);
      return paletteViewer;
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

  }
  /**
   * Returns Palette Entry from Palette Viewer paletteViewer with entryLabel label
   * @param paletteViewer
   * @param entryLabel
   * @return
   */
  private static PaletteEntry getPaletteEntry (PaletteViewer paletteViewer , String entryLabel) {
    
    PaletteEntry result = null;
    EditDomain ed = new EditDomain();
    ed.setPaletteViewer(paletteViewer);
    ed.setPaletteRoot(paletteViewer.getPaletteRoot());
    PaletteFinder pf = new PaletteFinder(ed);
    ToolEntryLabelMatcher telm = new ToolEntryLabelMatcher(entryLabel);
    List<PaletteEntry> paletteEntries = pf.findEntries(telm);
    if (paletteEntries != null && paletteEntries.size() > 0) {
       result = paletteEntries.get(0);
    } else {
      throw new WidgetNotFoundException(
          "Unable to find JBoss Tools Palette Entry with label: " + entryLabel);
    }
    
    return result;

  }
  /**
   * Returns true if JBoss Tools Palette Contains Palette Entry with label paletteEntryLabel
   * @param paletteEntryLabel
   * @return
   */
  public static boolean paletteContainsPaletteEntry (SWTBotExt bot,String paletteEntryLabel){
    boolean result = false;
    PaletteViewer paletteViewer = SWTBotWebBrowser.getPaletteViewer(bot);
    try{
      SWTBotWebBrowser.getPaletteEntry(paletteViewer , paletteEntryLabel);
      result = true;
    } catch (WidgetNotFoundException wnfe){
      result = false;
    }
    
    return result;
  }
  /**
   * Returns browser selection
   * @return
   */
  public nsISelection getSelection (){
    nsISelection result = null;
    display.syncExec(new Runnable() {
      public void run() {
        mozillaEditor.setFocus();
      }
    });
    result = getSelectionController().getSelection(nsISelectionController.SELECTION_NORMAL);
    return result;
  }
  /**
   * Returns top context menu of Visual Editor
   * @param node
   * @param topMenuItemLabel
   * @return
   */
  public Menu getTopMenu(final nsIDOMNode node , final String topMenuItemLabel){
    // Create Context Menu Event
    final nsIDOMEvent domEvent = new nsIDOMEvent() {
      
      public nsISupports queryInterface(String arg0) {
        return node != null ? XPCOM.queryInterface(node,nsISupports.class) : null;
      }
      public void stopPropagation() {
      }
      public void preventDefault() {
      }
      public void initEvent(String arg0, boolean arg1, boolean arg2) {
      }
      public String getType() {
        return SWTBotWebBrowser.CONTEXT_MENU_EVENT_TYPE;
      }
      public double getTimeStamp() {
        return 0;
      }
      public nsIDOMEventTarget getTarget() {
        return node != null ? XPCOM.queryInterface(node,nsIDOMEventTarget.class) : null;
      }
      public int getEventPhase() {
        return 0;
      }
      
      public nsIDOMEventTarget getCurrentTarget() {
        return node != null ? XPCOM.queryInterface(node,nsIDOMEventTarget.class) : null;
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
    bot.sleep(Timing.time2S());    
    // Get Top Menu
    return UIThreadRunnable.syncExec(new WidgetResult<Menu>() {
      public Menu run() {        
        Menu result = null;
        Object menusHolder = null;
        if (SWTJBTExt.isRunningOnMacOs()){
          menusHolder = mozillaEditor.getControl().getDisplay();
        }
        else{
          Composite parent = (Composite)mozillaEditor.getControl().getParent();
          while (!(parent instanceof Decorations)){
            parent = parent.getParent();
          }
          menusHolder = parent;
        }        
        try {          
          Menu[] menus = ReflectionsHelper.getPrivateFieldValue(SWTJBTExt.isRunningOnMacOs() ? Display.class : Decorations.class,
              "menus",
              menusHolder,
              Menu[].class);          
          if (menus != null){
            MenuItem topMenuItem = null;
            int index = menus.length - 1;
            while (topMenuItem == null && index >= 0){
              if (menus[index] != null){
                MenuItem[] menuItems = menus[index].getItems();
                int menuItemIndex = 0;
                while (topMenuItem == null && menuItemIndex < menuItems.length){
                  if (ContextMenuHelper.trimMenuItemLabel(menuItems[menuItemIndex].getText())
                        .equals(topMenuItemLabel)){
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
            throw new WidgetNotFoundException("Unable to find MenuItem with label " + topMenuItemLabel);
          }
        } catch (SecurityException se) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + topMenuItemLabel,se);
        } catch (NoSuchFieldException nsfe) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + topMenuItemLabel,nsfe);
        } catch (IllegalArgumentException iae) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + topMenuItemLabel,iae);
        } catch (IllegalAccessException iace) {
          throw new WidgetNotFoundException("Unable to find MenuItem with label " + topMenuItemLabel,iace);
        }
        return result;
      }});
    
  }
  /**
   * Returns Palette Containers
   * @param bot
   * @return
   */
  public static final List<PaletteContainer> getPaletteRootContainers (SWTBotExt bot) {
    List<PaletteContainer> result = null;   
    final PaletteViewer paletteViewer = SWTBotWebBrowser.getPaletteViewer(bot);
    result = UIThreadRunnable.syncExec(new Result<LinkedList<PaletteContainer>> (){
      public LinkedList<PaletteContainer> run() {
        LinkedList<PaletteContainer> paletteContainers = new LinkedList<PaletteContainer>();
        for (Object o : paletteViewer.getPaletteRoot().getChildren()){
          if (o instanceof PaletteContainer){
            paletteContainers.add((PaletteContainer)o);
          }
        }
        return paletteContainers;
      }
    });
    
    return result;  
  }
  /**
   * Returns Palette Entries of paletteContainer
   * @param paletteContainer
   * @return
   */
  public static final List<PaletteEntry> getPaletteContainerItems (final PaletteContainer paletteContainer) {
    List<PaletteEntry> result = null;
    result = UIThreadRunnable.syncExec(new Result<LinkedList<PaletteEntry>> (){
      public LinkedList<PaletteEntry> run() {
        LinkedList<PaletteEntry> paletteEntries = new LinkedList<PaletteEntry>();
        for (Object o : paletteContainer.getChildren()){
          if (o instanceof PaletteEntry && !(o instanceof PaletteContainer)){
            paletteEntries.add((PaletteEntry)o);
          }
        }
        return paletteEntries;
      }
    });    
    return result;
  }
  
  /**
   * Returns true if JBoss Tools Palette Contains Root Palette Container with label paletteContainerLabel
   * @param paletteContainerLabel
   * @return
   */
  public static boolean paletteContainsRootPaletteCotnainer (SWTBotExt bot,String paletteContainerLabel){
    boolean result = false;
    List<PaletteContainer> paletteContainers = SWTBotWebBrowser.getPaletteRootContainers(bot);
    if (paletteContainers != null){
      Iterator<PaletteContainer> pcIterator = paletteContainers.iterator();
      while (pcIterator.hasNext() && !result){
        if (pcIterator.next().getLabel().equals(paletteContainerLabel)){
          result = true;
        }
      }
    }
    return result;
  }
  /**
   * Returns selected text in first selection range.
   * Works correctly only when single node is selected. 
   * @return
   */
  public String getSelectionText (){
    String result = null;
    
    nsISelection selection = getSelection();
    if (selection != null && selection.getRangeCount() > 0){
      nsIDOMRange firstSelectedRange = selection.getRangeAt(0);
      nsIDOMNode selectedNode = getSelectedDomNode();
      if (selectedNode.getNodeName().equals("#text")){
        int startOffset = firstSelectedRange.getStartOffset();
        int endOffset = firstSelectedRange.getEndOffset();
        int beginIndex, endIndex;
        if (startOffset > endOffset){
          beginIndex = endOffset;
          endIndex = startOffset;
        }
        else{
          beginIndex = startOffset;
          endIndex = endOffset;
        }
        result = selectedNode
          .getNodeValue()
          .substring(beginIndex, endIndex);  
      }
    }
    
    return result;
    
  }
  /**
   * Returns inputText striped from spaces, tabs and EOL
   * @param inputText
   * @return String
   */
  protected static String stripTextFromSpecChars(String inputText){
    return inputText.replaceAll("\n", " ").replaceAll("\t", "").trim();
  }
  
  public void mouseClickOnNode(final nsIDOMNode node){
    // Create DOM Event
    final nsIDOMMouseEvent domMouseEvent = new DomMouseEvent(node);
    // Simulate Context Menu Event
    display.syncExec(new Runnable() {
      public void run() {
        getMozillaEventAdapter().handleEvent(domMouseEvent);
      }
    });
  }
  
  private class DomMouseEvent implements nsIDOMMouseEvent {
    private nsIDOMNode node = null;
    public DomMouseEvent (nsIDOMNode node){
      this.node = node;
    }
    @Override
    public int getDetail() {
      return 0;
    }
    @Override
    public nsIDOMAbstractView getView() {
      return null;
    }
    @Override
    public void initUIEvent(String arg0, boolean arg1, boolean arg2,
        nsIDOMAbstractView arg3, int arg4) {
    }
    @Override
    public boolean getBubbles() {
      return false;
    }
    @Override
    public boolean getCancelable() {
      return false;
    }
    @Override
    public nsIDOMEventTarget getCurrentTarget() {
      return node != null ? XPCOM.queryInterface(node,nsIDOMEventTarget.class) : null;
    }
    @Override
    public int getEventPhase() {
      return 0;
    }
    @Override
    public nsIDOMEventTarget getTarget() {
      return node != null ? XPCOM.queryInterface(node,nsIDOMEventTarget.class) : null;
    }
    @Override
    public double getTimeStamp() {
      return 0;
    }
    @Override
    public String getType() {
      return SWTBotWebBrowser.MOUSE_CLICK_EVENT_TYPE;
    }
    @Override
    public void initEvent(String arg0, boolean arg1, boolean arg2) {
    }
    @Override
    public void preventDefault() {
    }
    @Override
    public void stopPropagation() {
    }
    @Override
    public nsIDOMMouseEvent queryInterface(String arg0) {
      return this;
    }
    @Override
    public boolean getAltKey() {
      return false;
    }
    @Override
    public int getButton() {
      return 0;
    }
    @Override
    public int getClientX() {
      return 0;
    }
    @Override
    public int getClientY() {
      return 0;
    }
    @Override
    public boolean getCtrlKey() {
      return false;
    }
    @Override
    public boolean getMetaKey() {
      return false;
    }
    @Override
    public nsIDOMEventTarget getRelatedTarget() {
      return null;
    }
    @Override
    public int getScreenX() {
      return 0;
    }
    @Override
    public int getScreenY() {
      return 0;
    }
    @Override
    public boolean getShiftKey() {
      return false;
    }
    @Override
    public void initMouseEvent(String arg0, boolean arg1, boolean arg2,
        nsIDOMAbstractView arg3, int arg4, int arg5, int arg6, int arg7,
        int arg8, boolean arg9, boolean arg10, boolean arg11, boolean arg12,
        int arg13, nsIDOMEventTarget arg14) {
    }
  }
  /**
   * Returns node attribute attributeName value or null when node doesn't have attribute with attributeName
   * @param node
   * @param attributeName
   * @return
   */
  public static String getNodeAttribute (nsIDOMNode node , String attributeName){
    String result = null;
    nsIDOMNamedNodeMap attrs = node.getAttributes();
    int index = 0;
    while (result == null && index < attrs.getLength()){
      nsIDOMNode modelAttr = attrs.item(index);
      if (modelAttr.getNodeName().equals(attributeName)){
        result = modelAttr.getNodeValue().replaceAll("\n", " ");
      }
      index++;
    }
    return result;
  }
  /**
   * Returns true when current selection contains exactly nodes with values
   * @param containsWholeNodes if true selection has to contain whole nodes
   * @param values
   * @return
   */
  public boolean selectionContainsNodes(boolean containsWholeNodes,
      String... values) {
    boolean result = true;

    List<nsIDOMNode> selectedNodes = mozillaEditor.getXulRunnerEditor()
        .getSelectedNodes();
    if (selectedNodes != null && values != null) {
      if (selectedNodes.size() == values.length) {
        int index = 0;
        while (result && index < values.length) {
          boolean selectedNodeContainsValue = false;
          Iterator<nsIDOMNode> itSelectedNodes = selectedNodes.iterator();
          while (!selectedNodeContainsValue && itSelectedNodes.hasNext()) {
            if (containsNodeWithValue(itSelectedNodes.next(), values[index])){
              selectedNodeContainsValue = true;
            }
          }
          if (!selectedNodeContainsValue){
            result = false;
          }
          index++;
        }
      } else {
        result = false;
      }
    } else {
      result = false;
    }
    
    return result;
  }
  
  /**
   * Returns all comment nodes
   * @return
   */
  public List<nsIDOMNode> getCommentNodes(){
    return getCommentNodes(mozillaEditor.getDomDocument());
  }
  /**
   * Returns all comment nodes of node
   * @param node
   * @return
   */
  public List<nsIDOMNode> getCommentNodes(nsIDOMNode node){
    LinkedList<nsIDOMNode> result = new LinkedList<nsIDOMNode>();
    final String commentNodeName = "#comment";
    if (node.getNodeName().trim().equals(commentNodeName)){
      result.add(node);
    }
    else{
      // check children children
      nsIDOMNodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {

        nsIDOMNode child = children.item(i);
        // leave out empty text nodes in test dom model
        if ((child.getNodeType() == Node.TEXT_NODE)
            && ((child.getNodeValue() == null) || (child.getNodeValue().trim()
                .length() == 0)))
          continue;

        result.addAll(getCommentNodes(child));
      }
    }
    return result;
  }
  /**
   * Returns true if browser contains comment node with value
   * @param value
   * @return
   */
  public boolean containsCommentWithValue (String value){
    boolean notFound = true;
    
    List<nsIDOMNode> comments = getCommentNodes();
    if (comments != null && comments.size() > 0){
      Iterator<nsIDOMNode> itNode = comments.iterator();
      while (itNode.hasNext() && notFound){
        nsIDOMNode node = itNode.next();
        if (stripTextFromSpecChars(node.getNodeValue()).equalsIgnoreCase(value)){
          notFound = false;
        }
      }
    }
    
    return !notFound;
  }
}

 /*******************************************************************************
  * Copyright (c) 2007-2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.drools.ui.bot.test.smoke;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * Tests Rule Flow
 * @author Vladimir Pakan
 *
 */
public class RuleFlowTest extends SWTTestExt{
  private static final String RULE_FLOW_FILE_DIRECTORY = "src" + File.separator +
    "main" + File.separator +
    "rules";
  private static final String ROOT_NODE_NAME = "process";
  private static final String HEADER_NODE_NAME = "header";
  private static final String NODES_NODE_NAME = "nodes";
  private static final String CONNECTIONS_NODE_NAME = "connections";
  private static final String CONNECTION_NODE_NAME = "connection";
  private static final int NODES_NODE_CHILDREN_COUNT = 7;
  private static final int CONNECTIONS_NODE_CHILDREN_COUNT = 1;
  private static final int ROOT_NODE_CHILDREN_COUNT = 3;
  
  private boolean isEditorMaximized = false;
  /**
   * Tests Rule Flow
   */
  @Test
  public void testRuleFlow() {
    runRuleFlowCheck(DroolsAllBotTests.RULE_FLOW_JAVA_TEST_FILE_NAME);
    ruleFlowEditorCheck(DroolsAllBotTests.RULE_FLOW_RF_FILE_NAME);
  }
  /**
   * Runs newly created Drools project and check result
   * @param droolsProjectName
   */
  private void runRuleFlowCheck(String droolsRuleTestFileName){
    console.clearConsole();
    bot.sleep(5000L);

    SWTBotTreeItem tiTestFile = packageExplorer.selectTreeItem(droolsRuleTestFileName, 
      new String[] {DroolsAllBotTests.DROOLS_PROJECT_NAME,
        DroolsAllBotTests.SRC_MAIN_JAVA_TREE_NODE,
        DroolsAllBotTests.COM_SAMPLE_TREE_NODE});
    
    eclipse.runTreeItemAsJavaApplication(tiTestFile);
    
    String consoleText = console.getConsoleText(3*1000L,60*1000L,true);
      
    assertTrue(droolsRuleTestFileName + " didn't run properly.\n" +
      "Console Text was: " + consoleText + "\n" +
      "Expected console text is: " + "Hello World\n",
      "Hello World\n".equals(consoleText));
  }
  /**
   * Add all possible object to RF diagram and then remove them
   * @param ruleFlowRfFileName
   */
  private void ruleFlowEditorCheck (String ruleFlowFileName){
    packageExplorer.show();
    packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME ,
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE,
      DroolsAllBotTests.RULE_FLOW_RF_FILE_NAME);
    // Test if Rule Flow RF File is opened in editor
    assertTrue("Rule Flow RF File is not opened properly. File " + ruleFlowFileName + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot,ruleFlowFileName));
    // Maximize editor
    bot.menu(IDELabel.Menu.WINDOW)
      .menu(IDELabel.Menu.NAVIGATION)
      .menu(IDELabel.Menu.MAXIMIZE_ACTIVE_VIEW_OR_EDITOR)
      .click();
    isEditorMaximized = true;
    SWTGefBot gefBot = new SWTGefBot();
    SWTBotGefEditor gefEditor = gefBot.gefEditor(ruleFlowFileName);
    final Control editorControl = (Control)gefEditor.getWidget();
    Rectangle editorBounds = UIThreadRunnable.syncExec(new Result<Rectangle>() {
      public Rectangle run() {
        return editorControl.getBounds();
      }
    });
    // Clear Editor
    gefEditor.activateTool("Marquee");
    gefEditor.drag(0,0,editorBounds.width - editorBounds.x, editorBounds.height - editorBounds.y);
    gefEditor.setFocus();
    bot.sleep(Timing.time1S());
    KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
    // Draw each component
    String[] tools = new String[]{"Start Event","End Event","Rule Task",
      "Gateway [diverge]","Gateway [converge]","Reusable Sub-Process",
      "Script Task"
      };
    int xspacing = 100;
    int xoffset = 10;
    int yspacing = 100;
    int yoffset = 10;
    for (int toolIndex = 0;toolIndex < tools.length;toolIndex++){
      gefEditor.activateTool(tools[toolIndex]);
      gefEditor.click(xspacing * (toolIndex % 3) + xoffset, 
        yspacing * (toolIndex / 3) + yoffset);
    }
    // Add Sequence Flow between Start and End Node
    gefEditor.activateTool("Sequence Flow");
    // Click on Start Node
    gefEditor.click(xoffset + 5, yoffset + 5);
    // Click on End Node
    gefEditor.click(xspacing + xoffset + 5, yoffset + 5);
    gefEditor.save();
    checkFullRFFile(DroolsAllBotTests.DROOLS_PROJECT_NAME , ruleFlowFileName);
    // check synchronization with Properties View
    gefEditor.activateTool("Select");
    gefEditor.click(xoffset + 5, yoffset + 5);
    SWTBotTree tree = eclipse.showView(ViewType.PROPERTIES).tree();
    String id = tree.getTreeItem("Id").cell(1);
    String name = tree.getTreeItem("Name").cell(1);
    assertTrue("First editor element has to have Id=1 and Name=Start." +
      "\nBut it has Id=" + id +
      " Name=" + name, id.equals("1") && name.equals("Start"));
    // Delete each component
    gefEditor.activateTool("Select");
    for (int toolIndex = 0;toolIndex < tools.length;toolIndex++){
      gefEditor.click(xspacing * (toolIndex % 3) + xoffset + 10, 
        yspacing * (toolIndex / 3) + yoffset + 10);
      gefEditor.setFocus();
      bot.sleep(Timing.time1S());
      KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
    }
    // Restore maximized editor
    bot.menu(IDELabel.Menu.WINDOW)
      .menu(IDELabel.Menu.NAVIGATION)
      .menu(IDELabel.Menu.MAXIMIZE_ACTIVE_VIEW_OR_EDITOR)
      .click();
    isEditorMaximized = false;
    gefEditor.save();
    gefEditor.close();
    checkEmptyRFFile(DroolsAllBotTests.DROOLS_PROJECT_NAME , ruleFlowFileName);
  }  
  /**
   * Check content of Rule Flow file containing all possible objects
   * 
   * @param projectName
   * @param ruleFlowFileName
   */
  private void checkFullRFFile(String projectName, String ruleFlowFileName) {

    Document doc = loadXmlFile(SWTUtilExt.getPathToProject(projectName)
        + File.separator + RuleFlowTest.RULE_FLOW_FILE_DIRECTORY
        + File.separator + ruleFlowFileName);

    String errorDescription = null;
    Element rootNode = doc.getDocumentElement();
    doc.normalizeDocument();
    if (rootNode.getNodeName().equals(ROOT_NODE_NAME)) {
      NodeList rootNodeList = rootNode.getChildNodes();
      List<Node> rootNodes = removeTextNodes(rootNodeList);
      if (rootNodes.size() == ROOT_NODE_CHILDREN_COUNT) {
        Node header = rootNodes.get(0);
        errorDescription = checkEmptyFileNode(header, HEADER_NODE_NAME);
        if (errorDescription == null) {
          Node nodesNode = rootNodes.get(1);
          errorDescription = checkNodeName(nodesNode, NODES_NODE_NAME);
          if (errorDescription == null) {
            errorDescription = checkNodesFileNodes(removeTextNodes(nodesNode
                .getChildNodes()));
            if (errorDescription == null) {
              Node connectionsNode = rootNodes.get(2);
              errorDescription = checkNodeName(connectionsNode,
                  CONNECTIONS_NODE_NAME);
              if (errorDescription == null) {
                errorDescription = checkConnectionsFileNodes(removeTextNodes(connectionsNode
                    .getChildNodes()));
              }
            }
          }
        }
      } else {
        errorDescription = "Root node has to have " + ROOT_NODE_CHILDREN_COUNT
            + " child nodes but it has " + rootNodeList.getLength();
      }
    } else {
      errorDescription = "Root node has to have name '" + ROOT_NODE_NAME
          + "' but it has name '" + rootNode.getNodeName() + "'";
    }
    assertNull(errorDescription,errorDescription);
  }
  /**
   * Check content of empty Rule Flow file
   * @param projectName
   * @param ruleFlowRfFileName
   */
  private void checkEmptyRFFile(String projectName, String ruleFlowFileName){
    
    Document doc = loadXmlFile(SWTUtilExt.getPathToProject(projectName) +
      File.separator + RuleFlowTest.RULE_FLOW_FILE_DIRECTORY + File.separator + 
      ruleFlowFileName);

    String errorDescription = null;

    Element rootNode = doc.getDocumentElement();
    doc.normalizeDocument();
    if (rootNode.getNodeName().equals(ROOT_NODE_NAME)) {
      NodeList rootNodeList = rootNode.getChildNodes();
      List<Node> nodes = removeTextNodes(rootNodeList);
      if (nodes.size() == ROOT_NODE_CHILDREN_COUNT) {
        Node header = nodes.get(0);
        errorDescription = checkEmptyFileNode(header, HEADER_NODE_NAME);
        if (errorDescription == null) {
          Node nodesNode = nodes.get(1);
          errorDescription = checkEmptyFileNode(nodesNode, NODES_NODE_NAME);
          if (errorDescription == null) {
            Node connnections = nodes.get(2);
            errorDescription = checkEmptyFileNode(connnections, CONNECTIONS_NODE_NAME);
          }
        }
      } else {
        errorDescription = "Root node has to have " +
        ROOT_NODE_CHILDREN_COUNT +
        " child nodes but it has " + 
        rootNodeList.getLength();
      }
    } else {
      errorDescription = "Root node has to have name '" + ROOT_NODE_NAME
        + "' but it has name '" + rootNode.getNodeName() + "'";
    }
    assertNull(errorDescription,errorDescription);
 
  }
  /**
   * Loads and parse XML file with fileName from file system 
   * @param fileName - full path to XML file
   * @return
   */
  private Document loadXmlFile (String fileName){
    File file = new File(fileName);
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db;
    Document doc = null;
    try {
      db = dbf.newDocumentBuilder();
      doc = db.parse(file);
    } catch (ParserConfigurationException pce) {
        throw new RuntimeException(pce);
    } catch (SAXException saxe) {
      throw new RuntimeException(saxe);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    return doc;
  }
  private static List<Node> removeTextNodes(NodeList rootNodeList){
    LinkedList<Node> nodes = new LinkedList<Node>();
    for (int index = 0 ; index < rootNodeList.getLength(); index++){
      Node node = rootNodeList.item(index);
      if (!node.getNodeName().equals("#text")){
        nodes.add(node);
      }  
    }
    return nodes;
  }
  /**
   * Check if node is correct for empty file
   * @param node
   * @param expectedNodeName
   * @return
   */
  private static String checkEmptyFileNode (Node node,String expectedNodeName){
    String errorDescription = null;
    if (node.getNodeName().equals(expectedNodeName)){
      if (removeTextNodes(node.getChildNodes()).size() == 0){
        if(node.getTextContent().trim().length() != 0){
          errorDescription = expectedNodeName + " node has to have no text value but it has " +
            node.getTextContent();
        }
      }
      else{
        errorDescription = expectedNodeName + " node has to have no children but is has " +
        removeTextNodes(node.getChildNodes()).size();
        
      }
    }  
    else{
      errorDescription = checkNodeName(node, expectedNodeName);
    }
    
    return errorDescription;
    
  }
  /**
   * Check nodes of Nodes children.
   * @param nodes - list of nodes of nodes node stripped from text nodes
   * @return
   */
  private static String checkNodesFileNodes(List<Node> nodes){
    String errorDescription = null;
    
    if (nodes.size() == NODES_NODE_CHILDREN_COUNT){
      List<String> mandatoryNodes = getMandatoryNodesOfNodesNode(); 
      int index = 0;
      Iterator<Node> iterator = nodes.iterator();
      while (index < nodes.size() && errorDescription == null){
        String nodeName = iterator.next().getNodeName();
        if (mandatoryNodes.contains(nodeName)){
          mandatoryNodes.remove(nodeName);
        }
        else{
          errorDescription = "Nodes node cannot contain node " + nodeName;
        }
        index++;
      }
      if (errorDescription == null && mandatoryNodes.size() > 0) {
        StringBuilder sb = new StringBuilder("");
        for (String nodeName : mandatoryNodes){
          if (sb.length() != 0){
            sb.append(", ");
          }
          sb.append(nodeName);
        }
        errorDescription = "Nodes node doesn't contain all necesarry nodes.\n" +
          "These node(s) are missing within nodes node: " +
          sb.toString();
      }
    }
    else{
      errorDescription = "Nodes node has to have " +
        NODES_NODE_CHILDREN_COUNT +
        " child nodes but it has " +
        nodes.size();
    }
    
    return errorDescription;
  }
  /**
   * Returns list of mandatory nodes of nodes node
   * @return
   */
  private static List<String> getMandatoryNodesOfNodesNode(){
    LinkedList<String> allowedNodes = new LinkedList<String>();
    allowedNodes.add("split");
    /*
    allowedNodes.add("timerNode");
    allowedNodes.add("humanTask");
    */
    allowedNodes.add("ruleSet");
    allowedNodes.add("actionNode");
    /*
    allowedNodes.add("composite");
    */
    allowedNodes.add("end");
    /*
    allowedNodes.add("workItem");
    allowedNodes.add("fault");
    */
    allowedNodes.add("subProcess");
    allowedNodes.add("start");
    /*
    allowedNodes.add("workItem");
    allowedNodes.add("eventNode");
    */
    allowedNodes.add("join");
    
    return allowedNodes;
  }

  /**
   * Check nodes of connections children.
   * @param nodes - list of nodes of connections node stripped from text nodes
   * @return
   */
  private static String checkConnectionsFileNodes(List<Node> nodes){
    String errorDescription = null;
    
    if (nodes.size() == CONNECTIONS_NODE_CHILDREN_COUNT){
      Node connectioNode = nodes.get(0);
      errorDescription = checkEmptyFileNode(connectioNode, CONNECTION_NODE_NAME);
    }
    else{
      errorDescription = "Conections node has to have " +
        CONNECTIONS_NODE_CHILDREN_COUNT +
        " child nodes but it has " +
        nodes.size();
    }
    
    return errorDescription;
  }
  /**
   * Check if node has expected name
   * @param node
   * @param expectedNodeName
   * @return
   */
  private static String checkNodeName (Node node, String expectedNodeName){
    String errorDescription = null;
    
    if (!node.getNodeName().equals(expectedNodeName)){
      errorDescription = "Node has to have name '" +
      expectedNodeName + "' but it has name '" +
        node.getNodeName() + "'";
    }
    
    return errorDescription;
  }
  
  protected void tearDown(){
    if (isEditorMaximized){
      // Restore maximized editor
      bot.menu(IDELabel.Menu.WINDOW)
        .menu(IDELabel.Menu.NAVIGATION)
        .menu(IDELabel.Menu.MAXIMIZE_ACTIVE_VIEW_OR_EDITOR)
        .click();
      isEditorMaximized = false;
    }
    try {
      super.tearDown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
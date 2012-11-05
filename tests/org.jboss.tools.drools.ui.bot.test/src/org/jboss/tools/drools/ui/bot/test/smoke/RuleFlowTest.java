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

//import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.drools.ui.bot.test.DroolsAllBotTests;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
        configureJbpmProject();
        runRuleFlowCheck(DroolsAllBotTests.RULE_FLOW_JAVA_TEST_FILE_NAME);
        ruleFlowEditorCheck(DroolsAllBotTests.RULE_FLOW_FILE_NAME);
    }

    /**
     * Sets jBPM library 
     */
    private void configureJbpmProject() {
        SWTBotTree tree = eclipse.showView(ViewType.PACKAGE_EXPLORER).tree();
        tree.getTreeItem(DroolsAllBotTests.DROOLS_PROJECT_NAME).select();
        ContextMenuHelper.clickContextMenu(tree, IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE,
                IDELabel.Menu.CONVERT_TO_JBPM_PROJECT);
    }

    /**
     * Sets all drools flow nodes.
     */
    @SuppressWarnings("unused")
    private void setAllDroolsFlowNodes() {
        new SWTOpenExt(bot).preferenceOpen(ActionItem.Preference.DroolsDroolsFlownodes.LABEL);
        bot.waitForShell(IDELabel.Shell.PREFERENCES);
        for (SWTBotCheckBox checkBox : bot.checkBoxes()) {
            if (checkBox.isEnabled()) {
                checkBox.select();
            }
        }
        bot.button(IDELabel.Button.OK).click();
        bot.waitForShell(IDELabel.Shell.WARNING);
        bot.button(IDELabel.Button.OK).click();
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
  private void ruleFlowEditorCheck(String ruleFlowFileName) {
    packageExplorer.show();
    SWTUtilExt.startCapturingStandardOutput();
    packageExplorer.openFile(DroolsAllBotTests.DROOLS_PROJECT_NAME,
      DroolsAllBotTests.SRC_MAIN_RULES_TREE_NODE, ruleFlowFileName);
    final String capturedOutput = SWTUtilExt.stopCapturingStandardOutput();
    System.out.print(capturedOutput);
    // Test if Rule Flow File is opened in editor
    assertTrue("Rule Flow File is not opened properly. File " + ruleFlowFileName + " is not opened in editor",
      SWTEclipseExt.existEditorWithLabel(bot, ruleFlowFileName));
    // Maximize editor
    bot.menu(IDELabel.Menu.WINDOW)
      .menu(IDELabel.Menu.NAVIGATION)
      .menu(IDELabel.Menu.MAXIMIZE_ACTIVE_VIEW_OR_EDITOR)
      .click();
    isEditorMaximized = true;
    SWTGefBot gefBot = new SWTGefBot();
    SWTBotGefEditor gefEditor = gefBot.gefEditor(ruleFlowFileName);
    // Clear Editor
    gefEditor.setFocus();
    deleteAllObjectsFromRuleFile(gefEditor, DroolsAllBotTests.DROOLS_PROJECT_NAME, ruleFlowFileName);
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
    checkFullRuleFile(DroolsAllBotTests.DROOLS_PROJECT_NAME , ruleFlowFileName);
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
    bot.sleep(Timing.time1S());
    for (int toolIndex = 0;toolIndex < tools.length;toolIndex++){
      gefEditor.click(xspacing * (toolIndex % 3) + xoffset + 10, 
        yspacing * (toolIndex / 3) + yoffset + 10);
      gefEditor.setFocus();
      //bot.sleep(Timing.time1S());
      //KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
      bot.menu(IDELabel.Menu.EDIT).menu(IDELabel.Menu.DELETE).click();
    }
    // Restore maximized editor
    bot.menu(IDELabel.Menu.WINDOW)
      .menu(IDELabel.Menu.NAVIGATION)
      .menu(IDELabel.Menu.MAXIMIZE_ACTIVE_VIEW_OR_EDITOR)
      .click();
    isEditorMaximized = false;
    gefEditor.save();
    gefEditor.close();
    checkEmptyRuleFile(DroolsAllBotTests.DROOLS_PROJECT_NAME , ruleFlowFileName);
    assertFalse("Opening BPMN process throws IndexOutOfBoundsException exception."
            + " Reported bug: https://issues.jboss.org/browse/JBIDE-11984",
            capturedOutput.contains("IndexOutOfBoundsException"));
  }

    /**
     * Converts to full path of rule flow file.
     * 
     * @param projectName Project name
     * @param ruleFlowFileName File name of rule flow
     * @return Full path to rule flow file
     */
    private String getFullPathToRuleFlowFile(final String projectName, final String ruleFlowFileName) {
        return SWTUtilExt.getPathToProject(projectName) + File.separator
                + RuleFlowTest.RULE_FLOW_FILE_DIRECTORY + File.separator + ruleFlowFileName;  
    }

    /**
     * Return normalized document from file with given name.
     * 
     * @param fileName File name to get it normalized document from.
     * @return Normalized document
     */
    private Document getNormalizedDocument(final String fileName) {
        Document document = loadXmlFile(fileName);
        document.normalizeDocument();
        return document;
    }

    /**
     * Decides according to used Drools version.
     * 
     * @param projectName
     * @param ruleFlowFileName
     */
    private void checkFullRuleFile(final String projectName, final String ruleFlowFileName) {
        if (DroolsAllBotTests.RULE_FLOW_SAMPLE_FILE_NAME.equals(ruleFlowFileName)) {
            checkFullBpmnFile(projectName, ruleFlowFileName);
        } else {
            checkFullRFFile(projectName, ruleFlowFileName);
        }
    }

    /**
     * Checks full BPMN file.
     * 
     * @param projectName
     * @param ruleFlowFileName
     */
    private void checkFullBpmnFile(final String projectName, final String ruleFlowFileName) {
        final String START_EVENT_NODE_NAME = "startEvent";
        final String END_EVENT_NODE_NAME = "endEvent";
        final String BUSINESS_RULE_TASK = "businessRuleTask";
        final String COMPLEX_GATEWAY_NODE_NAME = "complexGateway";
        final String CALL_ACTIVITY_NODE_NAME = "callActivity";
        final String SCRIPT_TASK_NODE_NAME = "scriptTask";
        final String SEQUENCE_FLOW_NODE_NAME = "sequenceFlow";
        final String GATEWAY_DIRECTION = "gatewayDirection";
        final String DIVERGING = "Diverging";
        final String CONVERGING = "Converging";
        final String SOURCE_REF = "sourceRef";
        final String TARGET_REF = "targetRef";
        final String ID = "id";

        final String fullRuleFlowFileName = getFullPathToRuleFlowFile(projectName, ruleFlowFileName);

        assertTrue("'" + fullRuleFlowFileName + "' is not valid BPMN 2 XML file.", isValidBpmnXml(fullRuleFlowFileName));

        Document document = getNormalizedDocument(fullRuleFlowFileName);

        final String START_EVENT_ID;
        final String END_EVENT_ID;

        assertEquals("There should be just one '" + START_EVENT_NODE_NAME + "' element in XML.",
                1, document.getElementsByTagName(START_EVENT_NODE_NAME).getLength());
        START_EVENT_ID = document.getElementsByTagName(START_EVENT_NODE_NAME).item(0).getAttributes()
                .getNamedItem(ID).getTextContent();

        assertEquals("There should be just one '" + END_EVENT_NODE_NAME + "' element in XML.",
                1, document.getElementsByTagName(END_EVENT_NODE_NAME).getLength());
        END_EVENT_ID = document.getElementsByTagName(END_EVENT_NODE_NAME).item(0).getAttributes()
                .getNamedItem(ID).getTextContent();

        assertEquals("There should be just one '" + BUSINESS_RULE_TASK + "' element in XML.",
                1, document.getElementsByTagName(BUSINESS_RULE_TASK).getLength());

        NodeList gatewayNodes = document.getElementsByTagName(COMPLEX_GATEWAY_NODE_NAME);
        assertEquals("There should be exactly two '" + COMPLEX_GATEWAY_NODE_NAME + "' elements in XML.",
                2, gatewayNodes.getLength());
        int diverging = 0;
        int converging = 0;
        for (int i = 0; i < gatewayNodes.getLength(); i++) {
            NamedNodeMap attributes = gatewayNodes.item(i).getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                if (GATEWAY_DIRECTION.equals(attribute.getNodeName())) {
                    if (DIVERGING.equals(attribute.getTextContent())) {
                        diverging++;
                    } else if (CONVERGING.equals(attribute.getTextContent())) {
                        converging++;
                    }
                }
            }
        }
        assertEquals("There should be one diverging and one converting gateway, but it wasn't so.",
                1, diverging * converging);

        assertEquals("There should be just one '" + CALL_ACTIVITY_NODE_NAME + "' element in XML.",
                1, document.getElementsByTagName(CALL_ACTIVITY_NODE_NAME).getLength());

        assertEquals("There should be just one '" + SCRIPT_TASK_NODE_NAME + "' element in XML.",
                1, document.getElementsByTagName(SCRIPT_TASK_NODE_NAME).getLength());

        assertEquals("There should be just one '" + SEQUENCE_FLOW_NODE_NAME + "' element in XML.",
                1, document.getElementsByTagName(SEQUENCE_FLOW_NODE_NAME).getLength());

        NamedNodeMap attributes = document.getElementsByTagName(SEQUENCE_FLOW_NODE_NAME).item(0).getAttributes();
        final String sourceRef = attributes.getNamedItem(SOURCE_REF).getTextContent();
        assertEquals("Source of sequence flow should be '" + START_EVENT_ID + "' but was '"
                + sourceRef + "'.", START_EVENT_ID, sourceRef);
        final String targetRef = attributes.getNamedItem(TARGET_REF).getTextContent();
        assertEquals("Target of sequence flow should be '" + END_EVENT_ID + "' but was '"
                + targetRef, END_EVENT_ID, targetRef);
    }

  /**
   * Check content of Rule Flow file containing all possible objects
   * 
   * @param projectName
   * @param ruleFlowFileName
   */
  private void checkFullRFFile(final String projectName, final String ruleFlowFileName) {
    String errorDescription = null;
    Document doc = getNormalizedDocument(getFullPathToRuleFlowFile(projectName, ruleFlowFileName));
    Element rootNode = doc.getDocumentElement();
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
     * Decides according to used Drools version.
     * 
     * @param projectName
     * @param ruleFlowFileName
     */
    private void checkEmptyRuleFile(final String projectName, final String ruleFlowFileName) {
        if (DroolsAllBotTests.RULE_FLOW_SAMPLE_FILE_NAME.equals(ruleFlowFileName)) {
            checkEmptyBpmnFile(projectName, ruleFlowFileName);
        } else {
            checkEmptyRFFile(projectName, ruleFlowFileName);
        }
    }

    /**
     * Checks empty BPMN file with given name.
     * 
     * @param projectName
     * @param ruleFlowFileName
     */
    private void checkEmptyBpmnFile(final String projectName, final String ruleFlowFileName) {
        final String fullRuleFlowFileName = getFullPathToRuleFlowFile(projectName, ruleFlowFileName);
        assertTrue("'" + fullRuleFlowFileName + "' is not a valid BPMN 2 XML file.",
                isValidBpmnXml(fullRuleFlowFileName));

        final String PROCESS_NODE_NAME = "process";
        Document document = getNormalizedDocument(fullRuleFlowFileName);
        NodeList processNode = document.getElementsByTagName(PROCESS_NODE_NAME);
        assertEquals("There should be just one '" + PROCESS_NODE_NAME + "' node.", 1, processNode.getLength());
        NodeList processChildNodes = processNode.item(0).getChildNodes();
        for (int i = 0; i < processChildNodes.getLength(); i++) {
            final String processChildNodeName = processChildNodes.item(i).getNodeName();
            if (!("#text".equals(processChildNodeName) || "#comment".equals(processChildNodeName))) {
                fail("'" + PROCESS_NODE_NAME + "' node should not have any child nodes (except #text and #comment) but it has.");
            }
        }
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

    /**
     * Deletes all objects from rule file.
     * 
     * @param gefEditor
     * @param projectName
     * @param ruleFlowFileName
     */
    private void deleteAllObjectsFromRuleFile(SWTBotGefEditor gefEditor, String projectName, String ruleFlowFileName) {
        if (DroolsAllBotTests.RULE_FLOW_SAMPLE_FILE_NAME.equals(ruleFlowFileName)) {
            deleteAllObjectFromBpmnFile(gefEditor, projectName, ruleFlowFileName);
        } else {
            deleteAllObjectsFromRFFile(gefEditor, projectName, ruleFlowFileName);
        }
    }

  /**
   * Delete all objects from RF File
   * @param gefEditor
   * @param projectName
   * @param ruleFlowFileName
   */
  private void deleteAllObjectsFromRFFile(SWTBotGefEditor gefEditor, String projectName, String ruleFlowFileName) {

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
        Node nodesNode = rootNodes.get(1);
        errorDescription = checkNodeName(nodesNode, NODES_NODE_NAME);
        if (errorDescription == null) {
          List<Node> nodes = removeTextNodes(nodesNode.getChildNodes());
          bot.sleep(Timing.time1S());
          for (Node node : nodes){
            NamedNodeMap attributes = node.getAttributes();
            int xPos = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
            int yPos = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
            gefEditor.click(xPos + 3, yPos + 3);
            //bot.sleep(Timing.time1S());
            bot.menu(IDELabel.Menu.EDIT).menu(IDELabel.Menu.DELETE).click();
            //KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
            //bot.sleep(Timing.time1S());
          }
        }
      } else {
        errorDescription = "'" + NODES_NODE_NAME + "'" +" was not found on expected location within RF file." +
          " RF file structure has been changed";
      }
    } else {
      errorDescription = "Root Node has to have name '" + ROOT_NODE_NAME + "'. RF file structure has been changed.";
    }
    assertNull(errorDescription,errorDescription);
  }

    /**
     * Deletes all object from BPMN file.
     * 
     * @param gefEditor
     * @param projectName
     * @param ruleFlowFileName
     */
    private void deleteAllObjectFromBpmnFile(SWTBotGefEditor gefEditor, String projectName, String ruleFlowFileName) {
        final String fileName = getFullPathToRuleFlowFile(projectName, ruleFlowFileName);
        assertTrue("Rule flow file '" + fileName + "' is not valid.", isValidBpmnXml(fileName));

        Document document = getNormalizedDocument(fileName);
        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node xAttribute = nodeList.item(i).getAttributes().getNamedItem("g:x");
            Node yAttribute = nodeList.item(i).getAttributes().getNamedItem("g:y");
            if (xAttribute != null && yAttribute != null) {
                int x = Integer.parseInt(xAttribute.getNodeValue());
                int y = Integer.parseInt(yAttribute.getNodeValue());
                final int OFFSET = 3;
                gefEditor.click(x + OFFSET, y + OFFSET);
                bot.sleep(Timing.time1S());
                bot.menu(IDELabel.Menu.EDIT).menu(IDELabel.Menu.DELETE).click();
                //KeyboardHelper.typeKeyCodeUsingAWT(KeyEvent.VK_DELETE);
                //bot.sleep(Timing.time1S());
            }
        }
    }

    /**
     * Validates file with given name if it is valid BPMN 2 XML.
     * 
     * @param bpmnXmlFileName Name of the file to be validated.
     * @return <code>true</code> if given document is valid BPMN 2 XML file,
     *         <code>false</code> otherwise.
     */
    private static boolean isValidBpmnXml(final String bpmnXmlFileName) {
        final String XML_SCHEMA_FILE = "resources/XMLSchemas/BPMN20.xsd";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder parser;
        Document document;
        try {
            parser = dbf.newDocumentBuilder();
            document = parser.parse(new File(bpmnXmlFileName)); 
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return false;
        } catch (SAXException saxe) {
            saxe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(new File(XML_SCHEMA_FILE));
        Schema schema;
        try {
            schema = schemaFactory.newSchema(schemaFile);
        } catch (SAXException saxe) {
            saxe.printStackTrace();
            return false;
        }

        Validator validator = schema.newValidator();
        try {
            validator.validate(new DOMSource(document));
        } catch (SAXException saxe) {
            // instance document is invalid!
            log.error("ERROR: Document '" + bpmnXmlFileName + "' is invalid (" + saxe.getMessage() + ")");
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }

        return true;
    }
}
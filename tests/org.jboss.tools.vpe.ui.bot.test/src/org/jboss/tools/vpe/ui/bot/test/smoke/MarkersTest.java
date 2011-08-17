/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.smoke;

import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.MarkerHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
/**
 * Test Markers position and attributes regarding to VPE components within jsp page
 * @author Vladimir Pakan
 *
 */
public class MarkersTest extends VPEEditorTestCase{
  private SWTBotEditorExt editor;
  private String originalEditorText;
  /**
   * Test open on functionality of JSF components within jsp page
   */
	public void testMarkers(){
	  MarkerHelper markerHelper = new MarkerHelper(TEST_PAGE,
	      VPEAutoTestCase.JBT_TEST_PROJECT_NAME,"WebContent","pages");
	  String textToSelect = "<%@ taglib";
	  String insertText  = "yyaddedxx";
	  int[] expectedMarkerLines = new int[4];
	  String[] expectedMarkerDesc = new String[4];
    SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot, 
        TEST_PAGE,
        textToSelect, 
        textToSelect.length(), 
        0, 
        0);
    editor.insertText(insertText);
    expectedMarkerLines[0] = editor.cursorPosition().line;
    expectedMarkerDesc[0] = "^Unknown tag \\(jsp:directive\\.taglib" + insertText + "\\).*";
    textToSelect = "<%@ taglib uri=\"http://java.sun.com/jsf/html";
    SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot,
        TEST_PAGE,
        textToSelect, 
        textToSelect.length(), 
        0, 
        0);
    editor.insertText(insertText);
    expectedMarkerLines[1] = editor.cursorPosition().line;
    expectedMarkerDesc[1] = "^Can not find the tag library descriptor for \"http://java.sun.com/jsf/html" +
      insertText +".*";
    textToSelect = "<h1";
    SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot, 
        TEST_PAGE,
        textToSelect, 
        textToSelect.length(), 
        0, 
        0);
    editor.insertText(insertText);
    expectedMarkerLines[2] = editor.cursorPosition().line;
    expectedMarkerDesc[2] = "^Unknown tag \\(h1" + insertText + "\\).*";
    textToSelect = "</h:inputText";
    SWTJBTExt.selectTextInSourcePane(SWTTestExt.bot, 
        TEST_PAGE,
        textToSelect, 
        textToSelect.length(), 
        0, 
        0);
    editor.insertText(insertText);
    expectedMarkerLines[3] = editor.cursorPosition().line;
    expectedMarkerDesc[3] = "^Missing start tag for \"h:inputText" + insertText + ".*";
    editor.save();
    bot.sleep(Timing.time2S());
    // Check markers
	  for (int index = 0 ; index < expectedMarkerLines.length ; index++){
	    markerHelper.checkForMarker(String.valueOf(expectedMarkerLines[index] + 1),
	        expectedMarkerDesc[index]);  
	  }	  
	}   
  @Override
  public void setUp() throws Exception {
    super.setUp();
    openPage(TEST_PAGE);
    editor = SWTTestExt.bot.swtBotEditorExtByTitle(TEST_PAGE);
    originalEditorText = editor.getText();
  }
  @Override
  public void tearDown() throws Exception {
    if (editor != null){
      editor.setText(originalEditorText);
      editor.saveAndClose();
    }
    super.tearDown();
  }
}
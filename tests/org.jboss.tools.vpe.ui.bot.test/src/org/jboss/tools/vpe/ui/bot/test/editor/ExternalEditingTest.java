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
import java.io.File;
import java.io.IOException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
/**
 * Tests editing of web page via external editor  
 * @author vlado pakan
 *
 */
public class ExternalEditingTest extends VPEEditorTestCase {
  
  private SWTBotExt botExt = null;
  
  private SWTBotEclipseEditor jspEditor;
  
	public ExternalEditingTest() {
		super();
		botExt = new SWTBotExt();
	}
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    bot.closeAllEditors();
	}
	/**
	 * Checks External Editing of web page when accepting External changes
	 */
	public void testAcceptExternalChanges(){
	  final String acceptExtChangesPageName = "ExternalEditingTestAccept.jsp";
    createJspPage(acceptExtChangesPageName);
    jspEditor = botExt.editorByTitle(acceptExtChangesPageName).toTextEditor();
    final String originalPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<html>\n" +
        "  <body>\n" +
        "  </body>\n" +
        "</html>";
    jspEditor.setText(originalPageContent);
    jspEditor.save();
    // modify web page externally   
    final String changedPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<html>\n" +
        "  <body>\n" +
        "    !@#$%CHANGED_TEXT%$#@!\n" +
        "  </body>\n" +
        "</html>";
    try {
      FileHelper.modifyTextFile(getPageLocation(acceptExtChangesPageName),
          changedPageContent);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    new TypeKeyCodesThread(new int[] {KeyEvent.VK_ENTER})
        .start();
    jspEditor.setFocus();
    String sourceText = jspEditor.getText();
    assertTrue("VPE Source pane has to contain text\n" + changedPageContent +
        "'\nbut it contains\n" +
        sourceText,
      sourceText.equals(changedPageContent));
	}
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  @Override
  public void tearDown() throws Exception {
    jspEditor.close();
    super.tearDown();
  } 
  /**
   * Returns absolute page pageName location
   * @param pageName
   * @return
   */
  private String getPageLocation (String pageName){
    StringBuffer sbPageLocation = new StringBuffer("");
    sbPageLocation.append(FileHelper.getProjectLocation(VPEAutoTestCase.JBT_TEST_PROJECT_NAME, botExt));
    sbPageLocation.append(File.separator);
    sbPageLocation.append("WebContent");
    sbPageLocation.append(File.separator);
    sbPageLocation.append("pages");
    sbPageLocation.append(File.separator);
    sbPageLocation.append(pageName);
    return sbPageLocation.toString();
  }
  /**
   * Checks External Editing of web page when deny External changes
   */
  public void testDenyExternalChanges(){
    final String denyExtChangesPageName = "ExternalEditingTestDeny.jsp";
    createJspPage(denyExtChangesPageName);
    jspEditor = botExt.editorByTitle(denyExtChangesPageName).toTextEditor();
    final String originalPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<html>\n" +
        "  <body>\n" +
        "  </body>\n" +
        "</html>";
    jspEditor.setText(originalPageContent);
    jspEditor.save();
    // modify web page externally   
    final String changedPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
        "<html>\n" +
        "  <body>\n" +
        "    !@#$%CHANGED_TEXT%$#@!\n" +
        "  </body>\n" +
        "</html>";
    try {
      FileHelper.modifyTextFile(getPageLocation(denyExtChangesPageName),
          changedPageContent);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    new TypeKeyCodesThread(new int[] {KeyEvent.VK_TAB,KeyEvent.VK_ENTER})
        .start();
    jspEditor.setFocus();
    String sourceText = jspEditor.getText();
    assertTrue("VPE Source pane has to contain text\n" + originalPageContent +
        "'\nbut it contains\n" +
        sourceText,
      sourceText.equals(originalPageContent));
  }
  /**
   * Thread closing dialog displayed when page is modified externally 
   */
  class TypeKeyCodesThread extends Thread {
    private int[] keyCodes;
    public TypeKeyCodesThread (int[] keyCodes){
      super();
      this.keyCodes = keyCodes;
    }
    public void run() {
      try {
        System.out.println("**-- start thread");
        sleep(Timing.time5S());
        for (int keyCode : keyCodes){
          System.out.println("**--Type: " + keyCode);
          KeyboardHelper.typeKeyCodeUsingAWT(keyCode);
          sleep(Timing.time2S());
        }
      } catch (InterruptedException e) {
      }
    }
  }
}

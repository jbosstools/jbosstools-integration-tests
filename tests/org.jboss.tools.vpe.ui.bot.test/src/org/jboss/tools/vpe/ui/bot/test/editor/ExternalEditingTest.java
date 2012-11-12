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
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.EclipseEditorText;
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
	public void testExternalChanges(){
	  final String extChangesPageName = "ExternalEditingTest.jsp";
    createJspPage(extChangesPageName);
    jspEditor = botExt.editorByTitle(extChangesPageName).toTextEditor();
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
      FileHelper.modifyTextFile(getPageLocation(extChangesPageName),
          changedPageContent);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    jspEditor.setFocus();
    boolean isOk = false;
    try{
      bot.waitUntil(new EclipseEditorText(jspEditor, changedPageContent, true), Timing.time5S());
      isOk = true;
    } catch (TimeoutException te){
      // do nothing      
    }
    jspEditor.save();
    String sourceText = jspEditor.getText();
    assertTrue("VPE Source pane has to contain text\n" + changedPageContent +
        "'\nbut it contains\n" +
        sourceText,
      isOk);
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
}

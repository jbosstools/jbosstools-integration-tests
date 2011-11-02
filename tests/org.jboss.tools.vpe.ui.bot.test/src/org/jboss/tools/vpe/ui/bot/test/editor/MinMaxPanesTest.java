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

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.awt.event.KeyEvent;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.editor.xpl.CustomSashForm;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests Minimize and maximize panes functionality  
 * @author vlado pakan
 *
 */
public class MinMaxPanesTest extends VPEEditorTestCase {
  
  private SWTBotExt botExt = null;
  
  private static final String TEST_TEXT = "test text";
  
  private static final String PAGE_TEXT = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n" +
    "<html>\n" +
    "  <body>\n" +
    "  <h:inputText/>" +
    "  </body>\n" +
    "</html>";

  private static final String TEST_PAGE_NAME = "MinMaxPanesTest.jsp";
  
  private SWTBotEclipseEditor jspTextEditor;
  private SWTBotWebBrowser webBrowser;
  
	public MinMaxPanesTest() {
		super();
		botExt = new SWTBotExt();
	}
	
	public void testMinMaxPanes(){
	  botExt.swtBotEditorExtByTitle(MinMaxPanesTest.TEST_PAGE_NAME)
	    .selectPage(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);
	  final CustomSashForm csf = botExt.widgets(widgetOfType(CustomSashForm.class)).get(0);
	  // Maximize Visual Pane
	  int sashWeight = UIThreadRunnable.syncExec(new Result<Integer>() {
      @Override
      public Integer run() {
        csf.maxUp();
        return csf.getWeights()[0];
      }
    });
    assertTrue("Height of Source Pane has to be 0 but is " + sashWeight, 0 == sashWeight);
	  // Maximize Source Pane
	  sashWeight = UIThreadRunnable.syncExec(new Result<Integer>() {
      @Override
      public Integer run() {
        csf.maxDown();
        return csf.getWeights()[1];
      }
    });
	  assertTrue("Height of Visual Pane has to be 0 but is " + sashWeight, 0 == sashWeight);
	  // Make changes to Page Source and Restore Sash position check for changes reflected in Visual Pane
	  jspTextEditor.insertText(4, 0, "    <h:outputText value=\""
        + MinMaxPanesTest.TEST_TEXT + "\" />\n");
    jspTextEditor.save();
    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        csf.upClicked();
      }
    });
    bot.sleep(Timing.time2S());
    assertVisualEditorContains(webBrowser, "INPUT", null, null, MinMaxPanesTest.TEST_PAGE_NAME);
    jspTextEditor.setFocus();
    jspTextEditor.selectRange(4,25,0);
    webBrowser.setFocus();
    KeyboardHelper.typeKeyCodeUsingAWTRepeately(KeyEvent.VK_RIGHT, 1);
    // Make changes to Visual Page and Restore Sash position check for changes reflected in Source Pane
    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        csf.maxUp();
      }
    });
    String insertTestString = "xxinsertedyy";
    KeyboardHelper.typeBasicStringUsingAWT(insertTestString);
    jspTextEditor.save();
    UIThreadRunnable.syncExec(new VoidResult() {
      @Override
      public void run() {
        csf.downClicked();
      }
    });
    bot.sleep(Timing.time2S());
    assertSourceEditorContains(jspTextEditor.getText(), insertTestString, MinMaxPanesTest.TEST_PAGE_NAME);
	}
	
	@Override
	public void setUp() throws Exception {
	  super.setUp();
    eclipse.maximizeActiveShell();
    createJspPage(MinMaxPanesTest.TEST_PAGE_NAME);
    jspTextEditor = botExt.editorByTitle(MinMaxPanesTest.TEST_PAGE_NAME).toTextEditor();
    jspTextEditor.setText(MinMaxPanesTest.PAGE_TEXT);
    jspTextEditor.save();
    webBrowser = new SWTBotWebBrowser(MinMaxPanesTest.TEST_PAGE_NAME,botExt);	  
	}
  @Override
  public void tearDown() throws Exception {
    jspTextEditor.close();
    super.tearDown();
  }
  
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  
}

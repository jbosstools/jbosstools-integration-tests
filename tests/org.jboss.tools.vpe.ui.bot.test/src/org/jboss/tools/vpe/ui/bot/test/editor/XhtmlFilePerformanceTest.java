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


import java.io.File;
import java.io.IOException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests large XHTML file editing 
 * @author vlado pakan
 *
 */
public class XhtmlFilePerformanceTest extends VPEAutoTestCase {
  
  private static final String TEST_PAGE_NAME = "employee.xhtml";
  private SWTBotExt swtBotExt = null;
  
	public XhtmlFilePerformanceTest() {
		super();
    swtBotExt = new SWTBotExt();
	}

	public void testXhtmlFilePerformance(){
	  // copy big XHTML page from resources folder
	  try{
	    String resourceWebContentLocation = getPathToResources("WebContent");	    
	    FileHelper.copyFilesBinaryRecursively(new File(resourceWebContentLocation),
	      new File(FileHelper.getProjectLocation(JBT_TEST_PROJECT_NAME, bot),IDELabel.JsfProjectTree.WEB_CONTENT),
	      null);
	  }catch (IOException ioe){
	    throw new RuntimeException("Unable to copy necessary files from plugin's resources directory",ioe);
	  }
	  bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.REFRESH).click();
	  bot.sleep(Timing.time1S());
	  eclipse.maximizeActiveShell();
	  // open main page
	  packageExplorer.openFile(JBT_TEST_PROJECT_NAME,IDELabel.JsfProjectTree.WEB_CONTENT,XhtmlFilePerformanceTest.TEST_PAGE_NAME);
	  final SWTBotEclipseEditor xhtmlTextEditor = bot.editorByTitle(XhtmlFilePerformanceTest.TEST_PAGE_NAME).toTextEditor();
	  String insertText = "!!!123 Test Title Inserted 321!!!";
	  String origText = xhtmlTextEditor.getText();
	  xhtmlTextEditor.insertText(9, 5, "<h1>" + insertText + "<h1/>");
	  xhtmlTextEditor.save();
	  bot.sleep(Timing.time2S());
    bot.toolbarButtonWithTooltip(SWTJBTExt.isRunningOnMacOs() ? 
        IDELabel.ToolbarButton.REFRESH_MAC_OS: IDELabel.ToolbarButton.REFRESH).click();
    SWTBotEditorExt multiPageEditor = swtBotExt.swtBotEditorExtByTitle(XhtmlFilePerformanceTest.TEST_PAGE_NAME);
    multiPageEditor.selectPage(IDELabel.VisualPageEditor.PREVIEW_TAB_LABEL);
    SWTBotWebBrowser swtBotWebBrowserExt = new SWTBotWebBrowser(XhtmlFilePerformanceTest.TEST_PAGE_NAME, swtBotExt);

    bot.sleep(Timing.time5S());
    try {
      Thread.sleep(Timing.time5S());
    } catch (InterruptedException e) {
      // Ignore
    }
    multiPageEditor.selectPage(IDELabel.VisualPageEditor.VISUAL_SOURCE_TAB_LABEL);
    boolean isOK = swtBotWebBrowserExt.containsNodeWithValue(swtBotWebBrowserExt.getNsIDOMDocument(), insertText);
    xhtmlTextEditor.setText(origText);
    xhtmlTextEditor.save();
    xhtmlTextEditor.close();
    assertTrue("Web Browser has to contain text " + insertText + " but it doesn't.",isOK);
    
    
	}
	
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  
}

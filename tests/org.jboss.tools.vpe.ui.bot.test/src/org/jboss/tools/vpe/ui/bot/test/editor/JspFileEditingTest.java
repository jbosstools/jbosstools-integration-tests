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
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;
/**
 * Tests large XHTML file editing 
 * @author vlado pakan
 *
 */
public class JspFileEditingTest extends VPEEditorTestCase {
  
	public JspFileEditingTest() {
		super();
	}

	public void testJspFileEditing(){
	  try{
	    String resourceWebContentLocation = getPathToResources("WebContent");	    
	    FileHelper.copyFilesBinaryRecursively(new File(resourceWebContentLocation),
	      new File(FileHelper.getProjectLocation(JBT_TEST_PROJECT_NAME, bot),"WebContent"),
	      null);
	  }catch (IOException ioe){
	    throw new RuntimeException("Unable to copy necessary files from plugin's resources directory",ioe);
	  }
	  bot.menu(IDELabel.Menu.FILE).menu(IDELabel.Menu.REFRESH).click();
	  bot.sleep(Timing.time1S());
	  
	  eclipse.maximizeActiveShell();
	  openPage();
	  openPalette();
	  SWTBotWebBrowser swtBotWebBrowser = new SWTBotWebBrowser(TEST_PAGE,bot);
	  
	  nsIDOMNode node = swtBotWebBrowser.getDomNodeByTagName("INPUT",1);
	  
    swtBotWebBrowser.selectDomNode(node,0);
    bot.sleep(Timing.time1S());
    
    swtBotWebBrowser.clickContextMenu(node, SWTBotWebBrowser.INSERT_AFTER_MENU_LABEL,
      SWTBotWebBrowser.JSF_MENU_LABEL,
      SWTBotWebBrowser.HTML_MENU_LABEL,
      SWTBotWebBrowser.H_OUTPUT_TEXT_TAG_MENU_LABEL);
	  
    final SWTBotEclipseEditor jspTextEditor = bot.editorByTitle(TEST_PAGE).toTextEditor();
    jspTextEditor.save();
    // Check if tag h:outputText was properly added
    String editorText = jspTextEditor.getText();
    assertTrue("File " + TEST_PAGE + " has to contain string '<h:outputText/>' but it doesn't",
      editorText.contains("<h:outputText/>"));
	}
	
	@Override
	protected void closeUnuseDialogs() {

	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
  
}

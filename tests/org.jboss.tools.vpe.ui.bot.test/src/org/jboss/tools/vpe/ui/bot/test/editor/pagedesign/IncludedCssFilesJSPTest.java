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
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import java.io.File;
import java.io.IOException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.JobName;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

/**
 * Tests functionality of Included CSS Files tab page of Page Design Options Dialog  with JSP File 
 * @author vlado pakan
 *
 */
public class IncludedCssFilesJSPTest extends PageDesignTestCase {
  
  private static final String CSS_FILE_NAME = "includedCssFileJSPTest.css";
  private static final String JSP_FILE_NAME = "includedCssFileJSPTest.jsp";
  
  private SWTBot addCssReferenceDialogBot = null;
  private SWTBot optionsDialogBot  = null;
    
  public void testIncludedCssFiles() throws IOException{
    SWTBotTree tree = packageExplorer.show().bot().tree();
    tree.expandNode(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
      .expandNode("WebContent")
      .getNode("pages")
      .select();
    // add CSS File
    open.newObject(ActionItem.NewObject.WebCSS.LABEL);
    bot.shell(IDELabel.Shell.NEW_CSS_FILE).activate(); //$NON-NLS-1$
    bot.textWithLabel(IDELabel.NewCSSWizard.FILE_NAME).setText(IncludedCssFilesJSPTest.CSS_FILE_NAME); //$NON-NLS-1$
    bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    SWTBotEditor cssEditor = bot.editorByTitle(IncludedCssFilesJSPTest.CSS_FILE_NAME);
    cssEditor.toTextEditor().setText(".post-info {\n" +
      "  color: blue;\n" +
      "}\n" +
      ".post-info a {\n" +
      "  color: orange;\n" +
      "}");
    cssEditor.saveAndClose();
    // add HTML File
    open.newObject(ActionItem.NewObject.WebJSPFile.LABEL);
    bot.shell(IDELabel.Shell.NEW_JSP_FILE).activate(); //$NON-NLS-1$
    bot.textWithLabel(ActionItem.NewObject.WebJSPFile.TEXT_FILE_NAME).setText(IncludedCssFilesJSPTest.JSP_FILE_NAME); //$NON-NLS-1$
    bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    SWTBotEditor jspEditor = bot.editorByTitle(IncludedCssFilesJSPTest.JSP_FILE_NAME);
    jspEditor.toTextEditor().setText("<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\"%>\n" +
      "  <body>\n" +
      "    <p class=\"post-info\">Posted by\n" +
      "    <a href=\"index.html\">a href</a>\n" +
      "    <br>\n" +
      "    <h:outputLink  value=\"url\" value=\"/index.jsp\">h:outputLink</h:outputLink>\n" +
      "    <h:form>\n" +
      "      <h:commandLink value=\"h:commandLink\"/>\n" +
      "    </h:form>\n" +
      "  </p>\n" +    
      "</body>\n");
    jspEditor.save();
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    // add CSS File Reference
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.INCLUDED_CSS_FILES_TAB).activate();
    optionsDialogBot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    addCssReferenceDialogBot = optionsDialogBot.shell(IDELabel.Shell.ADD_CSS_REFERENCE).activate().bot();
    addCssReferenceDialogBot.textWithLabel(IDELabel.PageDesignOptionsDialog.INCLUDED_CSS_FILES_CSS_FILE_PATH)
      .setText(SWTUtilExt.getPathToProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME) + File.separator +
          "WebContent" + File.separator +
          "pages" + File.separator +
          IncludedCssFilesJSPTest.CSS_FILE_NAME);
    addCssReferenceDialogBot.button(IDELabel.Button.FINISH).click();
    addCssReferenceDialogBot = null;
    optionsDialogBot.button(IDELabel.Button.OK).click();
    optionsDialogBot = null;
    bot.sleep(Timing.time3S());
    SWTBotWebBrowser webBrowser = new SWTBotWebBrowser(IncludedCssFilesJSPTest.JSP_FILE_NAME, new SWTBotExt());
    assertVisualEditorContainsNodeWithValue(webBrowser, 
        "h1 {  color: Red}.post-info {  color: blue;}.post-info a {  color: orange;}",
        IncludedCssFilesJSPTest.JSP_FILE_NAME);
    assertVisualEditorContains(webBrowser,
        "P", 
        new String [] {"class"},
        new String [] {"post-info"},
        IncludedCssFilesJSPTest.JSP_FILE_NAME);
    bot.toolbarButtonWithTooltip(PAGE_DESIGN).click();
    optionsDialogBot = bot.shell(IDELabel.Shell.PAGE_DESIGN_OPTIONS).activate().bot();
    optionsDialogBot.tabItem(IDELabel.PageDesignOptionsDialog.INCLUDED_CSS_FILES_TAB).activate();
    optionsDialogBot.table().select(0);
    optionsDialogBot.button(IDELabel.Button.REMOVE).click();
    optionsDialogBot.button(IDELabel.Button.OK).click();
    optionsDialogBot = null;
    jspEditor.close();
	}
	
	@Override
	protected void closeUnuseDialogs() {
    if (addCssReferenceDialogBot != null){
      addCssReferenceDialogBot.button(IDELabel.Button.CANCEL).click();
      addCssReferenceDialogBot = null;
    }
    if (optionsDialogBot != null){
      optionsDialogBot.button(IDELabel.Button.OK).click();
      optionsDialogBot = null;
    }
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return optionsDialogBot != null
		    || addCssReferenceDialogBot != null;
	}
  
}

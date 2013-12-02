/*******************************************************************************

 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor.properties;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
/**
 * Tests HTML tag properties displayed within Properties View  
 * @author vlado pakan
 *
 */
public class HTMLTagPropertiesTest extends VPEAutoTestCase {
  private SWTBotExt botExt = null;
  
  public HTMLTagPropertiesTest() {
    super();
    botExt = new SWTBotExt();
  }
  /**
   * Checks properties of selected HTML tag displayed within PropertiesView  
   */
  public void testHTMLTagProperties(){
    createDynamicWebProject(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME);
    final String htmlPageName = "HTMLTagPropertiesTest.html";
    createHtmlPage(htmlPageName, VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME,"WebContent");
    SWTBotEclipseEditor htmlPageEditor = eclipse.openFile(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME, 
        "WebContent",
        htmlPageName).toTextEditor();
    htmlPageEditor.setText("<!DOCTYPE html>\n" + 
        "<html>\n" +
        " <head>\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
        "    <link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.css\"/>\n" +
        "    <script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>\n" +
        "    <script src=\"http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.js\"></script>\n" +
        " </head>\n" +
        " <body>\n" +
        "  <div data-role=\"fieldcontain\">\n" +
        "  <fieldset data-role=\"collapsible\" id=\"collapsible-1\">\n" +
        "    <legend>Header</legend>\n" +
        "    <p>Collapsible content.</p>\n" +
        "  </fieldset>\n" +
        "  </div>\n" +
        "  <label><input type=\"checkbox\" name=\"checkbox-1\" id=\"checkbox-1\"/>I agree</label>\n" +        
        " </body>\n" +
        "</html>");
    htmlPageEditor.save();
    htmlPageEditor.close();
    eclipse.openFile(VPEAutoTestCase.DYNAMIC_WEB_TEST_PROJECT_NAME, 
        "WebContent",
        htmlPageName);
    
    SWTJBTExt.selectTextInSourcePane(botExt,
        htmlPageName,
        "<fieldset ",
        6, 
        0,
        0);
    SWTBot propBot = eclipse.showView(ViewType.PROPERTIES);
    SWTBotTree tree = propBot.tree();
    // Check properties for <fieldset> tag
    HTMLTagPropertiesTest.checkProperty(tree, "Often used");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced jQuery");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced HTML");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced jQuery","data-enhance");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced jQuery","data-ajax");
    // Check properties for <input> tag
    SWTJBTExt.selectTextInSourcePane(botExt,
        htmlPageName,
        "<input ",
        3, 
        0,
        0);
    HTMLTagPropertiesTest.checkProperty(tree, "Often used");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced jQuery");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced HTML");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced jQuery","data-mini");
    HTMLTagPropertiesTest.checkProperty(tree, "Advanced jQuery","data-role");
    
  }
	@Override
	protected void closeUnuseDialogs() {
    bot.button(IDELabel.Button.CANCEL).click();
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return bot.activeShell().getText().equals(IDELabel.Shell.INSERT_TAG);
	}
	
	private static SWTBotTreeItem checkProperty (SWTBotTree tree , String... path){
	  SWTBotTreeItem item = null;
	  StringBuffer sbPath = new StringBuffer("");
	  try{
	    sbPath.append(path[0]);
	    item = tree.getTreeItem(path[0]);
	    if (path.length > 1){
	      sbPath.append(", ");
	      sbPath.append(path[1]);
	      item.expand();
	      item = item.getNode(path[1]);
	    }
	  } catch (WidgetNotFoundException wnf){
	    fail("Unable to find property: " + sbPath.toString());	    
	  }
	  return item;	  
	}
}

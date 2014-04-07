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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
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
    final SWTBotTabItem[] tabItems = new SWTBotTabItem[]{bot.tabItem("jQuery"),
        bot.tabItem("HTML"),
        bot.tabItem("All")};
    // Check properties for <fieldset> tag
    tabItems[2].activate();
    SWTBotTable table = propBot.table();
    HTMLTagPropertiesTest.checkProperty(table, "id");
    HTMLTagPropertiesTest.checkProperty(table, "data-enhance");
    HTMLTagPropertiesTest.checkProperty(table, "data-ajax");
    tabItems[1].activate();
    assertTrue("ID attribute has to have value 'collapsible-1'",
      bot.textWithLabel("ID:").getText().equals("collapsible-1"));
    tabItems[0].activate();
    assertTrue("Data Role attribute has to have value 'collapsible'",
        bot.comboBoxWithLabel("Data Role:").getText().equals("collapsible"));
    // Check properties for <input> tag
    SWTJBTExt.selectTextInSourcePane(botExt,
        htmlPageName,
        "<input ",
        3, 
        0,
        0);
    tabItems[2].activate();
    HTMLTagPropertiesTest.checkProperty(table, "id");
    HTMLTagPropertiesTest.checkProperty(table, "data-mini");
    HTMLTagPropertiesTest.checkProperty(table, "data-role");
    tabItems[1].activate();
    assertTrue("ID attribute has to have value 'checkbox-1'",
      bot.textWithLabel("ID:").getText().equals("checkbox-1"));
    assertTrue("Name attribute has to have value 'checkbox-1'",
        bot.textWithLabel("Name:").getText().equals("checkbox-1"));
    assertTrue("Type attribute has to have value 'checkbox'",
        bot.comboBoxWithLabel("Type:").getText().equals("checkbox"));
    tabItems[0].activate();
    assertTrue("Data Role attribute has to have empty value",
        bot.comboBoxWithLabel("Data Role:").getText().equals(""));    
  }
	@Override
	protected void closeUnuseDialogs() {
    bot.button(IDELabel.Button.CANCEL).click();
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return bot.activeShell().getText().equals(IDELabel.Shell.INSERT_TAG);
	}
	
	private static SWTBotTableItem checkProperty (SWTBotTable table , String propName){
	  SWTBotTableItem tableItem = null;
	  try{
	    tableItem = table.getTableItem(propName);
	  } catch (WidgetNotFoundException wnf){
	    fail("Unable to find property: " + propName);	    
	  }
	  return tableItem;	  
	}
}

/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.browsersim;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;

/**
 * Assertions for BrowserSim tests
 * @author Vladimir Pakan
 *
 */
public class BrowserSimAssertions {
  /**
   * Asserts BrowserSim browser contains textToCotnain
   * @param browserSimHandler
   * @param textToContain
   */
  public static void assertBrowserTextContains (BrowserSimHandler browserSimHandler, String textToContain){
    String browserText = browserSimHandler.getBrowserSimBrowser().getText();
    assertTrue("BrowserSim browser has to contain text '" + textToContain + "' but it doesn't.\n" +
        "BrowserSim browser text is:\n" + browserText,
      browserText.toLowerCase().contains(textToContain.toLowerCase()));
  }
  /**
   * Asserts BrowserSim browser text is text
   * @param browserSimHandler
   * @param text
   */
  public static void assertBrowserTextIs (BrowserSimHandler browserSimHandler, String text){
    String browserText = browserSimHandler.getBrowserSimBrowser().getText();
    assertTrue("BrowserSim browser has to be '" + text + "' but it's not.\n" +
        "BrowserSim browser text is:\n" + browserText,
      browserText.equalsIgnoreCase(text));
  }
  /**
   * Asserts BrowserSim Address bar contains textToCotnain
   * @param browserSimHandler
   * @param textToContain
   */
  public static void assertAddressBarContains (BrowserSimHandler browserSimHandler, String textToContain){
    String addressBarText = browserSimHandler.getAddressText().getText();
    assertTrue("BrowserSim address bar has to contain text '" + textToContain + "' but it doesn't.\n" +
        "BrowserSim address bar text is:\n" + addressBarText,
        addressBarText.toLowerCase().contains(textToContain.toLowerCase()));
  }
  /**
   * Asserts BrowserSim Address bar text is text
   * @param browserSimHandler
   * @param text
   */
  public static void assertAddressBarTextIs (BrowserSimHandler browserSimHandler, String text){
    String addressBarText = browserSimHandler.getAddressText().getText();
    assertTrue("BrowserSim address bar has to be '" + text + "' but it's not.\n" +
        "BrowserSim address bar text is:\n" + addressBarText,
        addressBarText.equalsIgnoreCase(text));
  }
  /**
   * Asserts BrowserSim Title bar contains textToCotnain
   * @param browserSimHandler
   * @param textToContain
   */
  public static void assertTitleBarContains (BrowserSimHandler browserSimHandler, String textToContain){
    String titleBarText = browserSimHandler.getTitleStyledText().getText();
    assertTrue("BrowserSim title bar has to contain text '" + textToContain + "' but it doesn't.\n" +
        "BrowserSim title bar text is:\n" + titleBarText,
        titleBarText.toLowerCase().contains(textToContain.toLowerCase()));
  }
  /**
   * Asserts BrowserSim Title bar text is text
   * @param browserSimHandler
   * @param text
   */
  public static void assertTitleBarTextIs (BrowserSimHandler browserSimHandler, String text){
    String titleBarText = browserSimHandler.getTitleStyledText().getText();
    assertTrue("BrowserSim address bar has to be '" + text + "' but it's not.\n" +
        "BrowserSim title bar text is:\n" + titleBarText,
        titleBarText.equalsIgnoreCase(text));
  }
  /**
   * Asserts equality of arrays containing menu items labels and expected menu items labels
   * @param menuItems
   * @param expectedMenuItems
   */
  public static void assertMenuContent (String[] menuItems , String[] expectedMenuItems){
    assertTrue("BrowserSim Context Menu has wrong content:\n" +
        "It has items:\n" +
        BrowserSimAssertions.displayFormattedArrayContent(menuItems) +
        "Expected items are:\n" +
        BrowserSimAssertions.displayFormattedArrayContent(expectedMenuItems)
      ,BrowserSimAssertions.menuItemsLabelsEquals(menuItems, expectedMenuItems));
  }
  /**
   * Returns formatted interpretation of stringArray used for displaying strinArray content
   * @param stringArray
   * @return
   */
  private static String displayFormattedArrayContent (String[] stringArray){
    StringBuffer sb = new StringBuffer("");
    
    if (stringArray != null){
      if (stringArray.length > 0){
        for (String item : stringArray){
          sb.append(item);
          sb.append("\n");
        }
      }
      else{
        sb.append("<empty>\n");
      }
    }
    else{
      sb.append("<null>\n");
    }
    
    return sb.toString();
  }
  /**
   * Compare menu items labels stored in string arrays
   * @param menuLabels1
   * @param menuLabels2
   * @return
   */
  private static boolean menuItemsLabelsEquals (String[] menuLabels1 , String[] menuLabels2){
    
    boolean areEqual = false;
    
    if (menuLabels1 == null && menuLabels2 == null){
      areEqual = true;
    }
    else if (menuLabels1 != null 
      && menuLabels2 != null
      && menuLabels1.length == menuLabels2.length){
      boolean areEqualYet = true;
      int index = 0;
      while (areEqualYet && (index < menuLabels1.length)){
        if (!ContextMenuHelper.trimMenuItemLabel(menuLabels1[index])
               .equals(menuLabels2[index])){
          areEqualYet = false;
        }
        index++;
      }
      if (areEqualYet){
        areEqual = true;
      }
    }

    return areEqual;
    
  }
}

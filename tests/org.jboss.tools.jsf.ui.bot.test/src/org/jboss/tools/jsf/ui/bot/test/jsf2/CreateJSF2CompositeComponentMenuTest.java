/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.jsf2;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.lookup.EditorPartLookup;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;
/** Tests Functionality of Create JSF2 Composite Component Menu Item of Context Menu
 * @author Vladimir Pakan
 *
 */
public class CreateJSF2CompositeComponentMenuTest extends JSFAutoTestCase{
  private static final String CC_NAME_SPACE = "ccNameSpace";
  private static final String CC_NAME = "ccName";
  private static final String CC_FILE_NAME = CC_NAME + ".xhtml";
  private static final String CREATE_JSF2_COMPOSITE = "Create JSF2 composite...";
  private TextEditor jsf2editor = null;
  private String originalContent = null;
  /**
   * Test if menu is working correctly  
   */
  @Test
  public void testMenuFunctionality(){
    EditorHandler.getInstance().closeAll(true);
    createJSF2Project(JSF2_TEST_PROJECT_NAME);
    openPage(JSF2_TEST_PAGE, JSF2_TEST_PROJECT_NAME);
    jsf2editor = new TextEditor(JSF2_TEST_PAGE);
    originalContent = jsf2editor.getText();
    jsf2editor.setCursorPosition(jsf2editor.getPositionOfText("<ui:define ") + "<ui:define ".length());
    KeyboardFactory.getKeyboard().type("\n");
    new ContextMenu(CreateJSF2CompositeComponentMenuTest.CREATE_JSF2_COMPOSITE).select();
    new DefaultShell("Creating composite component");
    new DefaultText().setText(CC_NAME_SPACE + ":" + CC_NAME);
    new OkButton().click();
    new WaitWhile(new JobIsRunning());
    final String activeEditorTitle = EditorPartLookup.getInstance().getActiveEditor().getTitle();
    jsf2editor.save();
    new WaitWhile(new JobIsRunning());
    assertTrue(activeEditorTitle.equals(CC_FILE_NAME));
    Assertions.assertFileExistsInWorkspace(CC_FILE_NAME, JSF2_TEST_PROJECT_NAME,"WebContent","resources",CC_NAME_SPACE);
    final String editorText = jsf2editor.getText();
    Assertions.assertSourceEditorContains(editorText.replaceAll(" ", ""),
      "<" + CreateJSF2CompositeComponentMenuTest.CC_NAME_SPACE + ":" + CreateJSF2CompositeComponentMenuTest.CC_NAME + ">" +
        "</" + CreateJSF2CompositeComponentMenuTest.CC_NAME_SPACE + ":" + CreateJSF2CompositeComponentMenuTest.CC_NAME + ">",
      JSF2_TEST_PROJECT_NAME);
    Assertions.assertSourceEditorContains(editorText,
      "xmlns:" + CreateJSF2CompositeComponentMenuTest.CC_NAME_SPACE + 
        "=\"http://java.sun.com/jsf/composite/" + CreateJSF2CompositeComponentMenuTest.CC_NAME_SPACE + "\"",
      JSF2_TEST_PROJECT_NAME);
  }
  /**
   * Tests if Menu is Not present for JSF project version 1.2
   */
  @Test
  public void testMenuNotPresentForJSF12Project(){
    // Test default JSF 1.2 Project 
    openPage(VPEAutoTestCase.TEST_PAGE,VPEAutoTestCase.JBT_TEST_PROJECT_NAME);
    try{
    	new ContextMenu(CreateJSF2CompositeComponentMenuTest.CREATE_JSF2_COMPOSITE); 
    	fail("Menu should not contain menu item:'" + IDELabel.Menu.CREATE_JSF2_COMPOSITE + "'");
    } catch (CoreLayerException cle){
      // correct menu is not available
    }
    // Test JSF 1.2 Project with Facelets
    openPage(VPEAutoTestCase.FACELETS_TEST_PAGE,VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME);
    assertFalse("Menu Item has to be disabled but is not" ,
    	new ContextMenu(CreateJSF2CompositeComponentMenuTest.CREATE_JSF2_COMPOSITE).isEnabled());
  }
  @Override
  public void tearDown() throws Exception {
    if (jsf2editor != null) {
      jsf2editor.setText(originalContent);
      jsf2editor.save();
      jsf2editor.close();
      new WaitWhile(new JobIsRunning());
    }
    if (FileHelper.isExistingFileWithinWorkspace(CC_NAME_SPACE, JSF2_TEST_PROJECT_NAME,"WebContent","resources")){
    	packageExplorer.getProject(JSF2_TEST_PROJECT_NAME)
    		.getProjectItem("WebContent","resources",CC_NAME_SPACE)
    		.delete();
    }
    super.tearDown();
  }
}
  

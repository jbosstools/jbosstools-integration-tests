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
package org.jboss.tools.jsf.ui.bot.test.smoke;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.common.reddeer.propertieseditor.PropertiesEditor;
import org.jboss.tools.common.reddeer.propertieseditor.PropertiesSourceEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;
/** Test for properties editor
 * @author Vladimir Pakan
 *
 */
public class PropertiesEditorTest extends JSFAutoTestCase{
  
  protected static final String PROPERTIES_FILE_NAME = "Messages.properties";
  private PropertiesEditor propertiesEditor;
  private String originalContent;
  private PropertiesSourceEditor propertiesSourceEditor;
    
  @Override
  public void setUp() throws Exception {
    super.setUp();
    EditorHandler.getInstance().closeAll(true);
    packageExplorer.open();
    packageExplorer.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
    	.getProjectItem("JavaSource","demo",PropertiesEditorTest.PROPERTIES_FILE_NAME).open();
    propertiesEditor = new PropertiesEditor(PropertiesEditorTest.PROPERTIES_FILE_NAME);
    propertiesSourceEditor = propertiesEditor.getPropertiesSourceEditor();
    originalContent = propertiesSourceEditor.getText();
  }
  
  @Override
  public void tearDown() throws Exception {
    if (propertiesEditor != null) {
      propertiesSourceEditor.setText(originalContent);
      propertiesSourceEditor.save();
      propertiesSourceEditor.close();
    }
    super.tearDown();
  }
  /**
   * Tests Properties Editor
   */
  @Test
  public void testPropertiesEditor(){
    propertiesEditor.activateSourceTab();
    String[] originalLines = PropertiesEditorTest.splitEditorContentToLines(propertiesSourceEditor);
    propertiesEditor.activatePropertiesTab();
    // Add Property
    propertiesEditor.selectProperty(propertiesEditor.getPropertiesCount() - 1);
    final String newPropertyName = "newPropName";
    final String newPropertyValue = "newPropValue";
    propertiesEditor.addProperty(newPropertyName, newPropertyValue);
    propertiesEditor.save();
    new WaitWhile(new JobIsRunning());
    int newPropertyIndex = originalLines.length;
    Assertions.assertSourceEditorContains(PropertiesEditorTest.stripXMLSourceText(
        propertiesSourceEditor.getText()), 
      getExpectedPropertiesEditorText(originalLines,
        newPropertyName,
        newPropertyValue,
        newPropertyIndex),
      PropertiesEditorTest.PROPERTIES_FILE_NAME);
    assertFalse("Button 'Down' has to be disabled" , propertiesEditor.getDownButton().isEnabled());
    // Move Property
    propertiesEditor.selectProperty(newPropertyName);
    propertiesEditor.getUpButton().click();
    propertiesEditor.save();
    Assertions.assertSourceEditorContains(PropertiesEditorTest.stripXMLSourceText(
        propertiesSourceEditor.getText()), 
      getExpectedPropertiesEditorText(originalLines,
        newPropertyName,
        newPropertyValue,
        --newPropertyIndex),
      PropertiesEditorTest.PROPERTIES_FILE_NAME);
    assertTrue("Button 'Down' has to be enabled",propertiesEditor.getDownButton().isEnabled());
    assertTrue("Button 'Up' has to be enabled",propertiesEditor.getUpButton().isEnabled());
    propertiesEditor.getDownButton().click();
    propertiesEditor.save();
    Assertions.assertSourceEditorContains(PropertiesEditorTest.stripXMLSourceText(
        propertiesSourceEditor.getText()), 
      getExpectedPropertiesEditorText(originalLines,
        newPropertyName,
        newPropertyValue,
        ++newPropertyIndex),
      PropertiesEditorTest.PROPERTIES_FILE_NAME);
    assertFalse("Button 'Down' has to be disabled" , propertiesEditor.getDownButton().isEnabled());
    assertTrue("Button 'Up' has to be enabled",propertiesEditor.getUpButton().isEnabled());
    // Move to Top
    PushButton upButton = propertiesEditor.getUpButton();
    while (newPropertyIndex > 0){
      upButton.click();
      newPropertyIndex--;
    }
    propertiesEditor.save();
    Assertions.assertSourceEditorContains(PropertiesEditorTest.stripXMLSourceText(
        propertiesSourceEditor.getText()), 
      getExpectedPropertiesEditorText(originalLines,
        newPropertyName,
        newPropertyValue,
        newPropertyIndex),
      PropertiesEditorTest.PROPERTIES_FILE_NAME);
    assertTrue("Button 'Down' has to be enaled" , propertiesEditor.getDownButton().isEnabled());
    assertFalse("Button 'Up' has to be disabled",propertiesEditor.getUpButton().isEnabled());
    // Update Property Directly
    propertiesEditor.selectProperty(newPropertyIndex);
    final String updatedPropertyName = "upn";
    final String updatedPropertyValue = "upv";
    propertiesEditor.selectProperty(newPropertyIndex);
    propertiesEditor.clickOnPropertyName(newPropertyIndex);
    KeyboardFactory.getKeyboard().type(updatedPropertyName);
    propertiesEditor.clickOnPropertyValue(newPropertyIndex);
    KeyboardFactory.getKeyboard().type(updatedPropertyValue);
    propertiesEditor.selectProperty(newPropertyIndex);
    propertiesEditor.save();
    Assertions.assertSourceEditorContains(PropertiesEditorTest.stripXMLSourceText(
        propertiesSourceEditor.getText()), 
      getExpectedPropertiesEditorText(originalLines,
        updatedPropertyName,
        updatedPropertyValue,
        newPropertyIndex),
      PropertiesEditorTest.PROPERTIES_FILE_NAME);
    // Update Property via Dialog
    propertiesEditor.selectProperty(newPropertyIndex);
    propertiesEditor.getEditButton().click();
    new DefaultShell("Edit");
    Text txName = new DefaultText(0);
    assertEquals("Text with label Name: has to have value " + updatedPropertyName + 
        " but has " + txName.getText(),
      txName.getText(), updatedPropertyName);
    Text txValue = new DefaultText(1);
    assertEquals("Text with label Value: has to have value " + updatedPropertyValue + 
        " but has " + txValue.getText(),
      txValue.getText(), updatedPropertyValue);
    txName.setText(newPropertyName);
    txValue.setText(newPropertyValue);
    new FinishButton().click();
    propertiesEditor.save();
    new WaitWhile(new JobIsRunning());
    Assertions.assertSourceEditorContains(PropertiesEditorTest.stripXMLSourceText(
        propertiesSourceEditor.getText()), 
      getExpectedPropertiesEditorText(originalLines,
        newPropertyName,
        newPropertyValue,
        newPropertyIndex),
      PropertiesEditorTest.PROPERTIES_FILE_NAME);
    // Delete Property
    propertiesEditor.selectProperty(newPropertyIndex);
    propertiesEditor.getDeleteButton().click();
    new DefaultShell("Confirmation");
    new OkButton().click();
    new WaitWhile(new JobIsRunning());
    propertiesEditor.save();
    new WaitWhile(new JobIsRunning());
    String sourceEditorText = propertiesSourceEditor.getText();
    assertEquals("Properties file " + PropertiesEditorTest.PROPERTIES_FILE_NAME + 
        " should have content:\n" + originalContent +
        "\nbut has:\n" + sourceEditorText,
      originalContent, sourceEditorText);
  }
  /**
   * Split editor content to particular lines
   * @param propertiesSourceEditor
   * @return
   */
  private static String[] splitEditorContentToLines(PropertiesSourceEditor propertiesSourceEditor){
    
    String[] result = new String[propertiesSourceEditor.getNumberOfLines()];
    
    for (int index = 0 ; index < result.length ; index ++){
      result[index] = propertiesSourceEditor.getTextAtLine(index);
    }
    
    return result;
    
  }
  /**
   * Returns Properties File Source striped from EOL
   * 
   * @return String
   */
  private static String stripXMLSourceText(String editorText) {
    return editorText.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\f", "");
  } 
  /**
   * Returns expected Properties Editor Text dependent on expected new Property position within
   * properties file
   * @param originalLines
   * @param propertyName
   * @param propertyValue
   * @param propertyIndex
   * @return
   */
  private static String getExpectedPropertiesEditorText (String[] originalLines, 
      String propertyName,
      String propertyValue,
      int propertyIndex){
    StringBuffer sbExpectedText = new StringBuffer("");
    
    for (int index = 0 ; index < propertyIndex ; index++){
      sbExpectedText.append(originalLines[index]);
    }
    sbExpectedText.append(propertyName);
    sbExpectedText.append("=");
    sbExpectedText.append(propertyValue);
    
    for (int index = propertyIndex ; index < originalLines.length ; index++){
      sbExpectedText.append(originalLines[index]);
    }
    
    return sbExpectedText.toString();
    
  }
}
  

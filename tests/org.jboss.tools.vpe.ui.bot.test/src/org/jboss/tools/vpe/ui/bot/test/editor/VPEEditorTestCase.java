/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.jboss.tools.vpe.ui.bot.test.Activator;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public abstract class VPEEditorTestCase extends VPEAutoTestCase{

	private String editorText;
	private SWTBotEclipseEditor editor;
	
	protected String getEditorText() {
		return editorText;
	}

	protected void setEditorText(String textEditor) {
		this.editorText = textEditor;
	}

	protected SWTBotEclipseEditor getEditor() {
		return editor;
	}

	protected void setEditor(SWTBotEclipseEditor editor) {
		this.editor = editor;
	}

	@Override
	protected boolean isUnuseDialogOpened() {
		return false;
	}
	
	@Override
	protected void closeUnuseDialogs() {
	}
	
	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"resources/editor/"+testPage; //$NON-NLS-1$ //$NON-NLS-2$
		File file = new File(filePath);
		if (!file.isFile()) {
			filePath = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile()+"editor/"+testPage;  //$NON-NLS-1$//$NON-NLS-2$
		}
		return filePath;
	}
	
	protected String getPathToRootResources(String testPage) throws IOException {
    return super.getPathToResources(testPage);
  }
	protected void openPage(){
		SWTBot innerBot = bot.viewByTitle(WidgetVariables.PACKAGE_EXPLORER).bot();
		SWTBotTree tree = innerBot.tree();
		tree.expandNode(JBT_TEST_PROJECT_NAME)
		  .expandNode("WebContent").expandNode("pages").getNode(TEST_PAGE).doubleClick(); //$NON-NLS-1$ //$NON-NLS-2$
		// wait for page to be opened
		bot.editorByTitle(TEST_PAGE);
	}
	
	void checkVPE(String testPage) throws Throwable{
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
		performContentTestByDocument(testPage, bot.multiPageEditorByTitle(TEST_PAGE));
	}

	@Override
	public void tearDown() throws Exception {

		//Restore page state before tests
    if (editor != null){
      editor.setFocus();
      bot.menu("Edit").menu("Select All").click(); //$NON-NLS-1$ //$NON-NLS-2$
      bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
      editor.setText(editorText);
      editor.save();
      bot.sleep(Timing.time5S());
    }
		super.tearDown();
	}
	
	 /**
   * Returns HTML Source striped from spaces, tabs and EOL
   * @return String
   */
  protected static String stripHTMLSourceText(String editorText){
    return editorText.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\b", "")
             .replaceAll(" ", "").replaceAll("\r", "").replaceAll("\f", "");
  }

  /**
   * Asserts if Visual Editor contains node with particular attributes
   * @param webBrowser
   * @param nodeNameToContain
   * @param attributeNames
   * @param attributeValues
   * @param fileName
   */
  protected static void assertVisualEditorContains (SWTBotWebBrowser webBrowser, 
      String nodeNameToContain, 
      String[] attributeNames, String[] attributeValues,
      String fileName){
    
    assertTrue("Visual Representation of file " + fileName
        + " has to contain "
        + nodeNameToContain
        + " node but it doesn't",
        webBrowser.containsNodeWithNameAndAttributes(webBrowser.getMozillaEditor().getDomDocument(), 
            nodeNameToContain,
            attributeNames,
            attributeValues));
    
  }
  /**
   * Asserts if Visual Editor contains node nodeNameToContain exactly numOccurrencies times
   * @param webBrowser
   * @param nodeNameToContain
   * @param numOccurrences
   * @param fileName
   */
  protected static void assertVisualEditorContainsManyNodes (SWTBotWebBrowser webBrowser, 
      String nodeNameToContain, 
      int numOccurrences,
      String fileName){
    
    assertTrue("Visual Representation of file " + fileName
        + " has to contain "
        + nodeNameToContain
        + " node "
        + (numOccurrences)
        + " times but it doesn't",
        webBrowser.getDomNodeOccurenciesByTagName(nodeNameToContain) == numOccurrences);
    
  }
  /**
   * Asserts if Visual Editor contains node with value valueToContain
   * @param webBrowser
   * @param valueToContain
   * @param fileName
   */
  protected static void assertVisualEditorContainsNodeWithValue (SWTBotWebBrowser webBrowser,
      String valueToContain,
      String fileName){
    
    assertTrue("Visual Representation of file " + fileName
        + " has to contain node with "
        + valueToContain
        + " value but it doesn't",
        webBrowser.containsNodeWithValue(webBrowser.getMozillaEditor().getDomDocument(),
            valueToContain));
    
  }
  
  /**
   * Asserts if Visual Editor doesn't contain node with particular attributes
   * @param webBrowser
   * @param valueToContain
   * @param fileName
   */
  protected static void assertVisualEditorNotContainNodeWithValue (SWTBotWebBrowser webBrowser,
      String valueToContain,
      String fileName){
    
    assertFalse("Visual Representation of file " + fileName
        + " cannot contain node with "
        + valueToContain
        + " value but it does",
        webBrowser.containsNodeWithValue(webBrowser.getMozillaEditor().getDomDocument(), 
            valueToContain));
    
  }
  /**
   * Asserts if Visual Editor doesn't contain node with particular attributes
   * @param webBrowser
   * @param nodeNameToContain
   * @param attributeNames
   * @param attributeValues
   * @param fileName
   */
  protected static void assertVisualEditorNotContain (SWTBotWebBrowser webBrowser, 
      String nodeNameToContain, 
      String[] attributeNames, String[] attributeValues,
      String fileName){
    
    assertFalse("Visual Representation of file " + fileName
        + " cannot contain "
        + nodeNameToContain
        + " node but it does",
        webBrowser.containsNodeWithNameAndAttributes(webBrowser.getMozillaEditor().getDomDocument(), 
            nodeNameToContain,
            attributeNames,
            attributeValues));
    
  }
  /**
   * Asserts if Visual Editor contains node nodeNameToContain exactly numOccurrencies times
   * @param webBrowser
   * @param nodeNameToContain
   * @param numOccurrences
   * @param fileName
   */
  protected static void assertVisualEditorContainsManyComments (SWTBotWebBrowser webBrowser, 
     int numOccurrences,
     String fileName){
    
    assertTrue("Visual Representation of file " + fileName
        + " has to contain "
        + numOccurrences
        + " comment nodes but it doesn't",
        webBrowser.getCommentNodes().size() == numOccurrences);
    
  }
}

/******************************************************************************* 
 * Copyright (c) 2012 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.vpe.reddeer.utils.BrowserUtils;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;

public abstract class JSFAutoTestCase extends VPEAutoTestCase {

	private String editorText;
	private TextEditor editor;

	String getEditorText() {
		return editorText;
	}

	protected void setEditorText(String textEditor) {
		this.editorText = textEditor;
	}

	protected TextEditor getEditor() {
		return editor;
	}

	protected void setEditor(TextEditor editor) {
		this.editor = editor;
	}

	@Override
	protected String getPathToResources(String testPage) throws IOException {
		String filePath = FileLocator
				.toFileURL(
						Platform.getBundle(Activator.PLUGIN_ID).getEntry("/")).getFile() + "resources/" + testPage; //$NON-NLS-1$ //$NON-NLS-2$

		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			filePath = FileLocator
					.toFileURL(
							Platform.getBundle(Activator.PLUGIN_ID).getEntry(
									"/")).getFile() + testPage; //$NON-NLS-1$ 
		}
		return filePath;
	}

	protected void openTestPage() {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent","pages",TEST_PAGE).open();
	}

	protected void checkVPE(String testPage) throws Throwable {
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
		performContentTestByDocument(testPage, bot
				.multiPageEditorByTitle(TEST_PAGE));
	}

	@Override
	public void tearDown() throws Exception {
		// Restore page state before tests
		if (editor != null) {
			editor.activate();
			editor.setText(editorText);
			editor.save();
			new WaitWhile (new JobIsRunning());
		}
		super.tearDown();
	}

	protected String loadFileContent(String resourceRelativePath) throws IOException {
		File file = new File(getPathToResources(resourceRelativePath));
		StringBuilder builder = new StringBuilder(""); //$NON-NLS-1$
		Scanner scanner = null;
    try {
      scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        builder.append(scanner.nextLine() + "\n"); //$NON-NLS-1$
      }
    } 
    catch (IllegalStateException e) {
      if (scanner != null) {
        scanner.close();
      }
    }
    catch (NoSuchElementException e) {
      if (scanner != null) {
        scanner.close();
      }
    }finally{
        if (scanner != null) {
            scanner.close();
          }
    }
    
    return builder.toString();
	}
  /**
   * Returns CSS Editor text striped from spaces, tabs CR and EOL
   * @param editorText
   * @return String
   */
  protected static String stripCSSText(String editorText){
    return editorText.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "").replaceAll(" ", "");
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
        webBrowser.containsNodeWithValue(webBrowser,
            valueToContain));
    
  }
  /**
   * Asserts if Visual Editor contains node with value valueToContain
   * @param browser
   * @param valueToContain
   * @param fileName
   */
  protected static void assertVisualEditorContainsNodeWithValue (Browser browser,
      String valueToContain,
      String fileName){
    assertTrue("Visual Representation of file " + fileName
        + " has to contain node with "
        + valueToContain
        + " value but it doesn't",
        BrowserUtils.containsNodeWithValue(browser,
            valueToContain));    
  }
  /**
   * Asserts if Visual Editor does not contain node with value valueToContain
   * @param browser
   * @param valueToContain
   * @param fileName
   */
  protected static void assertVisualEditorNotContainNodeWithValue (Browser browser,
      String valueToContain,
      String fileName){
    assertFalse("Visual Representation of file " + fileName
        + " cannot contain node with "
        + valueToContain
        + " value but it does",
        BrowserUtils.containsNodeWithValue(browser,
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
        webBrowser.containsNodeWithValue(webBrowser, 
            valueToContain));
    
  }
  /**
   * Asserts if Visual Editor contains textToContain
   * @param browser
   * @param textToContain
   * @param fileName
   */
  protected static void assertVisualEditorContainsString (Browser browser,
      String textToContain,
      String fileName){
    
    assertFalse("Visual Representation of file " + fileName
        + " has to contain string '"
        + textToContain
        + "' but it does not",
        BrowserUtils.containsStringValue(browser, textToContain));    
  }
  /**
   * Asserts if Visual Editor does not contain textToContain
   * @param browser
   * @param textToContain
   * @param fileName
   */
  protected static void assertVisualEditorNotContainString (Browser browser,
      String textToContain,
      String fileName){
    
    assertFalse("Visual Representation of file " + fileName
        + " cannot to contain string '"
        + textToContain
        + "' but it does",
        BrowserUtils.containsStringValue(browser, textToContain));    
  }
}

/*******************************************************************************

 * Copyright (c) 2007-2016 Exadel, Inc. and Red Hat, Inc.
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

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * Tests editing of web page via external editor
 * 
 * @author vlado pakan
 *
 */
public class ExternalEditingTest extends VPEEditorTestCase {

	private TextEditor jspEditor;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		new WorkbenchShell().maximize();
		EditorHandler.getInstance().closeAll(true);
	}

	/**
	 * Checks External Editing of web page when External changes are accepted
	 */
	@Test
	public void testAcceptExternalChanges() {
		final String acceptExtChangesPageName = "ExternalEditingTestAccept.jsp";
		createJspPage(acceptExtChangesPageName);
		jspEditor = new TextEditor(acceptExtChangesPageName);
		final String originalPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<html>\n" + "  <body>\n" + "  </body>\n" + "</html>";
		jspEditor.setText(originalPageContent);
		jspEditor.save();
		// modify web page externally
		final String changedPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<html>\n" + "  <body>\n" + "    !@#$%CHANGED_TEXT%$#@!\n" + "  </body>\n" + "</html>";
		try {
			FileHelper.modifyTextFile(getPageLocation(acceptExtChangesPageName), changedPageContent);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		jspEditor.activate();
		new TypeKeyCodesThread(new int[] { SWT.CR }).start();
		jspEditor.activate();
		String sourceText = jspEditor.getText();
		assertTrue("VPE Source pane has to contain text\n" + changedPageContent + "'\nbut it contains\n" + sourceText,
				sourceText.equals(changedPageContent));
	}

	/**
	 * Checks External Editing of web page when External changes are denied
	 */
	@Test
	public void testDenyExternalChanges() {
		final String denyExtChangesPageName = "ExternalEditingTestDeny.jsp";
		createJspPage(denyExtChangesPageName);
		jspEditor = new TextEditor(denyExtChangesPageName);
		final String originalPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<html>\n" + "  <body>\n" + "  </body>\n" + "</html>";
		jspEditor.setText(originalPageContent);
		jspEditor.save();
		// modify web page externally
		final String changedPageContent = "<%@ taglib uri=\"http://java.sun.com/jsf/html\" prefix=\"h\" %>\n"
				+ "<html>\n" + "  <body>\n" + "    !@#$%CHANGED_TEXT%$#@!\n" + "  </body>\n" + "</html>";
		try {
			FileHelper.modifyTextFile(getPageLocation(denyExtChangesPageName), changedPageContent);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		jspEditor.activate();
		new TypeKeyCodesThread(new int[] { SWT.TAB, SWT.CR }).start();
		jspEditor.activate();
		String sourceText = jspEditor.getText();
		assertTrue("VPE Source pane has to contain text\n" + originalPageContent + "'\nbut it contains\n" + sourceText,
				sourceText.equals(originalPageContent));
		jspEditor.close();
	}

	@Override
	public void tearDown() throws Exception {
		jspEditor.close();
		super.tearDown();
	}

	/**
	 * Returns absolute page pageName location
	 * 
	 * @param pageName
	 * @return
	 */
	private String getPageLocation(String pageName) {
		StringBuffer sbPageLocation = new StringBuffer("");
		sbPageLocation.append(FileHelper.getProjectLocation(VPEAutoTestCase.JBT_TEST_PROJECT_NAME));
		sbPageLocation.append(File.separator);
		sbPageLocation.append("WebContent");
		sbPageLocation.append(File.separator);
		sbPageLocation.append("pages");
		sbPageLocation.append(File.separator);
		sbPageLocation.append(pageName);
		return sbPageLocation.toString();
	}

	/**
	 * Thread closing dialog displayed when page is modified externally
	 */
	class TypeKeyCodesThread extends Thread {
		private int[] keyCodes;

		public TypeKeyCodesThread(int[] keyCodes) {
			super();
			this.keyCodes = keyCodes;
		}

		public void run() {
			try {
				System.out.println("**-- start thread");
				sleep(TimePeriod.getCustom(5).getSeconds() * 1000);
				;
				for (int keyCode : keyCodes) {
					System.out.println("**--Type: " + keyCode);
					KeyboardFactory.getKeyboard().invokeKeyCombination(keyCode);
					sleep(TimePeriod.getCustom(2).getSeconds() * 1000);
				}
			} catch (InterruptedException e) {
			}
		}
	}
}

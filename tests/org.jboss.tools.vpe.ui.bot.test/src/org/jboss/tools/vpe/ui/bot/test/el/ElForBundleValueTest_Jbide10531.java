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
package org.jboss.tools.vpe.ui.bot.test.el;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.helper.KeyboardHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
import org.mozilla.interfaces.nsIDOMNode;

public class ElForBundleValueTest_Jbide10531 extends VPEEditorTestCase {
	
	private SWTBotExt botExt = null;
	private SWTBotEditorExt jspTextEditor;
	private SWTBotWebBrowser webBrowser;

	private final String OUT1 = "<br> <h:outputText value=\"#{Message['prompt_message'}\" /> \n"; //$NON-NLS-1$
	private final String OUT2 = "<br> <h:outputText value=\"#{Message[hello_message']}\" /> \n"; //$NON-NLS-1$
	private final String OUT3 = "<br> <h:outputText value=\"#{Message'[prompt_message']}\" /> \n"; //$NON-NLS-1$
	private final String OUT4 = "<br> <h:outputText value=\"#{Message[hello_message]}\" /> \n"; //$NON-NLS-1$

	private final String OUT5 = "<br /> <h:outputText value=\"#{msg['prompt'}\" /> \n"; //$NON-NLS-1$
	private final String OUT6 = "<br /> <h:outputText value=\"#{msg[greeting']}\" /> \n"; //$NON-NLS-1$
	private final String OUT7 = "<br /> <h:outputText value=\"#{msg'[prompt']}\" /> \n"; //$NON-NLS-1$
	private final String OUT8 = "<br /> <h:outputText value=\"#{msg[greeting]}\" /> \n"; //$NON-NLS-1$
	
	
	public ElForBundleValueTest_Jbide10531() {
		super();
		botExt = new SWTBotExt();
	}

	public void testElForBundleValueInJSP() throws Throwable {
		openPage();
		util.waitForAll();
		jspTextEditor = botExt.swtBotEditorExtByTitle(TEST_PAGE); 
		setEditor(jspTextEditor);
		webBrowser = new SWTBotWebBrowser(TEST_PAGE, botExt);
		setEditorText(jspTextEditor.getText());
		jspTextEditor.setFocus();
		Display d = bot.getDisplay();
		
		jspTextEditor.insertText(13, 0, OUT4);
		jspTextEditor.insertText(13, 0, OUT3);
		jspTextEditor.insertText(13, 0, OUT2);
		jspTextEditor.insertText(13, 0, OUT1);
		util.sleep(TIME_1S);
		
		KeyboardHelper.pressKeyCode(d, SWT.F5);
		util.sleep(TIME_1S);
		webBrowser.setFocus();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("TD", 0); //$NON-NLS-1$
		util.sleep(TIME_1S);
		webBrowser.containsNodeWithValue(node, "#{Message['prompt_message'}"); //$NON-NLS-1$
		webBrowser.containsNodeWithValue(node, "#{Message[hello_message']}"); //$NON-NLS-1$
		webBrowser.containsNodeWithValue(node, "#{Message'[prompt_message']}"); //$NON-NLS-1$
		webBrowser.containsNodeWithValue(node, "#{Message[hello_message]}"); //$NON-NLS-1$
	}

	public void testElForBundleValueInXHTML() throws Throwable {
		openPage(FACELETS_TEST_PAGE, FACELETS_TEST_PROJECT_NAME);
		util.waitForAll();
		jspTextEditor = botExt.swtBotEditorExtByTitle(FACELETS_TEST_PAGE); 
		setEditor(jspTextEditor);
		webBrowser = new SWTBotWebBrowser(FACELETS_TEST_PAGE, botExt);
		setEditorText(jspTextEditor.getText());
		jspTextEditor.setFocus();
		Display d = bot.getDisplay();
		
		jspTextEditor.insertText(19, 0, OUT8);
		jspTextEditor.insertText(19, 0, OUT7);
		jspTextEditor.insertText(19, 0, OUT6);
		jspTextEditor.insertText(19, 0, OUT5);
		util.sleep(TIME_1S);
		
		KeyboardHelper.pressKeyCode(d, SWT.F5);
		util.sleep(TIME_1S);
		webBrowser.setFocus();
		nsIDOMNode node = webBrowser.getDomNodeByTagName("FORM", 0); //$NON-NLS-1$
		util.sleep(TIME_1S);
		webBrowser.containsNodeWithValue(node, "#{msg['prompt'}"); //$NON-NLS-1$
		webBrowser.containsNodeWithValue(node, "#{msg[greeting']}"); //$NON-NLS-1$
		webBrowser.containsNodeWithValue(node, "#{msg'[prompt']}"); //$NON-NLS-1$
		webBrowser.containsNodeWithValue(node, "#{msg[greeting]}"); //$NON-NLS-1$
	}
}

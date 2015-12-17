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
package org.jboss.tools.vpe.ui.bot.test.editor;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;

public class VerificationOfNameSpacesTest extends VPEEditorTestCase{

	private static String testText = "<jsp:root\n" + //$NON-NLS-1$
			"xmlns:jsp=\"http://java.sun.com/JSP/Page\n" + //$NON-NLS-1$
			"xmlns:public=\"http://www.jspcentral.com/tags\"\n" + //$NON-NLS-1$
			"version=\"1.2\">\n" + //$NON-NLS-1$
			"...\n" + //$NON-NLS-1$
			"</jsp:root>"; //$NON-NLS-1$
	
	private TextEditor textEditor;
	private String originalEditorText;
	@Test
	public void testVerificationOfNameSpaces() throws Throwable{
		//Test open page
		openPage();
		textEditor = new TextEditor(TEST_PAGE);
		originalEditorText = textEditor.getText();
		//Test clear source
		new ShellMenu("Edit","Select All").select();
		new ShellMenu("Edit","Delete").select();
		//Test insert test text
		textEditor.setText(testText);
		textEditor.save();
		new WaitWhile(new JobIsRunning());
		performContentTestByDocument("VerificationOfNameSpaces.xml", bot.multiPageEditorByTitle(TEST_PAGE));
	
	}
	@Override
	public void tearDown() throws Exception {
		if (textEditor != null) {
			textEditor.setText(originalEditorText);
			textEditor.save();
			textEditor.close();
		}
		super.tearDown();
	}	
}

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
package org.jboss.tools.jsf.ui.bot.test.templates;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.junit.Test;

public class UnknownTemplateTest extends JSFAutoTestCase {
	@Test
	public void testUnknownTemplate() throws Throwable{
		openTestPage();
		setEditor(new TextEditor(TEST_PAGE));
		setEditorText(getEditor().getText());
		final String unknownTag = "h:unknowntag";
		getEditor().insertLine(13,"<" + unknownTag + ">" + unknownTag + "</" + "h:unknowntag>");
		getEditor().save();
		new WaitWhile(new JobIsRunning());
		assertVisualEditorContainsNodeWithValue(new InternalBrowser(),
		    unknownTag,
		    TEST_PAGE);
	}	
}

/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beans.bean.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Test;

public class NullValuesInjectionTemplate extends CDITestBase {

	protected String CDIVersion;

	// cdi1.1+
	@Test
	public void injectNullValue() {
		beansHelper.createClass("Bean1", "test");
		beansHelper.createClass("Bean2", "test");
		beansHelper.createQualifier("Qa", "test", false, false);

		editResourceUtil.replaceClassContentByResource("Bean1.java", readFile("resources/nullValuesInjection/Bean1.jav_"), false, false);		
		
		TextEditor te = new TextEditor("Bean1.java");
		EditorHasValidationMarkers cond = new EditorHasValidationMarkers(te);
		new WaitWhile(new EditorHasValidationMarkers(te));
		te.save();
		new WaitWhile(new EditorHasValidationMarkers(te));

		editResourceUtil.replaceClassContentByResource("Bean2.java", readFile("resources/nullValuesInjection/Bean2.jav_"), false, false);
		
		te = new TextEditor("Bean2.java");
		cond = new EditorHasValidationMarkers(te);
		
		if (CDIVersion.equals("1.0")) {
			new WaitUntil(cond, false);
			assertTrue("Editor hasn't validation markers.", cond.test());
			te.save();
			new WaitWhile(new JobIsRunning());
			new WaitUntil(new EditorHasValidationMarkers(te), false);
			assertTrue("Editor hasn't validation markers.", cond.test());
		} else {
			new WaitUntil(cond, false);
			new WaitWhile(cond, false);
			assertFalse("Editor has validation markers: " + cond.getResult(), cond.test());
			te.save();
			new WaitWhile(new JobIsRunning());
			new WaitUntil(cond, false);
			new WaitWhile(cond, false);
			assertFalse("Editor has validation markers: " + cond.getResult(), cond.test());
			checkOpenOn("Bean2.java", "primitiveB", "Open @Inject Bean Bean1.getB()", "Bean1.java");
			checkOpenOn("Bean2.java", "objectB", "Open @Inject Bean Bean1.getB()", "Bean1.java");
		}
	}

	private void checkOpenOn(String editor, String text, String openOn, String expectedEditor) {
		TextEditor te = new TextEditor(editor);
		te.selectText(text);
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal(openOn);
		assertEquals(expectedEditor, new TextEditor().getTitle());
	}

}

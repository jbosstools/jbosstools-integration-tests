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
package org.jboss.tools.vpe.ui.bot.test.editor.pagedesign;

import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.junit.Test;

/**
 * Tests functionality of Substituted EL Expressions tab page of Page Design
 * Options Dialog
 * 
 * @author vlado pakan
 *
 */
public class SubstitutedELExressionsTest extends PageDesignTestCase {
	@Test
	public void testSubstitutedELExressions() {
		openPage();
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Substituted EL expressions").activate();
		new PushButton("Add").click();
		new DefaultShell("Add EL Reference");
		Text txMessage = new DefaultText(2);
		Text txValue = new LabeledText("Value");
		Text txName = new LabeledText("El Name*");
		// Tests default message
		final String defaultMessage = txMessage.getText();
		final String defaultMessageStartsWith = "Add El variable";
		assertTrue(
				"Default Dialog Message has to start with '" + defaultMessageStartsWith + "' but is: " + defaultMessage,
				defaultMessage.startsWith(defaultMessageStartsWith));
		// Tests properly set Value
		final String testName = "page.consist";
		txName.setText(testName);
		assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(defaultMessage));
		// Tests incorrect name message
		txName.setText(testName + " error");
		final String invalidELNameMessage = " Invalid EL expression.";
		assertTrue("Dialog Message has to be '" + invalidELNameMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(invalidELNameMessage));
		// Tests properly set Name and Value
		txName.setText(testName);
		final String testValue = "test value";
		txValue.setText(testValue);
		assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(defaultMessage));
		new FinishButton().click();
		// Reopens dialog and tests saved values
		new DefaultShell("Page Design Options");
		new PushButton("Edit").click();
		new DefaultShell("Add EL Reference");
		txMessage = new DefaultText(2);
		txValue = new LabeledText("Value");
		txName = new LabeledText("El Name*");
		assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(defaultMessage));
		assertTrue("Value has to be'" + testValue + "' but is: " + txValue.getText(),
				txValue.getText().equals(testValue));
		assertTrue("Name value has to be'" + testName + "' but is: " + txName.getText(),
				txName.getText().equals(testName));
		new CancelButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
	}
}

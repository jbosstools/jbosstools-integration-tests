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
 * Tests functionality of Included Tag Libs tab page of Page Design Options
 * Dialog
 * 
 * @author vlado pakan
 *
 */
public class IncludedTagLibsTest extends PageDesignTestCase {
	@Test
	public void testIncludedTagLibs() {
		openPage();
		new DefaultToolItem(PAGE_DESIGN).click();
		new DefaultShell("Page Design Options");
		new DefaultTabItem("Included tag libs").activate();
		new PushButton("Add").click();
		new DefaultShell("Add Taglib Reference");
		Text txMessage = new DefaultText(2);
		Text txURI = new LabeledText("URI*");
		Text txPrefix = new LabeledText("Prefix");
		// Tests default message
		final String defaultMessage = txMessage.getText();
		final String defaultMessageStartsWith = "Add TLD definition";
		assertTrue(
				"Default Dialog Message has to start with '" + defaultMessageStartsWith + "' but is: " + defaultMessage,
				defaultMessage.startsWith(defaultMessageStartsWith));
		// Tests empty prefix message
		final String testURI = "http://java.sun.com/jsf/core";
		txURI.setText(testURI);
		final String emptyPrefixMessage = " Prefix should be set.";
		assertTrue("Dialog Message has to be '" + emptyPrefixMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(emptyPrefixMessage));
		// Tests when URI and Prefix is properly set
		final String testPrefix = "pf";
		txPrefix.setText(testPrefix);
		assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(defaultMessage));
		// Tests incorrect prefix
		final String incorrectPrefix = testPrefix + ";";
		txPrefix.setText(incorrectPrefix);
		final String incorrectPrefixMessage = " Incorrect Prefix: " + incorrectPrefix;
		assertTrue("Dialog Message has to be '" + incorrectPrefixMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(incorrectPrefixMessage));
		// Sets proper values and close dialog
		txPrefix.setText(testPrefix);
		txURI.setText(testURI);
		assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(defaultMessage));
		new FinishButton().click();
		// Reopens dialog and tests saved values
		new DefaultShell("Page Design Options");
		new PushButton("Edit").click();
		new DefaultShell("Add Taglib Reference");
		txMessage = new DefaultText(2);
		txURI = new LabeledText("URI*");
		txPrefix = new LabeledText("Prefix");
		assertTrue("Dialog Message has to be '" + defaultMessage + "' but is: " + txMessage.getText(),
				txMessage.getText().equals(defaultMessage));
		assertTrue("URI value has to be'" + testURI + "' but is: " + txURI.getText(), txURI.getText().equals(testURI));
		assertTrue("Prefix value has to be'" + testPrefix + "' but is: " + txPrefix.getText(),
				txPrefix.getText().equals(testPrefix));
		new CancelButton().click();
		new DefaultShell("Page Design Options");
		new OkButton().click();
	}
}

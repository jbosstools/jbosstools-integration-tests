/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.jface.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.common.reddeer.perspectives.JBossPerspective;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.FirstWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of data processing in wizard by clicking next and back and 
 * checking field values for downloadable cartridge, basic cartridge and quickstarts.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID307WizardDataProcessingTest {

	private final String validURL = "http://some.url";
	
	private NewOpenShift2ApplicationWizard wizard;
	
	@Before
	public void testWizardDataProcessing() {
		new JBossPerspective().reset();
		wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		assertFalse("Importing an existing application should be disabled at this moment",
				new DefaultText(0).isEnabled());
	}
	
	@Test
	public void testQuickstart() {
		new FirstWizardPage().createQuickstart(OpenShiftLabel.Cartridge.DJANGO);
		
		wizard.next();
		
		new WaitUntil(new ControlIsEnabled(new BackButton()), TimePeriod.LONG);
		
		assertTrue("Cartridge type is not shown correctly on second wizard page."
				+ new DefaultLabel(4).getText() + " is shown but should be " 
				+ OpenShiftLabel.Cartridge.DJANGO,
				new DefaultLabel(4).getText().equals(OpenShiftLabel.Cartridge.DJANGO));
		
		assertFalse("Default source code should not be used in quickstarts",
				new CheckBox(1).isChecked());

		assertFalse("Source code URL should be by default disabled.", 
				new LabeledText(OpenShiftLabel.TextLabels.SOURCE_CODE).isEnabled());
		
		assertFalse("Source code should not be empty for quickstarts",
				new LabeledText(OpenShiftLabel.TextLabels.SOURCE_CODE).getText().isEmpty());
		
		assertFalse("Check box for default source code should not be enabled in quickstarts", 
				new CheckBox(1).isEnabled());
		
		new CancelButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
	}

	@Test
	public void testBasicCartridge() {
		new FirstWizardPage().createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.JBOSS_EAP);	
		
		wizard.next();
		
		new WaitUntil(new ControlIsEnabled(new BackButton()), TimePeriod.LONG);
		
		assertTrue("Cartridge type is not shown correctly on second wizard page."
				+ new DefaultLabel(4).getText() + " is shown but should be " 
				+ OpenShiftLabel.Cartridge.JBOSS_EAP,
				new DefaultLabel(4).getText().equals(OpenShiftLabel.Cartridge.JBOSS_EAP));
		
		new PushButton(OpenShiftLabel.Button.ADVANCED_OPEN).click();
		
		assertTrue("Default source code should be used by default", 
				new CheckBox(1).isChecked());
		
		assertFalse("Source code URL should be by default disabled.", 
				new LabeledText(OpenShiftLabel.TextLabels.SOURCE_CODE).isEnabled());
		
		new CheckBox(1).click();
		
		assertTrue("Source code URL should be enabled at this moment.",
				new LabeledText(OpenShiftLabel.TextLabels.SOURCE_CODE).isEnabled());
		
		wizard.back();
		
		new CancelButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
	}

	@Test
	public void testDownloadableCartridge() {
		TreeViewerHandler treeViewer = TreeViewerHandler.getInstance();
		treeViewer.getTreeItem(new DefaultTree(), 
				OpenShiftLabel.Cartridge.DOWNLOADABLE_CARTRIDGE).select();
		
		assertFalse("Next button should be disable if no URL is inserted for "
				+ "downloadable cartridge", new NextButton().isEnabled());
		
		new LabeledText(OpenShiftLabel.TextLabels.CARTRIDGE_URL).setText(validURL);
		
		try {	
			wizard.next();
		} catch (WaitTimeoutExpiredException ex) {
			fail("Next button has not been enabled after setting a valid URL");
		}
		
		new WaitUntil(new ControlIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new PushButton(OpenShiftLabel.Button.ADVANCED_OPEN).click();
		
		assertTrue("Default source code should be used by default", 
				new CheckBox(1).isChecked());
		
		assertFalse("Source code URL should be by default disabled.", 
				new LabeledText(OpenShiftLabel.TextLabels.SOURCE_CODE).isEnabled());
		
		new CheckBox(1).click();
		
		assertTrue("Source code URL should be enabled at this moment.",
				new LabeledText(OpenShiftLabel.TextLabels.SOURCE_CODE).isEnabled());
		
		wizard.back();
		
		assertTrue("URL has been removed after going back in wizard although"
				+ " it should be remembered",
				validURL.equals(new LabeledText(OpenShiftLabel.TextLabels.CARTRIDGE_URL).getText()));
		
		new CancelButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
	}
}

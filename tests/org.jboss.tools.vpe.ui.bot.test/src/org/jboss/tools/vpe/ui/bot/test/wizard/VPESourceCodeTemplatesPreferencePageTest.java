/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * This test class open vpe preference page Window->Preferences->JBoss
 * Tools->Web->Editors->Visual Page Editor->Templates
 * 
 * @author mareshkau
 * 
 */
public class VPESourceCodeTemplatesPreferencePageTest extends VPEAutoTestCase {
	// just open a VPE Source Code templates preference test page
	@Test
	public void testSourceCodeTemplatesPreferencePage() {
		VisualPageEditorPreferencePage vpePreferencePage = new VisualPageEditorPreferencePage();
		WorkbenchPreferenceDialog preferenceDialog = null;
		preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		preferenceDialog.select(vpePreferencePage);
		vpePreferencePage.activateVisualTemplatesTab();
		try {
			new PushButton("Add...").click();
			new DefaultShell("User specified tag template");
			new CancelButton().click();
		} catch (CoreLayerException cle) {
			fail("Preference Page has not been created" + cle);
		} finally {
			new DefaultShell("Preferences");
			new OkButton().click();
		}
	}
}

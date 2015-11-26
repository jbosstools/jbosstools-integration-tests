/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.ui.bot.test.bower;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.jst.reddeer.ui.preferences.BowerPreferencesPage;
import org.junit.Test;

/**
 * Tests for Bower Preferences 
 * @author Pavol Srna
 *
 */
public class BowerPreferencesTest extends BowerTestBase {

	public static String INVALID_NODE_PATH = "/usr/invalid";
	public static String INVALID_BOWER_PATH = "/usr/invalid";
	
	@Test
	public void testNodeLocationNotValid() {
	
		WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
		BowerPreferencesPage preferencesPage = new BowerPreferencesPage();
		preferencesDialog.open();
		preferencesDialog.select(preferencesPage);
		
		assertTrue("OK Button is not enabled!", preferencesPage.getOkBtn().isEnabled());
		preferencesPage.getNodeLocation().typeText(INVALID_NODE_PATH);
		assertFalse("OK Button is enabled!", preferencesPage.getOkBtn().isEnabled());
		
		preferencesDialog.cancel();
		
	}
	
	@Test
	public void testBowerLocationNotValid() {
		
		WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
		BowerPreferencesPage preferencesPage = new BowerPreferencesPage();
		preferencesDialog.open();
		preferencesDialog.select(preferencesPage);
		
		assertTrue("OK Button is not enabled!", preferencesPage.getOkBtn().isEnabled());
		preferencesPage.getBowerLocation().typeText(INVALID_BOWER_PATH);
		assertFalse("OK Button is enabled!", preferencesPage.getOkBtn().isEnabled());
		
		preferencesDialog.cancel();		
	}

}

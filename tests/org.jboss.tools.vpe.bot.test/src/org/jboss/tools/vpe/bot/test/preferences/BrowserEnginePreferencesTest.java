/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.bot.test.preferences;

import static org.junit.Assert.*;

import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.junit.Test;

public class BrowserEnginePreferencesTest extends VPETestBase{
	
	@Test
	public void testDefaultBrowserEngine(){
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		VisualPageEditorPreferencePage vp = new VisualPageEditorPreferencePage();
		wd.select(vp);
		assertTrue(vp.isHTML5Engine());
		assertFalse(vp.isJSFEngine());
		wd.ok();
	}
	
	@Test
	public void testBrowserEngineRestartPreferences(){
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		VisualPageEditorPreferencePage vp = new VisualPageEditorPreferencePage();
		wd.select(vp);
		if(isGTK2()){
			testGTK2EnginePreferences(wd,vp);
		} else {
			testGTK3EnginePreferences(wd,vp);
		}
	}
	
	private void testGTK3EnginePreferences(WorkbenchPreferenceDialog wd, VisualPageEditorPreferencePage vp){
		assertFalse(new RadioButton("HTML5 (use WebKit)").isEnabled());
		assertFalse(new RadioButton("JSF (use XulRunner)").isEnabled());
		new DefaultLabel("Switching Visual Editor engine is not available under "
				+ "GTK3 or when embedded XULRunner is disabled");
	}
	
	private void testGTK2EnginePreferences(WorkbenchPreferenceDialog wd, VisualPageEditorPreferencePage vp){
		if(vp.isHTML5Engine()){
			vp.setJSFEngine();
		} else {
			vp.setHTML5Engine();
		}		
		vp.apply();
		new DefaultShell("Confirm restart");
		new CancelButton().click();
		wd.open();
		new OkButton().click();
		new DefaultShell("Confirm restart");
		new CancelButton().click();
		wd.open();
		wd.cancel();
	}
		

	
	

}

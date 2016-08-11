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

import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.junit.BeforeClass;
import org.junit.Test;


//add tests for restore defaults button
public class VpvPreferencesTest extends VPETestBase{

	private static IPreferenceStore preferences;
	private VisualPageEditorPreferencePage vp = new VisualPageEditorPreferencePage();

	@BeforeClass
	public static void getPreferences() {
		preferences = WebUiPlugin.getDefault().getPreferenceStore();
		createWebProject();
		createHTMLPageWithJS();
	}

	@Test
	public void testEditorSplittingPreferences() {
		Consumer<String> c = (x) -> vp.setVisualSourceEditorsSplitting(x);
		Function<String, Integer> f = (x) -> preferences.getInt(x);
		
		setPreferenceObj(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING, 
				c, "Vertical splitting with Visual Editor on the top", f, 2);
		
		setPreferenceObj(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING, 
				c, "Vertical splitting with Source Editor on the top", f, 1);
		
		setPreferenceObj(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING, 
				c, "Horizontal splitting with Visual Editor to the left", f, 4);
		
		setPreferenceObj(IVpePreferencesPage.VISUAL_SOURCE_EDITORS_SPLITTING, 
				c, "Horizontal splitting with Source Editor to the left", f, 3);

	}
	
	@Test
	public void testDefaultEditorTab(){
		Consumer<String> c = (x) -> vp.setDefaultActiveEditorTab(x);
		Function<String, Integer> f = (x) -> preferences.getInt(x);
		
		setPreferenceObj(IVpePreferencesPage.DEFAULT_VPE_TAB, c, "Source", f, 1);
		setPreferenceObj(IVpePreferencesPage.DEFAULT_VPE_TAB, c, "Preview", f, 2);
		setPreferenceObj(IVpePreferencesPage.DEFAULT_VPE_TAB, c, "Visual/Source", f, 0);
	}
	
	@Test
	public void testSizeOfPane(){

	}
	
	@Test
	public void testSynchonizeScrolling(){
		Consumer<Boolean> c = (x) -> vp.toggleSynchronizeScrolling(x);
		testBoolenPreferences(IVpePreferencesPage.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES, c);
	}
	
	@Test
	public void testAskForTagAttributes(){
		Consumer<Boolean> c = (x) -> vp.toggleAskForAttrsDuringTagInsert(x);
		testBoolenPreferences(IVpePreferencesPage.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT, c);
		
	}
	
	@Test
	public void testInformIfProjectIsNotConfiguredProperly(){
		Consumer<Boolean> c = (x) -> vp.toggleInformIfProjectIsNotCofiguredProperly(x);		
		testBoolenPreferences(IVpePreferencesPage.INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE, c);
	}
	
	
	@Test
	public void testToggleShowResourceBundlesAsELExp(){
		Consumer<Boolean> c = (x) -> vp.toggleShowResourceBundlesAsELExp(x);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL, c);
	}
	
	@Test
	public void testShowTextFormattingBar(){
		Consumer<Boolean> c = (x) -> vp.toggleShowTextFormattingBar(x);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_TEXT_FORMATTING, c);
	}
	
	@Test
	public void testShowSelectionTagBar(){
		Consumer<Boolean> c = (x) -> vp.toggleShowSelectionTagBar(x);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_SELECTION_TAG_BAR, c);
	}
	
	@Test
	public void testShowNonVisualTag(){
		Consumer<Boolean> c = (x) -> vp.toggleShowNonVisualTag(x);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_NON_VISUAL_TAGS, c);
	}
	
	@Test
	public void testShowBorderForUnknownTags(){
		Consumer<Boolean> c = (x) -> vp.toggleShowBorderForUnknownTags(x);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS, c);
	}
	
	
	private void testBoolenPreferences(String preferenceId, Consumer c){
		Function<String, Boolean> f = (x) -> preferences.getBoolean(x);
		setPreferenceObj(preferenceId, c, true, f, true);
		setPreferenceObj(preferenceId, c, false, f, false);
	}
	
	private void setPreferenceObj(String preferenceId, Consumer c, Object accept, Function pref, Object expected){
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		VisualPageEditorPreferencePage vp = new VisualPageEditorPreferencePage();
		wd.select(vp);
		c.accept(accept);
		wd.ok();
		assertEquals(expected, pref.apply(preferenceId));
	}
	
	
	
}

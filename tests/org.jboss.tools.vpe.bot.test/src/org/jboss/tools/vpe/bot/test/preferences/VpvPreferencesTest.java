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

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.jst.web.ui.WebUiPlugin;
import org.jboss.tools.jst.web.ui.internal.editor.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.bot.test.VPETestBase;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
import org.junit.BeforeClass;
import org.junit.Test;


//add tests for restore defaults button
public class VpvPreferencesTest extends VPETestBase{

	private static IPreferenceStore preferences;

	@BeforeClass
	public static void getPreferences() {
		preferences = WebUiPlugin.getDefault().getPreferenceStore();
		createWebProject();
		createHTMLPageWithJS();
	}

	@Test
	public void testEditorSplittingPreferences() {
		BiConsumer<VisualPageEditorPreferencePage, String> c = (x,y) -> x.setVisualSourceEditorsSplitting(y);
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
		BiConsumer<VisualPageEditorPreferencePage, String> c = (x,y) -> x.setDefaultActiveEditorTab(y);
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
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleSynchronizeScrolling(y);
		testBoolenPreferences(IVpePreferencesPage.SYNCHRONIZE_SCROLLING_BETWEEN_SOURCE_VISUAL_PANES, c);
	}
	
	@Test
	public void testAskForTagAttributes(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleAskForAttrsDuringTagInsert(y);
		testBoolenPreferences(IVpePreferencesPage.ASK_TAG_ATTRIBUTES_ON_TAG_INSERT, c);
		
	}
	
	@Test
	public void testInformIfProjectIsNotConfiguredProperly(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleInformIfProjectIsNotCofiguredProperly(y);		
		testBoolenPreferences(IVpePreferencesPage.INFORM_WHEN_PROJECT_MIGHT_NOT_BE_CONFIGURED_PROPERLY_FOR_VPE, c);
	}
	
	
	@Test
	public void testToggleShowResourceBundlesAsELExp(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleShowResourceBundlesAsELExp(y);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_RESOURCE_BUNDLES_USAGE_AS_EL, c);
	}
	
	@Test
	public void testShowTextFormattingBar(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleShowTextFormattingBar(y);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_TEXT_FORMATTING, c);
	}
	
	@Test
	public void testShowSelectionTagBar(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleShowSelectionTagBar(y);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_SELECTION_TAG_BAR, c);
	}
	
	@Test
	public void testShowNonVisualTag(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleShowNonVisualTag(y);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_NON_VISUAL_TAGS, c);
	}
	
	@Test
	public void testShowBorderForUnknownTags(){
		BiConsumer<VisualPageEditorPreferencePage, Boolean> c = (x,y) -> x.toggleShowBorderForUnknownTags(y);		
		testBoolenPreferences(IVpePreferencesPage.SHOW_BORDER_FOR_UNKNOWN_TAGS, c);
	}
	
	
	private void testBoolenPreferences(String preferenceId, BiConsumer c){
		Function<String, Boolean> f = (x) -> preferences.getBoolean(x);
		setPreferenceObj(preferenceId, c, true, f, true);
		setPreferenceObj(preferenceId, c, false, f, false);
	}
	
	private void setPreferenceObj(String preferenceId, BiConsumer c, Object accept, Function pref, Object expected){
		WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
		wd.open();
		VisualPageEditorPreferencePage vp = new VisualPageEditorPreferencePage(wd);
		wd.select(vp);
		c.accept(vp,accept);
		wd.ok();
		assertEquals(expected, pref.apply(preferenceId));
	}
	
	
	
}
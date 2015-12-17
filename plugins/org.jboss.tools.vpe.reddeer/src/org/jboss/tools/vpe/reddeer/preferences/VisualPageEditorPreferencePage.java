/*******************************************************************************
 * Copyright (c) 2015 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.preferences;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
/**
 * RedDeer model of JBoss Tools > Web > Editors > Visual Page Editor preference page
 * 
 * @author vlado pakan
 */
public class VisualPageEditorPreferencePage extends PreferencePage  {
	
	protected final static Logger log = Logger.getLogger(VisualPageEditorPreferencePage.class);
	
	public VisualPageEditorPreferencePage() {
		super("JBoss Tools", "Web" , "Editors" , "Visual Page Editor");
	}
	/**
	 * Sets Do Not Show Browser Engine Dialog
	 * @param checked
	 */
	public void setDoNotShowBrowserEngineDialog (boolean checked){
		log.debug("Set Do not show Browser Engine dialog to: " + checked);
		new CheckBox("Do not show Browser Engine dialog").toggle(checked);
	}
	/**
	 * Sets default active editor tab
	 * @param activeTab
	 */
	public void setDefaultActiveEditorTab (String activeTab){
		log.debug("Set default active editor tab to: " + activeTab);
		new LabeledCombo("Select the default active editor's tab").setSelection(activeTab);
	}
	/**
	 * Toggles Show border for unknown tags
	 * @param checked
	 */
	public void toggleShowBorderForUnknownTags (boolean checked){
		log.debug("Toggle Show border for unknown tags to: " + checked);
		getShowBorderForUnknownTags().toggle(checked);
	}
	/**
	 * Returns Show border for unknown tags checkbox status
	 * @return
	 */
	public boolean isShowBorderForUnknownTags (){
		return getShowBorderForUnknownTags().isChecked();
	}
	
	private CheckBox getShowBorderForUnknownTags () {
		return new CheckBox("Show border for unknown tags");
	}
	/**
	 * Toggles Show resource bundles usage as EL expressions
	 * @param checked
	 */
	public void toggleShowResourceBundlesAsELExp (boolean checked){
		log.debug("Toggle Show resource bundles usage as EL expressions to: " + checked);
		getShowResourceBundlesAsELExp().toggle(checked);
	}
	/**
	 * Returns Show resource bundles usage as EL expressions checkbox status
	 * @return
	 */
	public boolean isShowResourceBundlesAsELExp (){
		return getShowResourceBundlesAsELExp().isChecked();
	}
	
	private CheckBox getShowResourceBundlesAsELExp () {
		return new CheckBox("Show resource bundles usage as EL expressions");
	}
	
	/**
	 * Toggles Show selection tag bar
	 * @param checked
	 */
	public void toggleShowSelectionTagBar (boolean checked){
		log.debug("Toggle Show selection tag bar to: " + checked);
		getShowSelectionTagBar().toggle(checked);
	}
	/**
	 * Returns Show selection tag bar checkbox status
	 * @return
	 */
	public boolean isShowSelectionTagBar (){
		return getShowSelectionTagBar().isChecked();
	}
	
	private CheckBox getShowSelectionTagBar () {
		return new CheckBox("Show selection tag bar");
	}
	/**
	 * Toggles Show non-visual tags
	 * @param checked
	 */
	public void toggleShowNonVisualTag (boolean checked){
		log.debug("Toggle Show non-visual tags to: " + checked);
		getShowNonVisualTag().toggle(checked);
	}
	/**
	 * Returns Show non-visual tags checkbox status
	 * @return
	 */
	public boolean isShowNonVisualTag (){
		return getShowNonVisualTag().isChecked();
	}
	
	private CheckBox getShowNonVisualTag () {
		return new CheckBox("Show non-visual tags");
	}
	
	/**
	 * Toggles Ask for tag attributes during tag insert
	 * @param checked
	 */
	public void toggleAskForAttrsDuringTagInsert (boolean checked){
		log.debug("Toggle Ask for tag attributes during tag insert to: " + checked);
		getAskForAttrsDuringTagInsert().toggle(checked);
	}
	/**
	 * Returns Ask for tag attributes during tag insert checkbox status
	 * @return
	 */
	public boolean isAskForAttrsDuringTagInsert (){
		return getAskForAttrsDuringTagInsert().isChecked();
	}
	
	private CheckBox getAskForAttrsDuringTagInsert () {
		return new CheckBox("Ask for tag attributes during tag insert");
	}
	
	/**
	 * Toggles Show text formatting bar
	 * @param checked
	 */
	public void toggleShowTextFormattingBar (boolean checked){
		log.debug("Toggle Show text formatting bar to: " + checked);
		getShowTextFormattingBar().toggle(checked);
	}
	/**
	 * Returns Show text formatting bar checkbox status
	 * @return
	 */
	public boolean isShowTextFormattingBar (){
		return getShowTextFormattingBar().isChecked();
	}
	
	private CheckBox getShowTextFormattingBar() {
		return new CheckBox("Show text formatting bar");
	}
	
	/**
	 * Sets default active editor tab
	 * @param splitting
	 */
	public void seVisualSourceEditorsSplitting (String splitting){
		log.debug("Set Visual/Source editors splitting to: " + splitting);
		new LabeledCombo("Visual/Source editors splitting").setSelection(splitting);
	}
	/**
	 * Activates Visual Templates tab
	 */
	public void activateVisualTemplatesTab (){
		new DefaultTabItem("Visual Templates").activate();
	}
	/**
	 * Activates General tab
	 */
	public void activateGeneralTab (){
		new DefaultTabItem("Visual Templates").activate();
	}
}

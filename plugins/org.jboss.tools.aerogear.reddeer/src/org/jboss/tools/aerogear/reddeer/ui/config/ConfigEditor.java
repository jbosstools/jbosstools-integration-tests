/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.ui.config;

import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

/**
 * RedDeer implementation of Hybrid Mobile Config Editor
 * 
 * @author Vlado Pakan
 * 
 */
public class ConfigEditor extends AbstractEditor {

	private static final String OVERVIEW_TAB_ITEM = "Overview";
	private static final String PLATFORM_PROPERTIES_TAB_ITEM = "Platform Properties";
	private static final String CONFIG_XML_TAB_ITEM = "config.xml";

	public ConfigEditor(String title) {
		super(title);
	}

	/**
	 * Adds plugin with pluginId to configuration
	 * 
	 * @param pluginId
	 */
	public void addPlugin(String pluginId) {
		openPluginDiscoveryDialog();
		// Filter for pluginId
		new DefaultText(0).setText(pluginId);
		KeyboardFactory.getKeyboard().type(SWT.CR);
		KeyboardFactory.getKeyboard().type(SWT.LF);
		new WaitUntil(new LabelWithTextHasPosition(pluginId , 2));
		// check plugin to add
		new CheckBox(1).click();
		// Wait for this text displayed
		new DefaultText("Discover and Install Cordova Plug-ins");
		new NextButton().click();
		// Check proper plugin was selected
		new DefaultLabel(pluginId);
		assertTrue("There is no version available for plugin " + pluginId,
			new DefaultCombo().getItems().size() > 0);
		new BackButton().click();
		new FinishButton().click();
		try{
			new DefaultShell("Overwrite Files");
			new PushButton("Yes To All").click();
		} catch (SWTLayerException swtle){
			// Do nothing shell was not displayed
		}
		
	}
	/**
	 * Opens Plugin Discovery dialog
	 * 
	 * @param pluginId
	 */
	public void openPluginDiscoveryDialog() {
		activate();
		new DefaultCTabItem(ConfigEditor.PLATFORM_PROPERTIES_TAB_ITEM).activate();
		new PushButton("Add...").click();
		new DefaultShell("Cordova Plug-in Discovery");
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		// Wait until some plugin is displayed within dialog
		new DefaultLabel(10);
	}
	/**
	 * Fulfilled when Label with text has specified position
	 * @author Vlado Pakan
	 *
	 */
	private class LabelWithTextHasPosition implements WaitCondition{
		private String text;
		private int position;
		
		public LabelWithTextHasPosition (String text , int position){
			this.text = text;
			this.position = position;
		}
		
		@Override
		public boolean test() {
			String indexedLabelText = new DefaultLabel(position).getText();
			return indexedLabelText.equals(text);
		}
		@Override
		public String description() {
			return "Checkin if label with text='" + text + "' has postion=" + position;
		}
	}	
}



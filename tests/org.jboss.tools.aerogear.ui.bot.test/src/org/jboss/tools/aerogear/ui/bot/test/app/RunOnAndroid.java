/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.app;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.aerogear.ui.bot.test.utils.AndroidDevelopmentTools;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Require(clearWorkspace = true)
public class RunOnAndroid extends AerogearBotTest {
  @BeforeClass 
  public static void setAndroidSDKPreference(){
    // sets mandatory preferences
    open.preferenceOpen(ActionItem.Preference.Android.LABEL);
    bot.textWithLabel("SDK Location:")
      .setText(AndroidDevelopmentTools.getAndoridSDKLocation());
    bot.button(IDELabel.Button.OK).click();
    open.preferenceOpen(ActionItem.Preference.HybridMobile.LABEL);
    bot.textWithLabel("Android SDK Directory:").setText("");
    bot.textWithLabel("Android SDK Directory:")
      .typeText(AndroidDevelopmentTools.getAndoridSDKLocation());
    bot.button(IDELabel.Button.OK).click();
  }
	@Test
	public void canRunOnAndroidEmulator() {
	  projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
		SWTBotShell activeShell = bot.activeShell();
		runTreeItemInAndroidEmulator(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
		activeShell.setFocus();
		assertTrue("Not exactly one instance of Android Emulator is running", AndroidDevelopmentTools.getRunningEmulators().size() == 1);
		bot.sleep(2*TIME_60S);
		// Second run should not start new Emulator instance
		runTreeItemInAndroidEmulator(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
		activeShell.setFocus();
    assertTrue("Not exactly one instance of Android Emulator is running", AndroidDevelopmentTools.getRunningEmulators().size() == 1);
	}

	@Test @Ignore
	public void canRunOnAndroidDevice() {
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);

		runTreeItemOnAndroidDevice(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
	}
	@Override
	public void tearDown(){
	  AndroidDevelopmentTools.killRunningEmulators();	  
	  super.tearDown();
	}
}

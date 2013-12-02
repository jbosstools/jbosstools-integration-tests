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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.aerogear.ui.bot.test.utils.AndroidDevelopmentTools;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ConsoleView;
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
	  projectExplorer.selectProject(AerogearBotTest.CORDOVA_PROJECT_NAME);
		SWTBotShell activeShell = bot.activeShell();
		runTreeItemInAndroidEmulator(bot.tree().expandNode(AerogearBotTest.CORDOVA_PROJECT_NAME));
		activeShell.setFocus();
		assertTrue("Not exactly one instance of Android Emulator is running", AndroidDevelopmentTools.getRunningEmulators().size() == 1);
		bot.sleep(2*TIME_60S);
		// Second run should not start new Emulator instance
		runTreeItemInAndroidEmulator(bot.tree().expandNode(AerogearBotTest.CORDOVA_PROJECT_NAME));
		activeShell.setFocus();
    assertTrue("Not exactly one instance of Android Emulator is running", AndroidDevelopmentTools.getRunningEmulators().size() == 1);
	}
	@Test
  public void testChangesDeployment() {
	  projectExplorer.openFile(CORDOVA_PROJECT_NAME,"www","js","index.js");
	  SWTBotEclipseEditor jsEditor = bot.editorByTitle("index.js").toTextEditor();
	  String editorText = jsEditor.getText();
	  final String firstVersion = "FIRST_VERSION_OF_JS_XYZCVDF1";
	  editorText = editorText.replace("app.report('deviceready');", "app.report('deviceready');\nconsole.log('" 
	      +  firstVersion
	      + "');");
	  jsEditor.setText(editorText);
	  jsEditor.save();
	  projectExplorer.selectProject(CORDOVA_PROJECT_NAME);
    ConsoleView consoleView = new ConsoleView();
    consoleView.show();
    consoleView.clearConsole();
    SWTBotShell activeShell = bot.activeShell();
	  setLogCatFilterPropsAndRun(AerogearBotTest.CORDOVA_PROJECT_NAME);
    activeShell.setFocus();
    // because Emulator instability try to run application one more time
    if (!consoleView.getConsoleText().contains(firstVersion)){
      setLogCatFilterPropsAndRun(AerogearBotTest.CORDOVA_PROJECT_NAME);
      activeShell.setFocus();
      assertTrue("Cordova app was not successfully deployed to Android Emulator",
          consoleView.getConsoleText().contains(firstVersion));
    }
    final String secondVersion = "SECOND_VERSION_OF_JS_XYZCVDF1";
    jsEditor.setText(editorText.replace(firstVersion, secondVersion));
    jsEditor.save();
    consoleView.clearConsole();
    // Second run with updated js file
    setLogCatFilterPropsAndRun(AerogearBotTest.CORDOVA_PROJECT_NAME);
    activeShell.setFocus();
    assertTrue("Updated Cordova app was not successfully deployed to Android Emulator",
        consoleView.getConsoleText().contains(secondVersion));
  }
	@Test @Ignore
	public void canRunOnAndroidDevice() {
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);

		runTreeItemOnAndroidDevice(bot.tree().expandNode(CORDOVA_PROJECT_NAME));
	}
	@Override
	public void tearDown(){
	  try{
	    bot.editorByTitle(AerogearBotTest.CORDOVA_APP_NAME).close();
	  } catch (WidgetNotFoundException wnfe){
	    // do nothing
	  }
	  try{
      bot.editorByTitle("index.js").close();
    } catch (WidgetNotFoundException wnfe){
      // do nothing
    }
	  try{
      bot.editorByTitle("config.xml").close();
    } catch (WidgetNotFoundException wnfe){
      // do nothing
    }
	  AndroidDevelopmentTools.killRunningEmulators();	  
	  super.tearDown();
	}
}

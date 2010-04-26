package org.jboss.tools.ui.bot.ext.helper;

import java.awt.AWTException;
import java.awt.Robot;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;

public class KeyboardHelper {
  private static Robot robot = null;
  /**
   * Simulate pressing of key with keyCode via SWT
   * @param display
   * @param keyCode
   */
  public static void pressKeyCode (Display display , int keyCode){
    
    Event keyPressed = new Event();
    keyPressed.keyCode = keyCode;   
    keyPressed.type = SWT.KeyDown;
    display.post(keyPressed);
    Event keyReleased = new Event();
      keyReleased.keyCode = keyCode;   
      keyReleased.type = SWT.KeyUp;
    display.post(keyReleased);
    
  }
  /**
   * Simulate pressing of key with keyCode via AWT
   * @param awtKeyCode
   */
  public static void pressKeyCodeUsingAWT (int awtKeyCode){
    try {
      if (KeyboardHelper.robot == null){
        SWTBotPreferences.KEYBOARD_STRATEGY = "org.eclipse.swtbot.swt.finder.keyboard.AWTKeyboardStrategy";
        KeyboardHelper.robot = new Robot();
      }
      KeyboardHelper.robot.keyPress(awtKeyCode);
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * Simulate releasing of key with keyCode via AWT
   * @param awtKeyCode
   */
  public static void releaseKeyCodeUsingAWT (int awtKeyCode){
    try {
      if (KeyboardHelper.robot == null){
        SWTBotPreferences.KEYBOARD_STRATEGY = "org.eclipse.swtbot.swt.finder.keyboard.AWTKeyboardStrategy";
        KeyboardHelper.robot = new Robot();
      }
      KeyboardHelper.robot.keyRelease(awtKeyCode);
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }
}

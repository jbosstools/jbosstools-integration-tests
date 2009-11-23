package org.jboss.tools.ui.bot.ext.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class KeyboardHelper {
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
}

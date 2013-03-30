package org.jboss.tools.bpmn2.ui.awt;

import java.awt.AWTException;
import java.awt.Robot;

/**
 * See: http://undocumentedmatlab.com/blog/gui-automation-robot/
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class AWTBot {
    
    Robot robot;

    int delay;
    
    /**
     * Creates a new instance of RobotExt.
     * 
     * @throws Exception 
     */
    public AWTBot() {
        this(100);
    }
    
    /**
     * Creates a new instance of RobotExt.
     * 
     * @param delay
     * @param driver 
     */
    public AWTBot(int delay) {
    	try {
    		this.delay = delay;
            this.robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException("Initialization failed.", e);
		}
    }
    
    /**
     * Returns the wrapped java.awt.Robot instance.
     * 
     * @return 
     */
    public Robot getRobot() {
        return robot;
    }
    
    /**
     * Type a key combination. E.g. CTRL+SHIFT+A
     * 
     * @param keycodes 
     */
    public void typeKeyCombo(int... keycodes) {
        // press from 1..n
        for (int i = 0; i <= keycodes.length - 1; i++) {
            robot.keyPress(keycodes[i]);
        }
        // release from n..1
        for (int i = keycodes.length - 1; i >= 0; i--) {
            robot.keyRelease(keycodes[i]);
        }
        robot.delay(delay);
    }
    
    /**
     * Type several key codes.
     * 
     * @param keycodes 
     */
    public void type(int... keycodes) {
        for (int k : keycodes) {
            robot.keyPress(k);
            robot.keyRelease(k);
            robot.delay(delay);
        }
    }
    
}

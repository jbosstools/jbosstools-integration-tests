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
package org.jboss.tools.jst.ui.bot.test.nodejs;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.ExplorerItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.Assert;

import static org.junit.Assert.assertTrue;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;

/**
 * Tests for NodeJS Launchers
 *
 * @author Pavol Srna
 *
 */
@OpenPerspective(DebugPerspective.class)
public class NodeJSLauncherTest extends JSTTestBase {
    
    private static String TEST_APP_NAME = "jsdt-node-test-project";
    private static String IMPORT_PATH = "resources/" + TEST_APP_NAME;
    
    private static int APP_PORT = 3000;
    private static int DEBUG_PORT = 5858;
    
    private NodeJSLaunchListener launchListener = null;
    
    @BeforeClass
    public static void prepare() {
        /* PE is closed in Debug perspective, open it */
        new ProjectExplorer().open();
        importExistingProject(IMPORT_PATH);
        assertTrue(TEST_APP_NAME + " has not been imported!", new ProjectExplorer().containsProject(TEST_APP_NAME));
        npmInstall(TEST_APP_NAME);
    }
    
    @AfterClass
    public static void cleanup() {
        ShellHandler.getInstance().closeAllNonWorbenchShells();
        new ProjectExplorer().deleteAllProjects();
    }
    
    @Test
    public void testNodeJSRunAsLauncherAvailable() {
        ExplorerItem indexJS = new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("index.js");
        indexJS.select();
        assertTrue("'Run As -> Node.js Application' not available!", runAsNodeJSAppMenu().isEnabled());
    }
    
    @Test
    public void testNodeJSAppIsRunning() {
        assertTrue(portAvailable(APP_PORT));
    	
        launchListener = new NodeJSLaunchListener();
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        manager.addLaunchListener(launchListener);
        
        ExplorerItem indexJS = new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("index.js");
        indexJS.select();
        runAsNodeJSAppMenu().select();
        
        ConsoleView console = new ConsoleView();
        console.activate();
        try {
            new WaitUntil(new ConsoleHasText("Listening on port 3000"));
        } catch (WaitTimeoutExpiredException e) {
            Assert.fail("Node.js App is not running!");
        }
    }
    
    @Test
    public void testNodeJSDebugAsLauncherAvailable() {
        assertTrue("'Debug As -> Node.js Application' not available!",
                   debugAsNodeJSAppMenu(new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("index.js"))
                   .isEnabled());
    }
    
    @Test
    public void testNodeJSAppIsDebugging() {
    	assertTrue(portAvailable(APP_PORT));
    	assertTrue(portAvailable(DEBUG_PORT));
    	
        launchListener = new NodeJSLaunchListener();
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        manager.addLaunchListener(launchListener);
        
        debugAsNodeJSAppMenu(new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("index.js")).select();
        
        ConsoleView console = new ConsoleView();
        console.activate();
        new WaitUntil(new ConsoleHasText("Debugger listening"), TimePeriod.LONG);
        
        assertTrue("Node.js App is not debugging!", console.getConsoleText().contains("Debugger listening"));
    }
    
    @After
    public void terminate(){
    	if(launchListener != null){
    		ILaunch launch = launchListener.getNodeJSLaunch();
    		if(launch != null){
    			terminatePrcs(launch.getProcesses());
    		}
            ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
            manager.removeLaunchListener(launchListener);
    	}
    }
}

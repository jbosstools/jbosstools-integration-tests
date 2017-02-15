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

import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.jst.reddeer.common.CursorPositionIsOnLine;
import org.jboss.tools.jst.reddeer.common.TreeContainsItem;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Tests for NodeJS Debugger
 *
 * @author Pavol Srna
 *
 */
@OpenPerspective(DebugPerspective.class)
public class NodeJSDebuggerTest extends JSTTestBase {
    
    private static String TEST_APP_NAME = "jsdt-node-test-project";
    private static String IMPORT_PATH = "resources/" + TEST_APP_NAME;
    
    private static int BOOK_JS_BREAKPOINT_LINE = 4;
    
    private static String DEBUG_VARIABLE_EXPRESS = "express";
    private static String DEBUG_VARIABLE_PORT = "port";
    private static String DEBUG_VARIABLE_TITLE = "title";
    private static String DEBUG_VARIABLE_TITLE_VALUE = "\"Nineteen Eighty-Four\"";
    private static String DEBUG_VARIABLE_AUTHOR = "author";
    private static String DEBUG_VARIABLE_AUTHOR_VALUE = "\"George Orwell\"";
    
    private static int APP_PORT = 3000;
    private static int DEBUG_PORT = 5858;
    
    private static NodeJSLaunchListener launchListener = null;
    
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
    
    @Before
    public void debugAs() {
        assertTrue(portAvailable(APP_PORT));
        assertTrue(portAvailable(DEBUG_PORT));
    	
        launchListener = new NodeJSLaunchListener();
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        manager.addLaunchListener(launchListener);
        
        debugAsNodeJSAppMenu(new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("index.js"))
        .select();
        
        new WorkbenchView("Debug").open();
        DefaultTree debugTree = new DefaultTree();
        new WaitUntil(
                      new TreeContainsItem(debugTree, new RegexMatcher("\\(anonymous function\\)(.*)(index\\.js)(.*)")),
                      TimePeriod.LONG);
        
        RegexMatcher matcher = new RegexMatcher("\\(anonymous function\\)(.*)(index\\.js)(.*)");
        doubleClickTreeItem(debugTree, matcher);
        
        ConsoleView console = new ConsoleView();
        console.open();
        new WaitUntil(new ConsoleHasText("Debugger listening"), TimePeriod.LONG);
        
    }
    
    @After
    public void terminate() {
        terminatePrcs(launchListener.getNodeJSLaunch().getProcesses());
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        manager.removeLaunchListener(launchListener);
    }
    
    @Test
    public void testJSVariablesAvailableInView() {
        
        TreeItem varExpress = getVariable(DEBUG_VARIABLE_EXPRESS);
        TreeItem varPort = getVariable(DEBUG_VARIABLE_PORT);
        
        assertTrue("Variable '" + DEBUG_VARIABLE_EXPRESS + "' not found in view!", varExpress != null);
        assertThat(varExpress.getCell(0), is(DEBUG_VARIABLE_EXPRESS));
        assertThat(varExpress.getCell(1), is("undefined"));
        
        assertTrue("Variable '" + DEBUG_VARIABLE_PORT + "' not found in view!", varPort != null);
        assertThat(varPort.getCell(0), is(DEBUG_VARIABLE_PORT));
        assertThat(varPort.getCell(1), is("undefined"));
        
    }
    
    @Test
    public void testJSVariablesInitialized() throws CoreException {
        
        
        new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("book.js").open();
        new WaitUntil(new EditorWithTitleIsActive("book.js"));
        TextEditor editor = new TextEditor("book.js");
        setLineBreakpoint(editor, BOOK_JS_BREAKPOINT_LINE);
        
        // resume
        new WorkbenchView("Debug").open();
        DefaultTree debugTree = new DefaultTree();
        resume(debugTree, new RegexMatcher("\\(anonymous function\\)(.*)(index\\.js)(.*)"));
        
        new WaitUntil(new EditorWithTitleIsActive("book.js"), TimePeriod.LONG);
        new WaitUntil(new CursorPositionIsOnLine(editor, BOOK_JS_BREAKPOINT_LINE));
        
        TreeItem varTitle = getVariable(DEBUG_VARIABLE_TITLE);
        TreeItem varAuthor = getVariable(DEBUG_VARIABLE_AUTHOR);
        
        assertTrue("Variable '"+ DEBUG_VARIABLE_TITLE  + "' not found in view!", varTitle != null);
        assertThat(varTitle.getCell(0), is(DEBUG_VARIABLE_TITLE));
        assertThat(varTitle.getCell(1), is(DEBUG_VARIABLE_TITLE_VALUE));
        
        
        assertTrue("Variable '" + DEBUG_VARIABLE_AUTHOR + "' not found in view!", varAuthor != null);
        assertThat(varAuthor.getCell(0), is(DEBUG_VARIABLE_AUTHOR));
        assertThat(varAuthor.getCell(1), is(DEBUG_VARIABLE_AUTHOR_VALUE));
    }
    
    @Test
    public void testDebuggerStopsAtBreakpoint() throws CoreException {
        
        new ProjectExplorer().getProject(TEST_APP_NAME).getProjectItem("book.js").open();
        new WaitUntil(new EditorWithTitleIsActive("book.js"));
        TextEditor editor = new TextEditor("book.js");
        setLineBreakpoint(editor, BOOK_JS_BREAKPOINT_LINE);
        
        // resume
        new WorkbenchView("Debug").open();
        DefaultTree debugTree = new DefaultTree();
        resume(debugTree, new RegexMatcher("\\(anonymous function\\)(.*)(index\\.js)(.*)"));
        
        try {
            new WaitUntil(new EditorWithTitleIsActive("book.js"), TimePeriod.LONG);
            new WaitUntil(new CursorPositionIsOnLine(editor, BOOK_JS_BREAKPOINT_LINE));
        } catch (WaitTimeoutExpiredException e) {
            Assert.fail("Debugger hasn't stopped on breakpoint");
        }
    }
    
}

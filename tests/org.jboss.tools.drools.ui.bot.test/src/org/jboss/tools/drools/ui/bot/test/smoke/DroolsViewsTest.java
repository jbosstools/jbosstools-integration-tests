package org.jboss.tools.drools.ui.bot.test.smoke;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withLabel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.helper.DragAndDropHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for testing Drools views.
 * 
 * @author jgargula
 */
public class DroolsViewsTest extends SWTTestExt {

    private static final String PROJECT_NAME = "droolsViewsTest";
    private static final String RULES_FILE = "Sample.drl";
    private static final String RULES_FILE_PATH = "src/main/rules";
    private static final String JAVA_FILE = "DroolsTest.java";
    private static final String JAVA_FILE_PATH = "src/main/java";
    private static final String SAMPLE_TREE_NODE = "com.sample";
    private static final String GLOBAL_VARIABLE_NAME = "globalString";
    private static final String GLOBAL_VARIABLE_VALUE = "abcd";
    private static final String LOG_FILE = "test.log";
    private static boolean isFirstTimeRun = true;

    /**
     * Prepares all necessary things.
     */
    @Before
    public void setUp() {
        if (isFirstTimeRun) {
            eclipse.closeAllEditors();
            manageProject();
            setUpOnce();
        }

        SWTBotTreeItem javaTreeItem = packageExplorer.selectTreeItem(JAVA_FILE, new String[] {PROJECT_NAME, JAVA_FILE_PATH, SAMPLE_TREE_NODE});
        eclipse.debugTreeItemAsDroolsApplication(javaTreeItem);

        if (isFirstTimeRun) {
            eclipse.closeConfirmPerspectiveSwitchShellIfOpened(false, true);
        } else {
            while (bot.waitForShell(IDELabel.Shell.PROGRESS_INFORMATION, 0) != null) {
                bot.sleep(Timing.time2S());
            }    
        }

        bot.waitForNumberOfShells(1);

        // waits for running debugging
        while (!eclipse.isDebugging()) {
            bot.sleep(Timing.time1S());
        	if (console.getConsoleText().contains("Finished")) {
                fail("Probably debugging is not stopping at breakpoints.");
            }
        }

        // waits for stopping at breakpoint
        while (!eclipse.isSuspendedAtBreakpoint()) {
            bot.sleep(Timing.time1S());
        }

        isFirstTimeRun = false;
        bot.sleep(Timing.time1S());
    }

    /**
     * This should be executed just before the first test.
     */
    private void setUpOnce() {
        adjustDroolsTestFile();
        adjustSampleFile();
        bot.saveAllEditors();
    }

    /**
     * Deletes all existing projects
     * and creates brand new project.
     */
    private void manageProject() {
        ManageDroolsProject.createDroolsProject(PROJECT_NAME);

        Widget openingProjectWidget = null;
        try {
            openingProjectWidget = bot.widget(withLabel("Drools Runtime"));
        } catch (WidgetNotFoundException wnfe) {
            // openingProjectWidget should be null in this case
        }
        for (int i = 0; i < 30; i++) {
            if (openingProjectWidget == null || (openingProjectWidget.isDisposed()
                    && packageExplorer.bot().tree().getAllItems().length > 0)) {
                break;
            } else {
                bot.sleep(Timing.time2S());
            }
        }    
    }

    /**
     * Finishes tests.
     */
    @After
    public void tearDown() {
        eclipse.closeView(IDELabel.View.AGENDA);
        eclipse.closeView(IDELabel.View.GLOBAL_DATA);
        eclipse.closeView(IDELabel.View.AUDIT);
        eclipse.closeView(IDELabel.View.WORKING_MEMORY);
        eclipse.finishDebug();
    }

    /**
     * Test of Agenda view.
     */
    @Test
    public void testAgenda() {
        bot.editorByTitle(RULES_FILE).show();
        eclipse.stepOver();
        SWTUtilExt.startCapturingStandardOutput();
        openView(IDELabel.View.AGENDA);
        final String output = SWTUtilExt.stopCapturingStandardOutput();
        System.out.print(output);
        HashSet<Activation> expectedSet = new HashSet<Activation>(2);
        // indicies are not compared
        expectedSet.add(new Activation(0, "Bye Bye"));
        expectedSet.add(new Activation(1, "GoodBye"));
        assertEquals(expectedSet, new HashSet<Activation>(getActivationsFromAgenda()));
        eclipse.resumeDebug();
        final String CONSOLE_TEXT_MESSAGE_1 = "Expected console text is:\n";
        final String CONSOLE_TEXT_MESSAGE_2 = "but was:\n";
        final String EXPECTED_CONSOLE_TEXT_1 = "Hello World\nGoodbye cruel world\nabcd\n";
        final String EXPECTED_CONSOLE_TEXT_2 = "JustBye rule\nFinished\n";
        assertTrue(CONSOLE_TEXT_MESSAGE_1 + EXPECTED_CONSOLE_TEXT_1 + CONSOLE_TEXT_MESSAGE_2 + console.getConsoleText(),
                EXPECTED_CONSOLE_TEXT_1.equals(console.getConsoleText()));
        expectedSet.clear();
        expectedSet.add(new Activation(0, "Finish rule"));
        showView(IDELabel.View.AGENDA);
        assertEquals(expectedSet, new HashSet<Activation>(getActivationsFromAgenda()));
        eclipse.finishDebug();
        assertTrue(CONSOLE_TEXT_MESSAGE_1 + EXPECTED_CONSOLE_TEXT_1 + EXPECTED_CONSOLE_TEXT_2 + CONSOLE_TEXT_MESSAGE_2
                + console.getConsoleText(), (EXPECTED_CONSOLE_TEXT_1 + EXPECTED_CONSOLE_TEXT_2).equals(console.getConsoleText()));
        assertFalse("Opening agenda view throws DebugException", output.contains("DebugException"));
    }

    /**
     * Tests refreshing of Agenda view.
     * 
     * This test is ignored because it's not a bug, it should be feature
     * https://issues.jboss.org/browse/JBIDE-10682
     */
    @Test
    @Ignore
    public void testRefreshAgenda() {
        openView(IDELabel.View.AGENDA);
        eclipse.stepOver();
        assertTrue("Agenda is not refreshing automatically.", bot.tree().getAllItems()[0].getItems().length > 0);
    }

    /**
     * Test of global variable in Global Data.
     */
    @Test
    public void testGlobalData() {
        openView(IDELabel.View.GLOBAL_DATA);
        assertTrue("Global data has wrong number of items. Expected 1 but was "
                + bot.tree().getAllItems().length, bot.tree().getAllItems().length == 1);
        assertTrue("Global data does not contain variable '" + GLOBAL_VARIABLE_NAME + "' with value '"
                + GLOBAL_VARIABLE_VALUE + "'", bot.tree().getAllItems()[0].getText()
                .contains(GLOBAL_VARIABLE_NAME + "= \"" + GLOBAL_VARIABLE_VALUE + "\""));
    }

    /**
     * Test of Audit view.
     */
    @Test
    public void testAudit() {
        eclipse.finishDebug();
        openView(IDELabel.View.AUDIT);
        SWTBotView auditView = bot.viewByTitle(IDELabel.View.AUDIT);
        assertEquals("Tree in Audit view should be empty, but it has " + auditView.bot().tree().getAllItems().length
                + " items.", 0, auditView.bot().tree().getAllItems().length);
        packageExplorer.selectTreeItem(PROJECT_NAME, null).contextMenu(IDELabel.Menu.REFRESH).click();
        assertTrue("Log file '" + LOG_FILE + "' was not created.", packageExplorer.existsResource(PROJECT_NAME, LOG_FILE));
        SWTBotTreeItem treeItem = packageExplorer.selectTreeItem(LOG_FILE, new String[] {PROJECT_NAME});
        DragAndDropHelper.dragAndDropOnTo(treeItem.widget, auditView.getWidget());
        SWTBotTreeItem[] auditTreeItems = auditView.bot().tree().getAllItems();
        assertTrue("Tree in Audit view should not be empty, but it was.", auditTreeItems.length > 0);
        assertFalse("Log file '" + LOG_FILE + "' was not loaded properly into Audit view.",
                "The selected audit log is empty.".equals(auditTreeItems[0].getText()));
        assertEquals("Tree in Audit view has wrong number of items.", 4, auditTreeItems.length);
        int index = 0;
        assertTrue("The first node of the audit view's tree does not contain right text.",
                auditTreeItems[index].getText().startsWith("Object inserted"));
        assertEquals("The first node has wrong number of items", 1, auditTreeItems[index].getItems().length);
        assertTrue("Child node of the first node does not contain right text.",
                auditTreeItems[index].getItems()[0].getText().startsWith("Activation created: Rule Hello World"));
        index = 1;
        assertTrue("The second node does not contain the right text.",
                auditTreeItems[index].getText().startsWith("Activation executed: Rule Hello World"));
        assertEquals("The second node has wrong number of items.", 1, auditTreeItems[index].getItems().length);
        assertTrue("Child node of the second node does not contain the right text.",
                auditTreeItems[index].getItems()[0].getText().startsWith("Object updated"));
        assertEquals("Child node of the second node has wrong number of items.", 2, auditTreeItems[index].getItems()[0].getItems().length);
        assertTrue("Child node of the second node does not contain the right text.",
                auditTreeItems[index].getItems()[0].getItems()[0].getText().startsWith("Activation created: Rule GoodBye"));
        assertTrue("Child node of the second node does not contain the right text.",
                auditTreeItems[index].getItems()[0].getItems()[1].getText().startsWith("Activation created: Rule Bye Bye"));
        index = 2;
        assertTrue("The third node does not contain the right text.",
                auditTreeItems[index].getText().startsWith("Activation executed: Rule Bye Bye"));
        assertEquals("The third node has wrong number of items.", 1, auditTreeItems[index].getItems().length);
        assertTrue("Child node of the third node does not contain the right text.",
                auditTreeItems[index].getItems()[0].getText().startsWith("Object updated"));
        assertEquals("Child node of the third node has wrong number of items.", 2, auditTreeItems[index].getItems()[0].getItems().length);
        assertTrue("Child node of the third node does not contain the right text.",
                auditTreeItems[index].getItems()[0].getItems()[0].getText().startsWith("Activation created: Rule Finish rule"));
        assertTrue("Child node of the third node does not contain the right text.",
                auditTreeItems[index].getItems()[0].getItems()[1].getText().startsWith("Activation cancelled: Rule GoodBye"));
        index = 3;
        assertTrue("The fourth node does not contain the right text.",
                auditTreeItems[index].getText().startsWith("Activation executed: Rule Finish rule"));
        assertEquals("The fourth node has wrong number of items.", 0, auditTreeItems[index].getItems().length);

        auditView.toolbarButton("Clear Log").click();
        assertEquals("Tree should be empty, but it was not.", 0, auditView.bot().tree().getAllItems().length);
        packageExplorer.selectTreeItem(PROJECT_NAME, null).contextMenu(IDELabel.Menu.REFRESH).click();
    }

    /**
     * Test of Working Memory view
     */
    @Test
    public void testWorkingMemory() {
        openView(IDELabel.View.WORKING_MEMORY);

        SWTBotTree workingMemoryTree = bot.tree();
        assertEquals("Working memory tree was expected to have exactly 1 root item, but it had "
                + workingMemoryTree.getAllItems().length + " root items.", 1, workingMemoryTree.getAllItems().length);

        String rootItemText = workingMemoryTree.getAllItems()[0].getText();
        assertNotNull(rootItemText);
        assertTrue("Root item of working memory tree had unexpected text: " + rootItemText,
                rootItemText.contains("DroolsTest$Message"));

        List<String> treeItemsStrings = workingMemoryTree.expandNode(rootItemText).getNodes();
        final int EXPECTED_ITEMS = 6;
        assertEquals("There should be " + EXPECTED_ITEMS + " items but was " + treeItemsStrings.size(),
                EXPECTED_ITEMS, treeItemsStrings.size());

        String messageString = "message= \"Goodbye cruel world\"";
        String statusString = "status= 1";
        String[] itemsStrings = new String[] {"BYE= 2", "FINISH= 3","GOODBYE= 1", "HELLO= 0", messageString, statusString};
        Set<String> expectedWorkingMemorySet = new HashSet<String>(EXPECTED_ITEMS);
        for (int i = 0; i < itemsStrings.length; i++) {
            expectedWorkingMemorySet.add(itemsStrings[i]);
        }
        assertEquals(expectedWorkingMemorySet, getWorkingMemoryItems(rootItemText));

        eclipse.resumeDebug();
        expectedWorkingMemorySet.remove(messageString);
        expectedWorkingMemorySet.remove(statusString);
        messageString = "message= \"JustBye rule\"";
        statusString = "status= 3";
        expectedWorkingMemorySet.add(messageString);
        expectedWorkingMemorySet.add(statusString);
        assertEquals(expectedWorkingMemorySet, getWorkingMemoryItems(rootItemText));

        eclipse.resumeDebug();
        expectedWorkingMemorySet.clear();
        showView(IDELabel.View.CONSOLE);
        showView(IDELabel.View.WORKING_MEMORY);
        assertEquals("Working memory view should be empty. Maybe it was not refreshed.",
                expectedWorkingMemorySet, getWorkingMemoryItems(rootItemText));
    }

    /**
     * Gets working memory items from Working Memory view.
     * 
     * @param rootNode Root node name
     * @return Set of strings representing working memory items.
     */
    private Set<String> getWorkingMemoryItems(final String rootNode) {
        waitForStoppingDebugging();
        showView(IDELabel.View.WORKING_MEMORY);
        Set<String> stringSet = new HashSet<String>();

        List<String> nodesStrings = null;
        try {
            nodesStrings = bot.tree().expandNode(rootNode).getNodes();
        } catch (WidgetNotFoundException wnfe) {
            return stringSet;
        }

        for (String s : nodesStrings) {
            final int index = s.indexOf('(');
            if (index > 0) {
                s = s.substring(0, index);
            }
            stringSet.add(s.trim());
        }

        return stringSet;
    }

    /**
     * Waits while debugging is stopped at breakpoint or finished.
     */
    private void waitForStoppingDebugging() {
        for (int i = 0; i < 20; i++) {
            SWTBotMenu resumeMenu = bot.menu(IDELabel.Menu.RUN).menu(IDELabel.Menu.RESUME);
            if (resumeMenu.isEnabled()) {
                return;
            }
            SWTBotMenu terminateMenu = bot.menu(IDELabel.Menu.RUN).menu(IDELabel.Menu.TERMINATE);
            if (!resumeMenu.isEnabled() && !terminateMenu.isEnabled()) {
                return;
            }
            bot.sleep(Timing.time1S());
        }
    }

    /**
     * Adds some pieces of code to DroolsTest.java
     */
    private void adjustDroolsTestFile() {
        packageExplorer.show();
        SWTBotEclipseEditor droolsTestEditor = packageExplorer.openFile(PROJECT_NAME,
                JAVA_FILE_PATH, SAMPLE_TREE_NODE, JAVA_FILE).toTextEditor();
        droolsTestEditor.insertText(46, 0, "        public static final int BYE = 2;\n"
                + "        public static final int FINISH = 3;\n");
        droolsTestEditor.insertText(20, 0, "            ksession.setGlobal(\""
                + GLOBAL_VARIABLE_NAME + "\", \"" + GLOBAL_VARIABLE_VALUE + "\");\n");
    }

    /**
     * Adds some source code to Sample.drl
     */
    private void adjustSampleFile() {
        packageExplorer.show();
        SWTBotEclipseEditor sampleEditor = packageExplorer.openFile(PROJECT_NAME,
                RULES_FILE_PATH, RULES_FILE).toTextEditor();
        sampleEditor.insertText(20, 0, "\nrule \"Bye Bye\"\n"
                + "    when\n"
                + "        m : Message(status == Message.BYE || status == Message.GOODBYE, myMessage : message)\n"
                + "    then\n"
                + "        System.out.println(myMessage);\n"
                + "        System.out.println(globalString);\n"
                + "        m.setMessage(\"JustBye rule\");\n"
                + "        m.setStatus(Message.FINISH);\n"
                + "        update(m);\n"
                + "end\n\n"
                + "rule \"Finish rule\"\n"
                + "    when\n"
                + "        Message(status == Message.FINISH, myMessage : message)\n"
                + "    then\n"
                + "        System.out.println(myMessage);\n"
                + "        System.out.println(\"Finished\");\n"
                + "end\n");
        sampleEditor.insertText(16, 8, "m : ");
        sampleEditor.insertText(19, 0, "        m.setMessage(\"Bye Bye\");\n"
                + "        m.setStatus(Message.BYE);\n"
                + "        update(m);\n");
        sampleEditor.insertText(4, 0, "global String globalString;\n\n");
        eclipse.setNewBreakpoints(sampleEditor, 13, 35);
    }

    /**
     * This method opens view with given title.
     * 
     * @param viewTitle Title of desired view.
     */
    private void openView(final String viewTitle) {
        checkViewTitleNull(viewTitle);

        IView iView;
        try {
            iView = (IView) Class.forName("org.jboss.tools.ui.bot.ext.gen.ActionItem$View$Drools"
                        + viewTitle.replace(" ", "")).getField("LABEL").get(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("It is not possible to get IView object for view with title '"
                    + viewTitle + "'.", e);
        }

        new SWTOpenExt(bot).viewOpen(iView);
    }

    /**
     * Shows view with given title.
     * It shows and focuses chosen view.
     * If view is closed then it is opened.
     * 
     * @param viewTitle Title of desired view.
     */
    private void showView(final String viewTitle) {
        checkViewTitleNull(viewTitle);
        boolean isViewOpened = false;
        for (final SWTBotView view : bot.views()) {
            if (viewTitle.equals(view.getTitle())) {
                view.getWidget().getDisplay().syncExec(new Runnable() {
                    public void run() {
                        view.show();
                        view.setFocus();
                    }
                });
                isViewOpened = true;
                break;
            }
        }
        if (!isViewOpened) {
            openView(viewTitle);
        }
    }

    /**
     * Checks if given string is null and if so this method
     * throws IllegalArgumentException.
     * 
     * @param viewTitle String to check. 
     */
    private void checkViewTitleNull(final String viewTitle) {
        if (viewTitle == null) {
            throw new IllegalArgumentException("'viewTitle' cannot be null");
        }
    }

    /**
     * Gets list of current activations in working memory.
     * 
     * @return List of current activations in working memory
     */
    private List<Activation> getActivationsFromAgenda() {
        List<Activation> activations = new ArrayList<Activation>();
        bot.tree().expandNode(bot.tree().getAllItems()[0].getText(), true);
        for (SWTBotTreeItem item : bot.tree().getAllItems()[0].getItems()) {
            Activation activation = new Activation(
                    parseIndex(item.getText()),
                    parseRuleName(item.getItems()[0].getText()));
            activations.add(activation);
        }
        return activations;
    }

    /**
     * Parses text for an activation index in working memory.
     * 
     * @param text Text to parse
     * @return index of activation in working memory
     */
    private int parseIndex(final String text) {
        int beginIndex = text.indexOf('[');
        int endIndex = text.indexOf(']');
        if (beginIndex < 0 || endIndex < 0) {
            throw new IllegalArgumentException("Given text is not parsable for index. Given text was '" + text + "'");
        }
        return Integer.valueOf(text.substring(beginIndex + 1, endIndex));
    }

    /**
     * Parses rule name of activation in working memory.
     * 
     * @param text Text to parse
     * @return Rule name
     */
    private String parseRuleName(final String text) {
        if (!text.startsWith("ruleName")) {
            return null;
        }
        int i = text.indexOf('"');
        return text.substring(i + 1, text.indexOf('"', i + 1));
    }

    /**
     * This class is representation of activation in working memory.
     * 
     * @author jgargula
     */
    private class Activation {
        private int index = 0;
        private String ruleName = null;

        /**
         * Constructor.
         */
        public Activation() {
            super();
        }

        /**
         * Constructor.
         * 
         * @param index Index of activation
         */
        public Activation(int index) {
            this();
            setIndex(index);
        }

        /**
         * Constructor.
         * 
         * @param index Index of activation
         * @param ruleName Rule name of activation
         */
        public Activation(int index, String ruleName) {
            this(index);
            setRuleName(ruleName);
        }

        /**
         * Getter for <code>index</code>.
         * 
         * @return index of activation
         */
        public int getIndex() {
            return index;
        }

        /**
         * Setter for <code>index</code>.
         * 
         * @param index Index to be set on the activation
         */
        public void setIndex(int index) {
            this.index = index;
        }

        /**
         * Getter for <code>ruleName</code>.
         * 
         * @return rule name of activation
         */
        public String getRuleName() {
            return ruleName;
        }

        /**
         * Setter for <code>ruleName</code>.
         * @param ruleName
         */
        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        /**
         * @return String representation of activation.
         */
        public String toString() {
            return "Activation [index=" + getIndex() + "; ruleName=\"" + getRuleName() + "\"]";
        }

        /**
         * Hash code.
         */
        @Override
        public int hashCode() {
            return 31 + ((ruleName == null) ? 0 : ruleName.hashCode());
        }

        /**
         * Two activations are equal if they have the same rule name.
         * Indicies are ignored while comparing.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Activation)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            Activation other = (Activation) obj;
            if (getRuleName() == null) {
                if (other.getRuleName() != null) {
                    return false;
                }
            }
            if (getRuleName() == null && other.getRuleName() == null) {
                return true;
            }
            return getRuleName().equals(other.getRuleName());
        }
    }
}

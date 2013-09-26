package org.jboss.tools.drools.ui.bot.test.functional;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class RulesManagementTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(RulesManagementTest.class);
    private static final String DEBUG_REGEX = "(SLF4J: .*\n)+?" +
            "(kmodules: file:(/.*)+/kmodule.xml\n)?";
    private static final String SUCCESSFUL_RUN_REGEX = DEBUG_REGEX + "Hello World\nGoodbye cruel world\n";

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testRunRulesFromContextMenu() {
        ConsoleView console = new ConsoleView();
        console.open();

        RunUtility.runAsJavaApplication(true, DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());
        waitASecond(); // this is quite annoying - the text is updated AFTER the application is terminated

        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testRunRulesFromToolbar() {
        ConsoleView console = new ConsoleView();
        console.open();

        RunUtility.runAsJavaApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());
        waitASecond(); // this is quite annoying - the text is updated AFTER the application is terminated

        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    public void testRunRulesWithDefaultRuntime() {
        final String runtimeName = "testRunRulesWithDefaultRuntime";
        final String projectName = "testRunRulesWithDefaultRuntime";
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntimeDialog dialog = pref.addDroolsRuntime();
        dialog.setName(runtimeName);
        dialog.createNewRuntime(createTempDir("testRunRulesWithDefaultRuntime"));
        dialog.ok();
        pref.setDroolsRuntimeAsDefault(runtimeName);
        pref.okCloseWarning();

        NewDroolsProjectWizard wiz = new NewDroolsProjectWizard();
        wiz.createDefaultProjectWithAllSamples(projectName);


        ConsoleView console = new ConsoleView();
        console.open();

        RunUtility.runAsJavaApplication(projectName, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());
        waitASecond(); // this is quite annoying - the text is updated AFTER the application is terminated

        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testRenameProject() {
        final String oldName = DEFAULT_PROJECT_NAME;
        final String newName = "renamed" + oldName;

        PackageExplorer explorer = new PackageExplorer();
        Assert.assertTrue("The original project was not created.", explorer.containsProject(oldName));
        explorer.getProject(oldName).select();

        RegexMatchers m = new RegexMatchers("Refactor.*", "Rename.*");
        new ContextMenu(m.getMatchers()).select();

        new DefaultShell("Rename Java Project");
        new LabeledText("New name:").setText(newName);
        new PushButton("OK").click();
        new WaitWhile(new JobIsRunning());

        waitASecond();
        Assert.assertFalse("The original project is still present.", explorer.containsProject(oldName));
        Assert.assertTrue("The renamed project is not present.", explorer.containsProject(newName));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testSetBreakpoint() {
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem(RESOURCES_LOCATION, "rules", "Sample.drl").select();
        new ContextMenu(new RegexMatchers("Open.*").getMatchers()).select();

        DrlEditor editor = new DrlEditor();
        editor.setPosition(8, 0);

        new ShellMenu(new RegexMatchers("Run.*", "Toggle Breakpoint.*").getMatchers()).select();
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testDebugRule() {
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem(RESOURCES_LOCATION, "rules", "Sample.drl").select();
        new ContextMenu(new RegexMatchers("Open.*").getMatchers()).select();

        new DrlEditor().setBreakpoint(8);

        RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

        try {
            new DefaultShell("Confirm Perspective Switch");
            new PushButton("Yes").click();
        } catch (Exception ex) {
            LOGGER.warn("Confirm Perspective Switch dialog not shown.");
        }

        ConsoleView console = new ConsoleView();
        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(DEBUG_REGEX));

        new ShellMenu(new RegexMatchers("Run", "Resume.*").getMatchers()).select();
        console.open();
        Assert.assertTrue("Wrong console text found\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    /**
     * @deprecated try not to use thread sleep to wait for events (there has to be a better way)
     */
    @Deprecated
    private void waitASecond() {
        try { Thread.sleep(1000); } catch (InterruptedException ex) {}
    }
}

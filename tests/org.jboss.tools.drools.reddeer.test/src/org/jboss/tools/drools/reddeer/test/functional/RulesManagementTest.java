package org.jboss.tools.drools.reddeer.test.functional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.reddeer.test.annotation.UsePerspective;
import org.jboss.tools.drools.reddeer.test.util.SmokeTest;
import org.jboss.tools.drools.reddeer.test.util.TestParent;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage.RuleResourceType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class RulesManagementTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(RulesManagementTest.class);
    private static final String RULES_LOCATION = "src/main/rules";
    private static final Pattern RULE_PATTERN = Pattern.compile("(?s)rule.*?when.*?then.*?end");

    @Test @Category(SmokeTest.class)
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testCreateIndividualRule() {
        final String resourceName = "testCreateIndividualRule";
        final String packageName = "com.sample";

        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        NewRuleResourceWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_PROJECT_NAME + "/" + RULES_LOCATION);
        page.setFileName(resourceName);
        page.setRulePackageName(packageName);
        page.setTypeOfRuleResource(RuleResourceType.individualRule);
        wiz.finish();

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        Project p = explorer.getProject(DEFAULT_PROJECT_NAME);
        Assert.assertTrue("Rule resource was not created", p.containsItem(RULES_LOCATION, resourceName + ".drl"));

        // FIXME Use RedDeer editors when available
        String text = Bot.get().activeEditor().toTextEditor().getText();
        Assert.assertTrue("Wrong package declaration.", text.contains("package " + packageName));
        Matcher m = RULE_PATTERN.matcher(text);
        Assert.assertTrue("No rule present in file", m.find());
        Assert.assertFalse("More than one rules present in file", m.find());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testCreateRulePackage() {
        final String resourceName = "testCreateRulePackage";
        final String packageName = "com.sample";

        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        NewRuleResourceWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_PROJECT_NAME + "/" + RULES_LOCATION);
        page.setFileName(resourceName);
        page.setRulePackageName(packageName);
        page.setTypeOfRuleResource(RuleResourceType.rulePackage);
        wiz.finish();

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        Project p = explorer.getProject(DEFAULT_PROJECT_NAME);
        Assert.assertTrue("Rule resource was not created", p.containsItem(RULES_LOCATION, resourceName + ".drl"));

        // FIXME Use RedDeer editors when available
        String text = Bot.get().activeEditor().toTextEditor().getText();
        Assert.assertTrue("Wrong package declaration.", text.contains("package " + packageName));
        Matcher m = RULE_PATTERN.matcher(text);
        Assert.assertTrue("No rule present in file", m.find());
        Assert.assertTrue("Only one rule present in file", m.find());
        Assert.assertFalse("More than two rules present in file", m.find());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testDebugRule() {
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem(RULES_LOCATION, "Sample.drl").select();
        new ContextMenu(new RegexMatchers("Open.*").getMatchers()).select();

        Bot.get().activeEditor().toTextEditor().navigateTo(8, 0);
        // FIXME workaround to enable Toggle Breakpoint
        new JavaPerspective().open();
        new ShellMenu(new RegexMatchers("Run.*", "Toggle Breakpoint.*").getMatchers()).select();

        explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem("src/main/java", "com.sample", "DroolsTest.java").select();
        new ShellMenu(new RegexMatchers("Run", "Debug As.*", ".*Drools Application.*").getMatchers()).select();

        try {
            new DefaultShell("Confirm Perspective Switch");
            new PushButton("Yes").click();
        } catch (Exception ex) {
            LOGGER.warn("Confirm Perspective Switch dialog not shown.");
        }

        ConsoleView console = new ConsoleView();
        console.open();
        Assert.assertEquals("Console text present!", "", console.getConsoleText());

        new ShellMenu(new RegexMatchers("Run", "Resume.*").getMatchers()).select();
        Assert.assertEquals("Wrong console text found!", "Hello World\nGoodbye cruel world", console.getConsoleText());
    }
}

package org.jboss.tools.drools.ui.bot.test.functional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.wizard.NewDecisionTableWizard;
import org.jboss.tools.drools.reddeer.wizard.NewDecisionTableWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage.RuleResourceType;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@Category(SmokeTest.class)
public class NewResourcesTest extends TestParent {
    private static final Pattern RULE_PATTERN = Pattern.compile("(?s)rule.*?when.*?then.*?end");

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewDRL() {
        final String resourceName = name.getMethodName();
        final String packageName = "com.redhat";

        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        NewRuleResourceWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(resourceName);
        page.setRulePackageName(packageName);
        page.setTypeOfRuleResource(RuleResourceType.rulePackage);
        wiz.finish();

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        Project p = explorer.getProject(DEFAULT_PROJECT_NAME);
        Assert.assertTrue("Rule resource was not created", p.containsItem(RESOURCES_LOCATION, "rules", resourceName + ".drl"));

        String text = new DrlEditor().getText();
        Assert.assertTrue("Wrong package declaration.", text.contains("package " + packageName));
        Matcher m = RULE_PATTERN.matcher(text);
        Assert.assertTrue("No rule present in file", m.find());
        Assert.assertTrue("Only one rule present in file", m.find());
        Assert.assertFalse("More than two rules present in file", m.find());
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewIndividualRule() {
        final String resourceName = "testCreateIndividualRule";
        final String packageName = "com.redhat";

        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        NewRuleResourceWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(resourceName);
        page.setRulePackageName(packageName);
        page.setTypeOfRuleResource(RuleResourceType.individualRule);
        wiz.finish();

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        Project p = explorer.getProject(DEFAULT_PROJECT_NAME);
        Assert.assertTrue("Rule resource was not created", p.containsItem(RESOURCES_LOCATION, "rules", resourceName + ".drl"));

        String text = new DrlEditor().getText();
        Assert.assertTrue("Wrong package declaration.", text.contains("package " + packageName));
        Matcher m = RULE_PATTERN.matcher(text);
        Assert.assertTrue("No rule present in file", m.find());
        Assert.assertFalse("More than one rules present in file", m.find());
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewDsl() {
        final String resourceName = name.getMethodName();

        NewDslWizard wiz = new NewDslWizard();
        wiz.open();
        NewDslWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(resourceName);
        // generate sample DSL lines
        wiz.getSamplesPage().setAddSampleDsl(true);
        wiz.finish();

        PackageExplorer pkg = new PackageExplorer();
        Assert.assertTrue(pkg.getProject(DEFAULT_PROJECT_NAME).containsItem(RESOURCES_LOCATION, "rules", resourceName + ".dsl"));
    }

    @Ignore("Opens the decision table and fails remaining tests")
    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewDecisionTable() {
        final String resourceName = name.getMethodName();

        NewDecisionTableWizard wiz = new NewDecisionTableWizard();
        wiz.open();
        NewDecisionTableWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(resourceName);
        wiz.finish();

        PackageExplorer pkg = new PackageExplorer();
        Assert.assertTrue(pkg.getProject(DEFAULT_PROJECT_NAME).containsItem(RESOURCES_LOCATION, "rules", resourceName + ".xls"));
    }
}

package org.jboss.tools.drools.reddeer.test.functional;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.reddeer.test.annotation.UsePerspective;
import org.jboss.tools.drools.reddeer.test.util.SmokeTest;
import org.jboss.tools.drools.reddeer.test.util.TestParent;
import org.jboss.tools.drools.reddeer.wizard.NewDecisionTableWizard;
import org.jboss.tools.drools.reddeer.wizard.NewDecisionTableWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizardPage.RuleResourceType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@Category(SmokeTest.class)
public class NewResourcesTest extends TestParent {

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewDRL() {
        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        NewRuleResourceWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(name.getMethodName());
        page.setTypeOfRuleResource(RuleResourceType.rulePackage);
        page.setRulePackageName("com.redhat");
        wiz.finish();

        PackageExplorer pkg = new PackageExplorer();
        pkg.open();
        ProjectItem item = pkg.getProject(DEFAULT_PROJECT_NAME).getProjectItem("src/main/rules");
        Assert.assertNotNull(item.getChild(name.getMethodName() + ".drl"));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewIndividualRule() {
        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        NewRuleResourceWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(name.getMethodName());
        page.setTypeOfRuleResource(RuleResourceType.individualRule);
        page.setRulePackageName("com.redhat");
        wiz.finish();

        PackageExplorer pkg = new PackageExplorer();
        pkg.open();
        ProjectItem item = pkg.getProject(DEFAULT_PROJECT_NAME).getProjectItem("src/main/rules");
        Assert.assertNotNull(item.getChild(name.getMethodName() + ".drl"));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewDsl() {
        NewDslWizard wiz = new NewDslWizard();
        wiz.open();
        NewDslWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(name.getMethodName());
        wiz.finish();

        PackageExplorer pkg = new PackageExplorer();
        pkg.open();
        ProjectItem item = pkg.getProject(DEFAULT_PROJECT_NAME).getProjectItem("src/main/rules");
        Assert.assertNotNull(item.getChild(name.getMethodName() + ".dsl"));
    }

    @Ignore("Opens the decision table and fails remaining tests")
    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testNewDecisionTable() {
        NewDecisionTableWizard wiz = new NewDecisionTableWizard();
        wiz.open();
        NewDecisionTableWizardPage page = wiz.getFirstPage();
        page.setParentFolder(DEFAULT_RULES_PATH);
        page.setFileName(name.getMethodName());
        wiz.finish();

        PackageExplorer pkg = new PackageExplorer();
        pkg.open();
        ProjectItem item = pkg.getProject(DEFAULT_PROJECT_NAME).getProjectItem("src/main/rules");
        Assert.assertNotNull(item.getChild(name.getMethodName() + ".xls"));
    }
}

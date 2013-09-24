package org.jboss.tools.drools.ui.bot.test.functional;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.properties.DroolsProjectProperties;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectSelectRuntimeWizardPage.CodeCompatibility;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class ProjectManagementTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(ProjectManagementTest.class);

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime
    public void testProjectCreationAndDeletion() {
        final String projectName = "testProjectCreationAndDeletion";
        ProblemsView problems = new ProblemsView();
        problems.open();
        waitASecond();
        final int errors = problems.getAllErrors().size();
        final int warnings = problems.getAllWarnings().size();
        
        NewDroolsProjectWizard wiz = new NewDroolsProjectWizard();
        wiz.open();
        wiz.getFirstPage().setProjectName(projectName);
        wiz.getSelectSamplesPage().checkAll();
        wiz.getDroolsRuntimePage().setUseDefaultRuntime(true);
        wiz.getDroolsRuntimePage().setCodeCompatibleWithVersion(CodeCompatibility.Drools60x);
        wiz.getDroolsRuntimePage().setGAV("com.redhat", name.getMethodName(), "1.0-SNAPSHOT");
        wiz.finish();
        new WaitWhile(new JobIsRunning());

        PackageExplorer explorer = new PackageExplorer();
        Assert.assertTrue("Project was not created.", explorer.containsProject(projectName));

        Assert.assertTrue("Project does not have Drools dependencies.", explorer.getProject(projectName).containsItem("Drools Library"));
        Assert.assertTrue("Wrong drools runtime used.", findDroolsCoreJar(projectName).contains(DEFAULT_DROOLS_RUNTIME_LOCATION));

        problems = new ProblemsView();
        problems.open();
        waitASecond();
        Assert.assertEquals("There are errors in newly created project.", errors ,problems.getAllErrors().size());
        Assert.assertEquals("There are warnings in newly created project.", warnings, problems.getAllWarnings().size());

        explorer.getProject(projectName).delete(true);
        Assert.assertFalse("Project was not deleted.", explorer.containsProject(projectName));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testChangeDefaultRuntime() {
        final String secondRuntime = "testChangeDefaultRuntime";
        final String runtimeLocation = createTempDir("testChangeDefaultRuntime");
        ProblemsView problems = new ProblemsView();
        problems.open();
        waitASecond();
        final int errors = problems.getAllErrors().size();
        final int warnings = problems.getAllWarnings().size();

        // create new runtime
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
        wiz.setName(secondRuntime);
        wiz.createNewRuntime(runtimeLocation);
        wiz.ok();
        pref.okCloseWarning();

        // set new runtime to project
        DroolsProjectProperties props = new DroolsProjectProperties(DEFAULT_PROJECT_NAME);
        props.open();
        props.setDefaultDroolsRuntime(secondRuntime);
        props.ok();
        new WaitWhile(new JobIsRunning());

        // confirm runtime change
        Assert.assertTrue("Wrong drools runtime used.", findDroolsCoreJar(DEFAULT_PROJECT_NAME).contains(runtimeLocation));

        // check for new problems
        problems = new ProblemsView();
        problems.open();
        waitASecond();
        Assert.assertEquals("New errors occured.", errors, problems.getAllErrors().size());
        Assert.assertEquals("New warnings occured.", warnings, problems.getAllWarnings().size());
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime
    public void testConvertJavaProject() {
        final String projectName = "testJavaProject";
        NewJavaProjectWizardDialog diag = new NewJavaProjectWizardDialog();
        diag.open();
        diag.getFirstPage().setProjectName("testJavaProject");
        try {
            diag.finish();
        } catch (SWTLayerException ex) {
            LOGGER.debug("'Open Associated Perspective' dialog was not shown");
        }

        PackageExplorer explorer = new PackageExplorer();
        Assert.assertTrue("Project was not created", explorer.containsProject(projectName));
        Assert.assertFalse("Project already has Drools dependencies.", explorer.getProject(projectName).containsItem("Drools Library"));

        explorer.getProject(projectName).select();
        RegexMatchers m = new RegexMatchers("Configure.*", "Convert to Drools Project.*");
        new ContextMenu(m.getMatchers()).select();
        new WaitWhile(new JobIsRunning());

        Assert.assertTrue("Project does not have Drools dependencies.", explorer.getProject(projectName).containsItem("Drools Library"));
    }

    private String findDroolsCoreJar(String projectName) {
        new PackageExplorer().open();
        TreeItem lib = new DefaultTreeItem(projectName, "Drools Library");
        for (TreeItem libItem : lib.getItems()) {
            if (libItem.getText().startsWith("drools-core")) {
                return libItem.getText();
            }
        }

        return null;
    }

    /**
     * @deprecated try not to use thread sleep to wait for events (there has to be a better way)
     */
    @Deprecated
    private void waitASecond() {
        try { Thread.sleep(1000); } catch (InterruptedException ex) {}
    }
}

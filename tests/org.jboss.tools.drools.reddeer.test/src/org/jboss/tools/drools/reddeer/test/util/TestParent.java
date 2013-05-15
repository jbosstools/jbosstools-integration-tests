package org.jboss.tools.drools.reddeer.test.util;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage.DroolsRuntime;
import org.jboss.tools.drools.reddeer.test.Activator;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.reddeer.test.annotation.UsePerspective;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

@RunWith(RedDeerSuite.class)
public abstract class TestParent {
    private static final Logger LOGGER = Logger.getLogger(TestParent.class);
    protected static final String DEFAULT_DROOLS_RUNTIME_NAME = "defaultTestRuntime";
    protected static final String DEFAULT_DROOLS_RUNTIME_LOCATION;
    protected static final String DEFAULT_PROJECT_NAME = "defaultTestProject";
    private static final Properties TEST_PARAMS = new Properties();

    @Rule
    public TestName name = new TestName();

    @Rule
    public TestWatcher watcher = new ScreenshotTestWatcher();

    static {
        try {
            LOGGER.info("Loading properties for Drools tests");
            // Read project properties
            Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
            InputStream is = bundle.getResource("project.properties").openStream();
            TEST_PARAMS.load(is);
            LOGGER.info("Properties for Drools test loaded");
        } catch (Exception ex) {
            LOGGER.error("Unable to load properties.", ex);
            fail(ex.getMessage());
        }

        DEFAULT_DROOLS_RUNTIME_LOCATION = TEST_PARAMS.getProperty("drools.default.location");
    }

    @BeforeClass
    public static void closeStartUpDialogsAndViews() {
        try {
            new DefaultShell("JBoss Tools Usage");
            new PushButton("No").click();
        } catch (Exception ex) {
            LOGGER.info("JBoss Tools Usage dialog was not found.");
        }

        try {
            // FIXME reddeer when possible
            Bot.get().viewByTitle("Welcome").close();
        } catch (Exception ex) {
            LOGGER.info("Eclipse Welcome editor not found.");
        }

        try {
            // FIXME reddeer when possible
            Bot.get().viewByTitle("JBoss Central").close();
        } catch (Exception ex) {
            LOGGER.info("JBoss Central editor was not found.");
        }
    }

    @Before
    public void logTestName() {
        LOGGER.info(String.format("***\tStarting %s\t***", name.getMethodName()));
    }

    @Before
    public void setUpEnvironment() {
        // first set up the correct perspective
        UsePerspective def = getAnnotationOnMethod(name.getMethodName(), UsePerspective.class);
        boolean opened = false;
        try {
            if (def != null) {
                def.value().newInstance().open();
                opened = true;
            }
        } catch (InstantiationException ex) {
            LOGGER.error("Unable to instantiate perspective", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.error("Unable to instantiate perspective", ex);
        }
        if (!opened) {
            new JavaPerspective().open();
        }

        // then add a default runtime
        if (getAnnotationOnMethod(name.getMethodName(), UseDefaultRuntime.class) != null) {
            DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
            pref.open();
            boolean exists = false;
            for (DroolsRuntime runtime : pref.getDroolsRuntimes()) {
                if (DEFAULT_DROOLS_RUNTIME_NAME.equals(runtime.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
                wiz.setName(DEFAULT_DROOLS_RUNTIME_NAME);
                wiz.setLocation(DEFAULT_DROOLS_RUNTIME_LOCATION);
                wiz.ok();
                pref.setDroolsRuntimeAsDefault(DEFAULT_DROOLS_RUNTIME_NAME);
            }
            pref.okCloseWarning();
        }

        // then create default project
        if (getAnnotationOnMethod(name.getMethodName(), UseDefaultProject.class) != null) {
            if (!new PackageExplorer().containsProject(DEFAULT_PROJECT_NAME)) {
                NewDroolsProjectWizard wiz = new NewDroolsProjectWizard();
                wiz.createDefaultProjectWithAllSamples(DEFAULT_PROJECT_NAME);

                // FIXME workaround for bz#957110 and bz#957122
                new PackageExplorer().getProject(DEFAULT_PROJECT_NAME).getProjectItem("src/main/java");
                RegexMatchers m = new RegexMatchers("Source.*", "Organize Imports.*");
                new ContextMenu(m.getMatchers()).select();
                new WaitWhile(new ShellWithTextIsActive("Progress Information"), TimePeriod.LONG);
            }
        }
    }

    @After
    public void cleanUp() {
        // close shells
        Bot.get().closeAllShells();
        // save and close editors
        Bot.get().saveAllEditors();
        Bot.get().closeAllEditors();

        // delete all projects
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        for (Project p : explorer.getProjects()) {
            p.delete(true);
        }

        // delete all runtimes
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        for (DroolsRuntime runtime : pref.getDroolsRuntimes()) {
            pref.removeDroolsRuntime(runtime.getName());
        }
        pref.okCloseWarning();
    }

    protected String createTempDir(String name) {
        File dir = new File("tmp", name);
        dir.mkdirs();

        return dir.getAbsolutePath();
    }

    private <T extends Annotation> T getAnnotationOnMethod(String methodName, Class<T> annotationClass) {
        Method m = null;
        try {
            m = getClass().getMethod(methodName);
        } catch (NoSuchMethodException ex) {
            return null;
        }
        return m.getAnnotation(annotationClass);
    }
}

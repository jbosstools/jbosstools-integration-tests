package org.jboss.tools.drools.ui.bot.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.lookup.impl.ShellLookup;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.reddeer.workbench.view.View;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage.DroolsRuntime;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.Activator;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
@RunWith(RedDeerSuite.class)
public abstract class TestParent {
    private static final Logger LOGGER = Logger.getLogger(TestParent.class);
    private static final Properties TEST_PARAMS = new Properties();
    private static final String LOCAL_RUNTIME = new File("tmp/runtime").getAbsolutePath();

    protected static final String DEFAULT_DROOLS_RUNTIME_NAME = "defaultTestRuntime";
    protected static final String DEFAULT_DROOLS_RUNTIME_LOCATION;
    protected static final String DEFAULT_PROJECT_NAME = "defaultTestProject";
    protected static final String RESOURCES_LOCATION = "src/main/resources";
    protected static final String DEFAULT_RULES_PATH = DEFAULT_PROJECT_NAME + "/" + RESOURCES_LOCATION + "/rules";

    @Rule
    public TestName name = new TestName();

    @Rule
    public TestWatcher watcher = new ScreenshotTestWatcher();

    @ClassRule
    public static TestWatcher classWatcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOGGER.info(String.format("%n%n%25s Starting [%s]%n", "", description.getClassName()));
        }
    };

    @Rule
    public TestWatcher resultWatcher = new TestWatcher() {
        protected void starting(Description description) {
            LOGGER.info(String.format("==== %s ====", description.getMethodName()));
        };

        protected void succeeded(Description description) {
            LOGGER.info(String.format("succeded %s - %s", description.getClassName(), description.getMethodName()));
        };

        protected void skipped(AssumptionViolatedException e, Description description) {
            LOGGER.info(String.format("skipped %s - %s", description.getClassName(), description.getMethodName()));
        }

        protected void failed(Throwable e, Description description) {
            LOGGER.warn(String.format("failed %s - %s", description.getClassName(), description.getMethodName()));
        };
    };

    static {
        try {
            LOGGER.info("Loading properties for Drools tests");
            // Read project properties
            Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
            InputStream is = bundle.getResource("project.properties").openStream();
            TEST_PARAMS.load(is);
            LOGGER.info("Properties for Drools test loaded");
        } catch (Exception ex) {
            LOGGER.warn("External properties were not loaded.");
            //fail(ex.getMessage());
        }

        DEFAULT_DROOLS_RUNTIME_LOCATION = TEST_PARAMS.getProperty("drools.default.location", LOCAL_RUNTIME);
    }

    @BeforeClass
    public static void closeStartUpDialogsAndViews() {
        try {
            new DefaultShell("JBoss Developer Studio Usage");
            new PushButton("No").click();
        } catch (Exception ex) {
            LOGGER.info("JBoss Tools Usage dialog was not found.");
        }

        try {
            new View("Welcome") {}.close();
        } catch (Exception ex) {
            LOGGER.info("Eclipse Welcome view not found.");
        }

        try {
            new DefaultEditor("JBoss Central").close();
        } catch (Exception ex) {
            LOGGER.info("JBoss Central editor was not found.");
        }

        // maximizes the window
        final org.eclipse.swt.widgets.Shell shell = new ShellLookup().getActiveShell();
        Display.syncExec(new Runnable() {
            public void run() {
                shell.setMaximized(true);
            }
        });

        // creates the default runtime if it is necessary
        if (LOCAL_RUNTIME.equals(DEFAULT_DROOLS_RUNTIME_LOCATION)) {
            new File(LOCAL_RUNTIME).mkdirs();
            DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
            pref.open();
            DroolsRuntimeDialog diag = pref.addDroolsRuntime();
            diag.setName("creating default runtime");
            diag.createNewRuntime(LOCAL_RUNTIME);
            diag.ok();
            pref.okCloseWarning();
        }
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
                //wiz.createDefaultProjectWithAllSamples(DEFAULT_PROJECT_NAME);
                // FIXME temporarily disable samples other than *.drl (BZ#1001990)
                wiz.open();
                wiz.getFirstPage().setProjectName(DEFAULT_PROJECT_NAME);
                wiz.getDroolsRuntimePage().setGAV("com.redhat", "brms", "6.0.0.ER2-SNAPSHOT");
                wiz.finish();
            }
        }
    }

    @After
    public void cleanUp() {
        // close shells
        new SWTWorkbenchBot().closeAllShells();
        // save and close editors
        while (true) {
            try {
                new DefaultEditor().close(true);
            } catch (Exception ex) {
                break;
            }
        }

        // refresh and delete all projects (as running the projects creates logs)
        PackageExplorer explorer = new PackageExplorer();
        while (explorer.getProjects().size() > 0) {
            explorer.getProjects().get(0).delete(true);
            explorer = new PackageExplorer();
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

    protected static String getTemplateText(String templateName) {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
        StringWriter w = new StringWriter();
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new InputStreamReader(bundle.getResource(templateName).openStream()));
            pw = new PrintWriter(w);
            String l;
            while ((l = br.readLine()) != null) {
                pw.println(l);
            }
        } catch (IOException ex) {
            LOGGER.error("Unable to close template stream", ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error closing BufferedReader", ex);
            }
            pw.close();
        }

        return w.toString();
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

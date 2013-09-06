package org.jboss.tools.drools.reddeer.test.util;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public final class RunUtility {
    private static final Logger LOGGER = Logger.getLogger(RunUtility.class);

    private RunUtility() {
    }

    public static void runAsJavaApplication(String projectName, String... path) {
        runAsJavaApplication(false, projectName, path);
    }

    public static void runAsJavaApplication(boolean useContextMenu, String projectName, String... path) {
        selectProject(projectName, path);

        if (useContextMenu) {
            new ContextMenu(new RegexMatchers("Run As", ".*Java Application.*").getMatchers()).select();
        } else {
            new ShellMenu(new RegexMatchers("Run", "Run As", ".*Java Application.*").getMatchers()).select();
        }

        waitAfterStarting();
    }

    public static void debugAsDroolsApplication(String projectName, String... path) {
        debugAsDroolsApplication(false, projectName, path);
    }

    public static void debugAsDroolsApplication(boolean useContextMenu, String projectName, String... path) {
        selectProject(projectName, path);

        if (useContextMenu) {
            new ContextMenu(new RegexMatchers("Debug As", ".*Drools Application.*").getMatchers()).select();
        } else {
            new ShellMenu(new RegexMatchers("Run", "Debug As.*", ".*Drools Application.*").getMatchers()).select();
        }

        waitAfterStarting();
    }

    private static void selectProject(String projectName, String... path) {
        PackageExplorer explorer = new PackageExplorer();
        explorer.getProject(projectName).getProjectItem(path).select();
    }

    private static void waitAfterStarting() {
        try {
            new DefaultShell("Progress Information");
            new WaitWhile(new ShellWithTextIsActive("Progress Information"), TimePeriod.VERY_LONG);
        } catch (Exception ex) {
            LOGGER.debug("'Progress Information' shell was not shown.");
        }
    }
}

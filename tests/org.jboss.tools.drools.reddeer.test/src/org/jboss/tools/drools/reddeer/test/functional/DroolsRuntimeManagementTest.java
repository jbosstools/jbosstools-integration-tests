package org.jboss.tools.drools.reddeer.test.functional;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage.DroolsRuntime;
import org.jboss.tools.drools.reddeer.test.util.SmokeTest;
import org.jboss.tools.drools.reddeer.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class DroolsRuntimeManagementTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(DroolsRuntimeManagementTest.class);

    @Test
    @Category(SmokeTest.class)
    public void testAddAndRemoveRuntime() {
        final String name = "testRuntimeCreation";

        // add new runtime
        DroolsRuntimeManagementTest.addRuntime(name, DEFAULT_DROOLS_RUNTIME_LOCATION, false);

        // check that runtime was added
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntime runtime = null;
        for (DroolsRuntime r : pref.getDroolsRuntimes()) {
            if (r.getName().equals(name)) {
                runtime = r;
                break;
            }
        }
        Assert.assertNotNull("Runtime was not created.", runtime);

        // remove runtime
        pref.removeDroolsRuntime(name);

        // check that runtime was deleted
        for (DroolsRuntime r : pref.getDroolsRuntimes()) {
            Assert.assertNotSame("Runtime was not deleted.", name, r.getName());
        }
    }

    @Test
    public void testWrongLocation() {
        final String name = "testWrongLocation";
        final String location = "/path/to/runtime";

        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
        wiz.setName(name);
        wiz.setLocation(location);

        Assert.assertFalse("It is possible to save invalid path to runtime.", wiz.isValid());
        wiz.cancel();
        pref.cancel();
    }

    @Test
    public void testDuplicateName() {
        final String name = "testDuplicateName";
        addRuntime(name, DEFAULT_DROOLS_RUNTIME_LOCATION, false);

        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();

        DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
        wiz.setName(name);
        wiz.setLocation(DEFAULT_DROOLS_RUNTIME_LOCATION);

        Assert.assertFalse("It is possible to save runtime with duplicate name.", wiz.isValid());
        wiz.cancel();
        pref.cancel();
    }

    @Test
    public void testSetDefaultRuntime() {
        final String name1 = "testSetDefaultRuntime1";
        final String name2 = "testSetDefaultRuntime2";

        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        addRuntime(name1, DEFAULT_DROOLS_RUNTIME_LOCATION, false, pref);
        addRuntime(name2, DEFAULT_DROOLS_RUNTIME_LOCATION, false, pref);
        boolean warning = pref.okCloseWarning();
        Assert.assertFalse("Warning was shown although the default runtime was not set.", warning);

        pref.open();
        pref.setDroolsRuntimeAsDefault(name1);
        warning = pref.okCloseWarning();
        Assert.assertFalse("Warning was not shown although the default runtime has changed.", warning);

        pref.open();
        Table table = new DefaultTable();
        for (int row = 0; row < table.rowCount(); row++) {
            if (table.getItem(row).getText(0).equals(name1)) {
                Assert.assertTrue("Default runtime is not checked", table.getItem(row).isChecked());
            } else {
                Assert.assertFalse("Other than default runtime is checked", table.getItem(row).isChecked());
            }
        }

        pref.setDroolsRuntimeAsDefault(name2);
        warning = pref.okCloseWarning();
        Assert.assertTrue("Warning was  not shown although the default runtime has changed.", warning);

        pref.open();
        table = new DefaultTable();
        for (int row = 0; row < table.rowCount(); row++) {
            if (table.getItem(row).getText(0).equals(name2)) {
                Assert.assertTrue("Default runtime is not checked", table.getItem(row).isChecked());
            } else {
                Assert.assertFalse("Other than default runtime is checked", table.getItem(row).isChecked());
            }
        }
    }

    @Test
    public void testDeleteDefaultRuntime() {
        final String name1 = "testDeleteDefaultRuntime1";
        final String name2 = "testDeleteDefaultRuntime2";
        final String name3 = "testDeleteDefaultRuntime3";
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        addRuntime(name1, DEFAULT_DROOLS_RUNTIME_LOCATION, true, pref);
        addRuntime(name2, DEFAULT_DROOLS_RUNTIME_LOCATION, false, pref);
        addRuntime(name3, DEFAULT_DROOLS_RUNTIME_LOCATION, false, pref);
        pref.okCloseWarning();

        pref.open();
        Table table = new DefaultTable();
        for (int row = 0; row < table.rowCount(); row++) {
            if (table.getItem(row).getText(0).equals(name1)) {
                Assert.assertTrue("Default runtime is not checked", table.getItem(row).isChecked());
            } else {
                Assert.assertFalse("Other than default runtime is checked", table.getItem(row).isChecked());
            }
        }
        pref.removeDroolsRuntime(name1);

        Assert.assertEquals("Default runtime was not deleted.", 2, pref.getDroolsRuntimes().size());
        table = new DefaultTable();
        for (int row = 0; row < table.rowCount(); row++) {
            Assert.assertFalse("Default runtime is set although it was deleted earlier", table.getItem(row).isChecked());
        }

        pref.setDroolsRuntimeAsDefault(name2);
        pref.removeDroolsRuntime(name2);

        Assert.assertEquals("Default runtime was not deleted.", 1, pref.getDroolsRuntimes().size());
        table = new DefaultTable();
        Assert.assertEquals("Wrong runtimes deleted.", name3, table.getItem(0).getText(0));
        // we know about this inconsistency
        Assert.assertFalse("Default runtime is set although it was deleted earlier", table.getItem(0).isChecked());
    }

    @Test
    public void testCreateRuntime() {
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
        wiz.setName("testCreateRuntime");
        wiz.createNewRuntime(createTempDir("testCreateRuntime"));
        Assert.assertTrue("Impossible to use created runtime.", wiz.isValid());
        wiz.ok();
        Assert.assertEquals("Runtime was not created.", 1, pref.getDroolsRuntimes().size());
        pref.ok();
    }

    @Test
    public void testApply() {
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
        wiz.setName(name.getMethodName());
        wiz.setLocation(DEFAULT_DROOLS_RUNTIME_LOCATION);
        Assert.assertTrue("Impossible to use created runtime.", wiz.isValid());
        wiz.ok();

        Assert.assertEquals("The runtime was not created!", 1, pref.getDroolsRuntimes().size());
        pref.setDroolsRuntimeAsDefault(name.getMethodName());
        Assert.assertNotNull("The default runtime was not set!", pref.getDefaultDroolsRuntime());
        pref.apply();
        try {
            new DefaultShell("Warning");
            new PushButton("OK").click();
        } catch (Exception ex) {
            LOGGER.info("'Default runtime changed' warning was not shown.");
        }

        try {
            Assert.assertNotNull("The default runtime was reset!", pref.getDefaultDroolsRuntime());
        } finally {
            pref.cancel();
        }
    }

    public static void addRuntime(String name, String location, boolean setAsDefault) {
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        addRuntime(name, location, setAsDefault, pref);
        pref.okCloseWarning();
    }

    private static void addRuntime(String name, String location, boolean setAsDefault, DroolsRuntimesPreferencePage pref) {
        DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
        wiz.setName(name);
        wiz.setLocation(location);
        wiz.ok();

        if (setAsDefault) {
            pref.setDroolsRuntimeAsDefault(name);
        }

        Collection<DroolsRuntime> runtimes = pref.getDroolsRuntimes();
        Assert.assertNotSame("No runtimes are present.", 0, runtimes.size());

        DroolsRuntime runtime = getRuntime(name, pref);
        Assert.assertNotNull("Requested runtime was not created.", runtime);
    }

    private static DroolsRuntime getRuntime(String name, DroolsRuntimesPreferencePage pref) {
        for (DroolsRuntime runtime : pref.getDroolsRuntimes()) {
            if (runtime.getName().equals(name)) {
                return runtime;
            }
        }
        return null;
    }
}

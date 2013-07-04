package org.jboss.tools.drools.reddeer.preference;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;

public class DroolsRuntimesPreferencePage extends PreferencePage {
    private static final Logger LOGGER = Logger.getLogger(DroolsRuntimesPreferencePage.class);

    public DroolsRuntimesPreferencePage() {
        super("Drools", "Installed Drools Runtimes");
    }

    public DroolsRuntimeDialog addDroolsRuntime() {
        new PushButton("Add...").click();
        return new DroolsRuntimeDialog();
    }

    public DroolsRuntimeDialog editDroolsRuntime(String name) {
        selectDroolsRuntime(name);
        new PushButton("Edit...").click();
        return new DroolsRuntimeDialog();
    }

    public void removeDroolsRuntime(String name) {
        selectDroolsRuntime(name);
        new PushButton("Remove").click();
    }

    public void setDroolsRuntimeAsDefault(String name) {
        new DefaultTable().check(name);
    }

    public Collection<DroolsRuntime> getDroolsRuntimes() {
        Table table = new DefaultTable();

        Collection<DroolsRuntime> result = new ArrayList<DroolsRuntimesPreferencePage.DroolsRuntime>();
        String name, location;
        for (int i = 0; i < table.rowCount(); i++) {
            name = table.cell(i, 0);
            location = table.cell(i, 1);
            result.add(new DroolsRuntime(name, location));
        }

        return result;
    }

    public static class DroolsRuntime {
        private final String name;
        private final String location;

        public DroolsRuntime(String name, String location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }
    }

    public boolean okCloseWarning() {
        ok();
        LOGGER.warn(new DefaultShell().getText());
        try {
            new DefaultShell("Warning");
            new PushButton("OK").click();
            return true;
        } catch (Exception ex) {
            LOGGER.info("Default Drools runtime changed warning not shown.");
            return false;
        }
    }

    private void selectDroolsRuntime(String name) {
        Table t = new DefaultTable();
        for (int i = 0; i < t.rowCount(); i++) {
            if (t.cell(i, 0).equals(name)) {
                t.select(i);
                break;
            }
        }
    }
}

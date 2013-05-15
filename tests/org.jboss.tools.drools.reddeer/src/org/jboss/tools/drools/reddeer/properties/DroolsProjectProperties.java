package org.jboss.tools.drools.reddeer.properties;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatchers;

public class DroolsProjectProperties {
    private final String projectName;

    public DroolsProjectProperties(String projectName) {
        this.projectName = projectName;
    }

    public void open() {
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(projectName).select();
        RegexMatchers m = new RegexMatchers("Project", "Properties.*");
        new ShellMenu(m.getMatchers()).select();
        new DefaultTreeItem("Drools").select();
    }

    public void setEnableSpecificSettings(boolean value) {
        new CheckBox("Enable project specific settings").toggle(value);
    }

    public void setDefaultDroolsRuntime(String runtimeName) {
        setEnableSpecificSettings(true);
        // FIXME space after label text
        new DefaultCombo("Drools Runtime: ").setSelection(runtimeName);
    }

    public void ok() {
        new PushButton("OK").click();
    }

    public void cancel() {
        new PushButton("Cancel").click();
    }
}

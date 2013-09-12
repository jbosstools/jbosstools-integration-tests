package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewDroolsProjectSelectRuntimeWizardPage extends WizardPage {
    private static final String LABEL_DROOLS_RUNTIME = "Drools Runtime:";
    private static final String LABEL_DROOLS_CODE = "Generate code compatible with:";

    public NewDroolsProjectSelectRuntimeWizardPage() {
    }

    public void setUseDefaultRuntime(boolean value) {
        new CheckBox().toggle(value);
    }

    public void selectRuntime(String runtimeName) {
        setUseDefaultRuntime(false);
        new DefaultCombo(LABEL_DROOLS_RUNTIME).setSelection(runtimeName);
    }

    public void setCodeCompatibleWithVersion(CodeCompatibility version) {
        new DefaultCombo(LABEL_DROOLS_CODE).setSelection(version.toString());
    }

    public void setGAV(String groupId, String artifactId, String version) {
        setGroupId(groupId);
        setArtifactId(artifactId);
        setVersion(version);
    }

    public void setGroupId(String groupId) {
        // FIXME space after colon
        new LabeledText("GroupId: ").setText(groupId);
    }

    public void setArtifactId(String artifactId) {
        // FIXME space after colon
        new LabeledText("ArtifactId: ").setText(artifactId);
    }

    public void setVersion(String version) {
        // FIXME space after colon
        new LabeledText("Version: ").setText(version);
    }

    public enum CodeCompatibility {
        Drools4x("Drools 4.x"),
        Drools50x("Drools 5.0.x"),
        Drools51OrAbove("Drools 5.1.x or above"),
        Drools60x("Drools 6.0.x");

        private final String label;

        private CodeCompatibility(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }
}

package org.jboss.tools.drools.reddeer.preference;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

/**
 * Drools preference page.
 */
public class DroolsPreferencePage extends PreferencePage {
    private static final String LABEL_RULES_REPARSE = "Automatically reparse all rules if a Java resource is changed.";
    private static final String LABEL_CROSSREFERENCE = "Allow cross references in DRL files.";
    private static final String LABEL_FOLDING = "Use code folding in DRL editor.";
    private static final String LABEL_CACHE_PARSED_RULES = "When parsing rules, always chache the result for future use. "
            + "Warning: when disabled, debugging of rules will not work.";
    private static final String LABEL_PREFERRED_PROCESS_SKIN = "Preferred process skin:";
    private static final String LABEL_ALOLOW_NODES_CUSTOMIZATION = "Allow the customization of process nodes.";
    private static final String LABEL_INTERNAL_CLASSES = "Internal Drools classes are:";

    public DroolsPreferencePage() {
        super("Drools");
    }

    /**
     * Automatically reparse all rules if a Java resource is changed.
     */
    public void setAutomaticRulesReparse(boolean value) {
        toggleCheckBox(LABEL_RULES_REPARSE, value);
    }

    /**
     * Allow cross references in DRL files.
     */
    public void setAllowCrossReferenceInRules(boolean value) {
        toggleCheckBox(LABEL_CROSSREFERENCE, value);
    }

    /**
     * Use code folding in DRL editor.
     */
    public void setUseFolding(boolean value) {
        toggleCheckBox(LABEL_FOLDING, value);
    }

    /**
     * When parsing rules, always chache the result for future use. Warning: when disabled, debugging of rules will not work.
     */
    public void setCacheParsedRules(boolean value) {
        toggleCheckBox(LABEL_CACHE_PARSED_RULES, value);
    }

    /**
     * Preferred process skin:
     */
    public void setPreferredProcessSkin(ProcessSkin skin) {
        new DefaultCombo(LABEL_PREFERRED_PROCESS_SKIN).setSelection(skin.toString());
    }

    /**
     * Allow the customization of process nodes.
     */
    public void setAllowCustomizationOfProcessNodes(boolean value) {
        toggleCheckBox(LABEL_ALOLOW_NODES_CUSTOMIZATION, value);
    }

    /**
     * Internal Drools classes are:
     */
    public void setInternalClassesAccessibility(InternalClassesAccessibility level) {
        new DefaultCombo(LABEL_INTERNAL_CLASSES).setSelection(level.toString());
    }

    private void toggleCheckBox(String label, boolean value) {
        new CheckBox(label).toggle(value);
    }

    public enum ProcessSkin {
        DEFAULT("default"), BPMN("BPMN"), BPMN2("BPMN2");
        private final String value;

        private ProcessSkin(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    public enum InternalClassesAccessibility {
        ACCESSIBLE("Accessible"), DISCOURAGED("Discouraged"), NOT_ACCESSIBLE("Not Accessible");
        private final String value;

        private InternalClassesAccessibility(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }
}

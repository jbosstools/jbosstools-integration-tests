package org.jboss.tools.drools.reddeer.dialog;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class DslLineDialog {

    public void setLanguageExpression(String expression) {
        new LabeledText("Language expression:").setText(expression);
    }

    public void setRuleMapping(String mapping) {
        new LabeledText("Rule mapping:").setText(mapping);
    }

    public void setObject(String object) {
        new LabeledText("Object:").setText(object);
    }

    public void setScope(Scope scope) {
        new DefaultCombo("Scope:").setSelection(scope.toString());
    }

    public void ok() {
        new PushButton("OK").click();
    }

    public void cancel() {
        new PushButton("Cancel").click();
    }

    public enum Scope {
        CONSEQUENCE("consequence"),
        CONDITION("condition"),
        KEYWORD("keyword"),
        ANY("*");

        private final String value;

        private Scope(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }

        public String toEditorString() {
            return String.format("[%s]", toString());
        }
    }
}

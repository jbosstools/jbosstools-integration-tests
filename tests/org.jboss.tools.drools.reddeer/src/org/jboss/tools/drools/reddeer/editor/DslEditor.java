package org.jboss.tools.drools.reddeer.editor;

import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.editor.DefaultEditor;
import org.jboss.tools.drools.reddeer.dialog.DslLineDialog;

public class DslEditor extends DefaultEditor {

    public String getDescription() {
        return new LabeledText("Description:").getText();
    }

    public void setDescription(String description) {
        new LabeledText("Description:").setText(description);
    }

    public List<DslLine> getDslLines() {
        List<DslLine> result = new LinkedList<DslLine>();
        DefaultTable table = new DefaultTable();

        String expr, map, obj, scope;
        TableItem item;
        for (int i = 0; i < table.rowCount(); i++) {
            item = table.getItem(i);
            expr = item.getText(0);
            map = item.getText(1);
            obj = item.getText(2);
            scope = item.getText(3);

            result.add(new DslLine(expr, map, obj, scope));
        }

        return result;
    }

    public void selectLine(DslLine line) {
        new DefaultTable().select(line.getExpression());
    }

    public DslLineDialog add() {
        new PushButton("Add").click();
        return new DslLineDialog();
    }

    public DslLineDialog copy() {
        new PushButton("Copy").click();
        return new DslLineDialog();
    }

    public DslLineDialog edit() {
        new PushButton("Edit").click();
        return new DslLineDialog();
    }

    public void remove() {
        new PushButton("Remove").click();
    }

    public void sort(SortBy sortBy) {
        new DefaultCombo("Sort by:").setSelection(sortBy.toString());
        new PushButton("Sort").click();
    }

    public enum SortBy {
        OBJECT("Object"),
        LANGUAGE_EXPRESSION("Language Expression"),
        RULE_LANGUAGE_MAPPING("Rule Language Mapping"),
        SCOPE("Scope");

        private final String value;
        private SortBy(String value) {
            this.value = value;
        }
        public String toString() {
            return value;
        }
    }

    public static class DslLine {
        private String expression;
        private String mapping;
        private String object;
        private String scope;

        public DslLine(String expression, String mapping, String object, String scope) {
            super();
            this.expression = expression;
            this.mapping = mapping;
            this.object = object;
            this.scope = scope;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public String getMapping() {
            return mapping;
        }

        public void setMapping(String mapping) {
            this.mapping = mapping;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String toString() {
            return String.format("DslLine: '[%s]%s=%s'", scope, expression, mapping);
        }
    }
}

package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class NewDroolsProjectSelectSamplesWizardPage extends WizardPage {
    public static final String LABEL_SAMPLE_RULE = "Add a sample HelloWorld rule file to this project.";
    public static final String LABEL_SAMPLE_RULE_CLASS = "Add a sample Java class for loading and executing the HelloWorld rules.";
    public static final String LABEL_SAMPLE_DTABLE = "Add a sample HelloWorld decision table file to this project.";
    public static final String LABEL_SAMPLE_DTABLE_CLASS = "Add a sample Java class for loading and executing the HelloWorld decision table.";
    public static final String LABEL_SAMPLE_PROCESS = "Add a sample HelloWorld process file to this project.";
    public static final String LABEL_SAMPLE_PROCESS_CLASS = "Add a sample Java class for loading and executing the HelloWorld process.";

    public NewDroolsProjectSelectSamplesWizardPage() {
    }

    public void setAddSampleRule(boolean value) {
        new CheckBox(LABEL_SAMPLE_RULE).toggle(value);
    }

    public void setAddSampleRuleClass(boolean value) {
        new CheckBox(LABEL_SAMPLE_RULE_CLASS).toggle(value);
    }

    public void setAddSampleDecisionTable(boolean value) {
        new CheckBox(LABEL_SAMPLE_DTABLE).toggle(value);
    }

    public void setAddSampleDecisionTableClass(boolean value) {
        new CheckBox(LABEL_SAMPLE_DTABLE_CLASS).toggle(value);
    }

    public void setAddSampleProcess(boolean value) {
        new CheckBox(LABEL_SAMPLE_PROCESS).toggle(value);
    }
    
    public void setAddSampleProcessClass(boolean value) {
        new CheckBox(LABEL_SAMPLE_PROCESS_CLASS).toggle(value);
    }

    public void checkAll() {
        setAddSampleRule(true);
        setAddSampleRuleClass(true);
        setAddSampleDecisionTable(true);
        setAddSampleDecisionTableClass(true);
        setAddSampleProcess(true);
        setAddSampleProcessClass(true);
    }

    public void uncheckAll() {
        setAddSampleRule(false);
        setAddSampleRuleClass(false);
        setAddSampleDecisionTable(false);
        setAddSampleDecisionTableClass(false);
        setAddSampleProcess(false);
        setAddSampleProcessClass(false);
    }
}

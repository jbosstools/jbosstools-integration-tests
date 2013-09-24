package org.jboss.tools.drools.reddeer.preference;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * Drools Flow nodes preference page:
 *      Drools -> Drools Flow nodes
 */
public class DroolsFlowNodesPreferencePage extends PreferencePage {

    public DroolsFlowNodesPreferencePage() {
        super("Drools", "Drools Flow nodes");
    }

    public void setShowRuleFlowGroup(boolean value) {
        toggleCheckBox("RuleFlowGroup", value);
    }

    public void setShowSplit(boolean value) {
        toggleCheckBox("Split", value);
    }

    public void setShowJoin(boolean value) {
        toggleCheckBox("Join", value);
    }

    public void setShowEventWait(boolean value) {
        toggleCheckBox("Event Wait", value);
    }

    public void setShowSubFlow(boolean value) {
        toggleCheckBox("SubFlow", value);
    }

    public void setShowAction(boolean value) {
        toggleCheckBox("Action", value);
    }

    public void setShowTimer(boolean value) {
        toggleCheckBox("Timer", value);
    }

    public void setShowFault(boolean value) {
        toggleCheckBox("Fault", value);
    }

    public void setShowEvent(boolean value) {
        toggleCheckBox("Event", value);
    }

    public void setShowHumanTask(boolean value) {
        toggleCheckBox("HumanTask", value);
    }

    public void setShowComposite(boolean value) {
        toggleCheckBox("Composite", value);
    }

    public void setShowForEach(boolean value) {
        toggleCheckBox("ForEach", value);
    }

    public void setShowWorkItems(boolean value) {
        toggleCheckBox("WorkItems", value);
    }

    /**
     * Toggles all check boxes to checked state.
     */
    public void checkAll() {
        setShowRuleFlowGroup(true);
        setShowSplit(true);
        setShowJoin(true);
        setShowEventWait(true);
        setShowSubFlow(true);
        setShowAction(true);
        setShowTimer(true);
        setShowFault(true);
        setShowEvent(true);
        setShowHumanTask(true);
        setShowComposite(true);
        setShowForEach(true);
        setShowWorkItems(true);
    }

    /**
     * Toggles all check boxes to unchecked state.
     */
    public void uncheckAll() {
        setShowRuleFlowGroup(false);
        setShowSplit(false);
        setShowJoin(false);
        setShowEventWait(false);
        setShowSubFlow(false);
        setShowAction(false);
        setShowTimer(false);
        setShowFault(false);
        setShowEvent(false);
        setShowHumanTask(false);
        setShowComposite(false);
        setShowForEach(false);
        setShowWorkItems(false);
    }

    private void toggleCheckBox(String label, boolean value) {
        new CheckBox(label).toggle(value);
    }
}

package org.jboss.tools.jbpm.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class JBPMProjectWizard extends NewWizardDialog {

	public JBPMProjectWizard() {
		super("JBoss jBPM", "jBPM 3 Project");
	}

	public JBPMProjectWizard setName(String name) {
		new LabeledText("Project name:").setText(name);
		return this;
	}

	public JBPMProjectWizard setRuntime(String runtime) {
		new DefaultCombo(0).setSelection(runtime);
		return this;
	}

	public JBPMProjectWizard checkSample() {
		new CheckBox().click();
		return this;
	}
}

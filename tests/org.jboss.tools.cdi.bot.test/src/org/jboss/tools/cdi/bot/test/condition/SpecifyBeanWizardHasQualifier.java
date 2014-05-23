package org.jboss.tools.cdi.bot.test.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.SpecifyBeanWizard;

public class SpecifyBeanWizardHasQualifier implements WaitCondition{
	
	private SpecifyBeanWizard spBeanDialogWizard;
	private String qualifier;
	
	public SpecifyBeanWizardHasQualifier(SpecifyBeanWizard spBeanDialogWizard, String qualifier){
		this.spBeanDialogWizard = spBeanDialogWizard;
		this.qualifier = qualifier;
	}

	public boolean test() {
		for (String availQualifer : spBeanDialogWizard.getAvailableQualifiers()) {
			if (availQualifer.equals(qualifier)) {
				return true;
			}
		} return false;
	}

	public String description() {
		return "Specify Bean wizard contains qualifier "+qualifier;
	}

}

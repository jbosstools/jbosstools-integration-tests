package org.jboss.tools.cdi.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.SpecifyBeanWizard;

public class QualifierIsFound implements WaitCondition{
	
	private SpecifyBeanWizard spBeanDialogWizard;
	private String qualifier;
	
	public QualifierIsFound(SpecifyBeanWizard spBeanDialogWizard, String qualifier){
		this.spBeanDialogWizard = spBeanDialogWizard;
		this.qualifier = qualifier;
	}

	@Override
	public boolean test() {
		return spBeanDialogWizard.getInBeanQualifiers().contains(qualifier);
	}

	@Override
	public String description() {
		return "Qualifier "+qualifier+" is found";
	}

}

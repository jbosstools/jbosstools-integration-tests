/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.button.LabeledCheckBox;

public class CDIInstallWizardPage extends WizardPage{
	
	public CDIInstallWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void toggleCreateBeansXml(boolean checked){
		new LabeledCheckBox("Generate beans.xml file:").toggle(checked);
	}
	
	public boolean isCreateBeansXml(){
		return new LabeledCheckBox("Generate beans.xml file:").isChecked();
	}

}

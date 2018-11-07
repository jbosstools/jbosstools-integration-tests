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
package org.jboss.tools.cdi.reddeer.xhtml;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class NewXHTMLFileWizardPage extends WizardPage{
	
	private static final String PARENT_FOLDER_LABEL = "Enter or select the parent folder:";
	private static final String FILE_NAME_LABEL = "File name:";
	
	public NewXHTMLFileWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}
	
	public void setDestination(String destination) {
		new LabeledText(PARENT_FOLDER_LABEL).setText(destination);
	}
	
	public void setName(String nameOfPage) {
		new LabeledText(FILE_NAME_LABEL).setText(nameOfPage);
	}

}

/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/
package org.jboss.tools.arquillian.ui.bot.reddeer.junit;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Page for JUnit test case definition
 * 
 * @author Lucia Jelinkova
 *
 */
public class JUnitTestCaseWizardPage extends WizardPage {

	public JUnitTestCaseWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void setSourceFolder(String text){
		new LabeledText("Source folder:").setText(text);
	}
	
	public void setPackage(String text){
		new LabeledText("Package:").setText(text);
	}
	
	public void setName(String text){
		new LabeledText("Name:").setText(text);
	}
	
	public void setSuperclass(String text){
		new LabeledText("Superclass:").setText(text);
	}
	
	public void setClassUnderTest(String text){
		new LabeledText("Class under test:").setText(text);
	}
}

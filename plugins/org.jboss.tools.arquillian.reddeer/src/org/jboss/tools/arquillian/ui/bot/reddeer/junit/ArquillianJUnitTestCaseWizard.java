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

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Wizard for creating Arquillian JUnit test cases
 * 
 * @author Lucia Jelinkova
 *
 */
public class ArquillianJUnitTestCaseWizard extends NewWizardDialog {

	public ArquillianJUnitTestCaseWizard() {
		super("Arquillian", "Arquillian JUnit Test Case");
	}
}

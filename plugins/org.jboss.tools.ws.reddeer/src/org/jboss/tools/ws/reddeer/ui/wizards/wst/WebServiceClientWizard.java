/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.reddeer.ui.wizards.wst;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Web Service Client wizard.<br/>
 *
 * {@link WebServiceClientWizardPage} is the first and the only wizard page.
 *
 * Web Services > Web Service Client
 *
 * @author Radoslav Rabara
 *
 */
public class WebServiceClientWizard extends NewWizardDialog {
	public WebServiceClientWizard() {
		super("Web Services", "Web Service Client");
	}
}

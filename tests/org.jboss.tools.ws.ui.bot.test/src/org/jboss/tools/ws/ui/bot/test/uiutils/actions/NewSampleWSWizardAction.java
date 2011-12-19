/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.uiutils.actions;

import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.SampleWSWizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Type;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;

public class NewSampleWSWizardAction extends NewFileWizardAction {

	private final Type type;

	public NewSampleWSWizardAction(Type type) {
		super();
		this.type = type;
	}

	@Override
	public SampleWSWizard run() {
		Wizard w = super.run();
		w.selectTemplate("Web Services", type.getLabel());
		w.next();
		return new SampleWSWizard(type);
	}

}

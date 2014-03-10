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
package org.jboss.tools.ws.ui.bot.test.uiutils.wizards;

public class WebServiceClientWizard extends WsWizardBase {

	@Override
	public String getSourceComboLabel() {
		return "Service definition:";
	}

	public void setClientProject(String name) {
		setTargetProject("Client project:", name);//, "Specify Client Project Settings"
	}
	
	public void setClientEARProject(String name) {
		setTargetProject("Client EAR project:", name);
	}
}

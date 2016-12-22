/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.portlet.bot.test.wizard;

import java.util.Arrays;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class NewPortletClassOptionsWizardPage extends WizardPage {

	public void setMethods(Methods... methods) {
		Arrays.asList(methods).forEach(method -> new CheckBox(method.getLabel()).toggle(true));
	}

	public enum Methods {
		
		INIT("init"),
		DESTROY("destroy"),
		GET_PORTLET_CONFIG("getPortletConfig"),
		DO_VIEW("doView"),
		DO_EDIT("doEdit"),
		DO_HELP("doHelp"),
		DO_DISPATCH("doDispatch"),
		PROCESS_ACTION("processAction"),
		RENDER("render");
		
		private String label;

		Methods(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
}

/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.reddeer.wst.jsdt.ui.wizard;


import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

/**
 * Wizard dialog for creating a JavaScript Source File.
 * @author Pavol Srna
 *
 */
public class NewJSFileWizardDialog extends NewMenuWizard{

	/**
	 * Constructs the wizard with JavaScript > JavaScript Source File.
	 */
	public NewJSFileWizardDialog() {
		super("New JavaScript file", "JavaScript", "JavaScript Source File");
	}
}

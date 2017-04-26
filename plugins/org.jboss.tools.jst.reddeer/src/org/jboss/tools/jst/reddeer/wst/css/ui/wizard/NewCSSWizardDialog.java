/*******************************************************************************
 * Copyright (c) 2007-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.reddeer.wst.css.ui.wizard;

import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;

/**
 * Wizard dialog for creating a CSS File.
 * @author vpakan
 */
public class NewCSSWizardDialog extends NewMenuWizard {
	/**
	 * Constructs the wizard with Web > HTML File.
	 */
	public NewCSSWizardDialog() {
		super("New CSS File", "Web", "CSS File");
	}
	
}
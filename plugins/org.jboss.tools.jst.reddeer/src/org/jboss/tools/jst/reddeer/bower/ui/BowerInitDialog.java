/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.reddeer.bower.ui;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;


/**
 * Represents the wizard for creating bower.json file
 * 
 * @author Pavol Srna
 *
 */
public class BowerInitDialog extends NewMenuWizard {

	public BowerInitDialog() {
		super("Bower Initialization Wizard", "JavaScript", "Bower Init");
	}
}

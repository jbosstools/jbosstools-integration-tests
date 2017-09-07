/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.easymport.reddeer.wizard;

import org.eclipse.reddeer.workbench.workbenchmenu.WorkbenchMenuWizardDialog;

/**
 * Reprezents SmartImport Wizard (File -> Open Projects from File System...).
 * @author rhopp
 *
 */

public class SmartImportWizard extends WorkbenchMenuWizardDialog {

	public SmartImportWizard() {
		super("Import Projects from File System or Archive", "File", "Open Projects from File System...");
	}
}

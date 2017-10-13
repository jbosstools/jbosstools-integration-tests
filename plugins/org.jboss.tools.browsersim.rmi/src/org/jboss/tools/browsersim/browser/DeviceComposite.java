/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.browser;

import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.widgets.AbstractControl;

public class DeviceComposite extends AbstractControl<org.jboss.tools.browsersim.ui.skin.DeviceComposite>{

	public DeviceComposite(Shell browsersimShell) {
		super(org.jboss.tools.browsersim.ui.skin.DeviceComposite.class, browsersimShell, 0);
	}

}

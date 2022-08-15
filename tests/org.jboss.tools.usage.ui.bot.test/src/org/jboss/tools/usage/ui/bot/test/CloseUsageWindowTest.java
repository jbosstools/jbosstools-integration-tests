/*******************************************************************************
 * Copyright (c) 2022 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.usage.ui.bot.test;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.junit.Test;

/** Testing closing usage dialog on load.
 * @author Tamara Babalova */

public class CloseUsageWindowTest {
	
	@Test
	public void closeWindowTest() {
		DefaultShell dialog = new DefaultShell("JBoss Tools Usage");
		dialog.setFocus();
		assert(dialog.isVisible());
		new PushButton("No").click();
		assert(dialog.isDisposed());
	}
}

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

package org.jboss.tools.central.test.ui.reddeer.prepareOffline;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.central.reddeer.preferences.OfflineSupportPreferencePage;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Running this test just creates file (targer/offlineCommand) which contains
 * command to cache all examples for offline usage.
 * 
 * @author rhopp
 *
 */

@RunWith(RedDeerSuite.class)
public class PrepareOfflineCommand {

	@Test
	public void prepareOffline() {
		WorkbenchPreferenceDialog prefDialog = new WorkbenchPreferenceDialog();
		OfflineSupportPreferencePage offlinePrefPage = new OfflineSupportPreferencePage();
		prefDialog.open();
		prefDialog.select(offlinePrefPage);
		String command = offlinePrefPage.getCommand();
		File file = new File("target/offlineCommand");
		writeCommandToFile(command, file);
		prefDialog.cancel();
	}

	private void writeCommandToFile(String command, File file) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print(command);
			writer.close();
		} catch (FileNotFoundException e) {
			fail("Unable to write to file");
		} finally {
			if (writer != null){
				writer.close();
			}
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.docker.preferences;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * 
 * @author jkopriva
 */

public class DockerMachinePreferencePage extends PreferencePage {

	private static final Logger log = Logger.getLogger(DockerMachinePreferencePage.class);

	public DockerMachinePreferencePage() {
		super("Docker", "Docker Machine");
	}

	public void setDockerMachine(String pathToDockerMachine) {
		new LabeledText("Docker Machine").setText(pathToDockerMachine);
	}

	public void setVMDriver(String pathToVMDriver) {
		new LabeledText("VM Driver").setText(pathToVMDriver);
	}

}

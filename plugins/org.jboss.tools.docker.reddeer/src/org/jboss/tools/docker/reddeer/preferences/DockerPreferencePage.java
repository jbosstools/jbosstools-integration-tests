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

package org.jboss.tools.docker.reddeer.preferences;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * 
 * @author jkopriva
 */

public class DockerPreferencePage extends PreferencePage {

	public DockerPreferencePage() {
		super("Docker");
	}

	public void setContainerRefreshRate(int seconds) {
		new LabeledText("Container Refresh Rate (seconds)").setText(String.valueOf(seconds));
	}
}

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
package org.jboss.tools.runtime.as.ui.bot.test.detector.cdk2;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

public class DetectCDK2 extends DetectRuntimeTemplate {

	private static final String SERVER_ID = "cdk";

	@Override
	protected String getPathID() {
		return SERVER_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime expectedServer = new Runtime();
		expectedServer.setName("Container Development Environment");
		expectedServer.setVersion("2.3");
		expectedServer.setType("CDK");
		expectedServer.setLocation(RuntimeProperties.getInstance().getRuntimePath(SERVER_ID)+"/components/rhel/rhel-ose");
		return Arrays.asList(expectedServer);
	}

}

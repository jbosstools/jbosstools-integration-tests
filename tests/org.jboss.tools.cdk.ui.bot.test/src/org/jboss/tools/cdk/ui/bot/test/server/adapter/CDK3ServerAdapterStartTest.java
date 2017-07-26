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
package org.jboss.tools.cdk.ui.bot.test.server.adapter;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CDK3ServerAdapterStartTest extends CDKServerAdapterAbstractTest {

	@Override
	protected boolean isCDK3() {
		return true;
	}

	@Override
	protected String getServerAdapter() {
		return SERVER_ADAPTER_3;
	}
	
	@BeforeClass
	public static void setup() {
		checkMinishiftParameters();
		addNewCDK3Server(CDK3_SERVER_NAME, SERVER_ADAPTER_3, MINISHIFT_HYPERVISOR, MINISHIFT_PATH);
	}
	
	@AfterClass
	public static void tearDown() {
		CDKTestUtils.deleteCDEServer(SERVER_ADAPTER_3);
	}
	
	@Test
	public void testStartServerAdapter() {
		startServerAdapter();
		getCDEServer().stop();
		assertEquals(ServerState.STOPPED, getCDEServer().getLabel().getState());
	}	

}

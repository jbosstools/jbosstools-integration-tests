/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.wsclient;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.junit.runner.RunWith;

/**
 * Test operates on Web Service Client Wizard, tests inherited
 * from {@link WSClientTestTemplate}
 * 
 * @author jlukas
 *
 */
@RunWith(RedDeerSuite.class)
public class WsClientTest extends WSClientTestTemplate {
	public WsClientTest() {
		super(WebServiceRuntime.JBOSS_WS);
	}
}

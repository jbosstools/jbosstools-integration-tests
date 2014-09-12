/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.sample.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.ws.ui.bot.test.sample.SampleSoapTestBase;
import org.junit.Test;

/**
 * Test operates on Simple Web Service Wizard
 * @author jjankovi
 *
 */
public class SimpleWebServiceTest extends SampleSoapTestBase {
	
	@Override
    protected String getWsProjectName() {
        return "SimpleSOAPWS";
    }

    @Test
    public void testSimpleWSService() {
    	IFile dd = getDD(getWsProjectName());

        assertTrue(dd.exists());
        createSimpleWS(getWsProjectName(), "HelloService", "sample", "SampleService");
        checkSOAPService(getWsProjectName(), "HelloService", "sample", "SampleService", "You");

        createSimpleWS(getWsProjectName(), "GreetService", "greeter", "Greeter");
        checkSOAPService(getWsProjectName(), "GreetService", "greeter", "Greeter", "Tester");
    }

}

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

import org.eclipse.core.resources.IFile;
import org.jboss.tools.ws.ui.bot.test.sample.SampleSoapTestBase;
import org.junit.Test;

/**
 * Test operates on Simple SOAP Web Service Wizard
 * @author jjankovi
 *
 */
public class SimpleSoapWebServiceTest extends SampleSoapTestBase {
	
	@Override
    protected String getWsProjectName() {
        return "SimpleSOAPWS";
    }

    @Test
    public void testSimpleSoapWS() {
    	IFile dd = getDD(getWsProjectName());
        if (!dd.exists()) {
            createDD(getWsProjectName());
        }
        assertTrue(dd.exists());
        createSimpleSOAPWS(getWsProjectName(), "HelloService", "sample", "SampleService");
        checkSOAPService(getWsProjectName(), "HelloService", "sample", "SampleService", "You");

        createSimpleSOAPWS(getWsProjectName(), "GreetService", "greeter", "Greeter");
        checkSOAPService(getWsProjectName(), "GreetService", "greeter", "Greeter", "Tester");
    }

}

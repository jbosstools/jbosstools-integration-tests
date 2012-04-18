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
 * Test operates on Sample SOAP Web Service Wizard
 * @author jjankovi
 *
 */
public class SampleSoapWebServiceTest extends SampleSoapTestBase {

    @Override
    protected String getWsProjectName() {
        return "SampleSOAPWS";
    }
    
    @Test
    public void testSampleSoapWS() {
    	IFile dd = getDD(getWsProjectName());
        if (!dd.exists()) {
            projectHelper.createDD(getWsProjectName());
        }
        assertTrue(dd.exists());
        createSampleSOAPWS(getWsProjectName(), "HelloService", "sample", "SampleService");
        checkSOAPService(getWsProjectName(), "HelloService", "sample", "SampleService", "You");

        createSampleSOAPWS(getWsProjectName(), "GreetService", "greeter", "Greeter");
        checkSOAPService(getWsProjectName(), "GreetService", "greeter", "Greeter", "Tester");
    }
    
}

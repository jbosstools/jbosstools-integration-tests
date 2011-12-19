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

package org.jboss.tools.ws.ui.bot.test.sample;

import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Type;

/**
 * 
 * @author jjankovi
 *
 */
public class SampleSoapTestBase extends SampleWSBase {

	protected void createSampleSOAPWS(String project, String name, String pkg, String cls) {
        createSampleService(Type.SOAP, project, name, pkg, cls, null);
    }
	
	protected void createSimpleSOAPWS(String project, String name, String pkg, String cls) {
        createSimpleService(Type.SOAP, project, name, pkg, cls, null);
    }
   
    protected void checkSOAPService(String project, String svcName, String svcPkg, String svcClass, String msgContent) {
        checkService(Type.SOAP, project, svcName, svcPkg, svcClass, msgContent, null);
    }
	
}

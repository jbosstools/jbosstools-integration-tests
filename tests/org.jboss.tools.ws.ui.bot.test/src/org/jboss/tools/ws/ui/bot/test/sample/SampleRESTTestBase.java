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
public class SampleRESTTestBase extends SampleWSBase {

	protected void createSampleRESTWS(String project, String name, String pkg,
			String cls, String appCls) {
		createSampleService(Type.REST, project, name, pkg, cls, appCls);		
	}
	
	protected void createSimpleRESTWS(String project, String name, String pkg,
			String cls, String appCls) {
		createSimpleService(Type.REST, project, name, pkg, cls, appCls);		
	}

	protected void checkRESTService(String project, String svcName,
			String svcPkg, String svcClass, String msgContent, String appCls) {
		checkService(Type.REST, project, svcName, svcPkg, svcClass, msgContent, appCls);		
	}

}

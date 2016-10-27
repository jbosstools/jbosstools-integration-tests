/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cordovasim.rmi;

import java.rmi.RemoteException;

import org.jboss.tools.browsersim.rmi.IBrowsersimHandler;
import org.jboss.tools.browsersim.rmi.SimUtil;

public class CordovasimUtil extends SimUtil {
	
	public static final String CS_HANDLER = "csHandler";
	public static final String MAIN_CLASS = "org.jboss.tools.cordovasim.CordovaSimRunner";

	public static void main(final String[] args) {
		try {
			IBrowsersimHandler bsHandler = new CordovasimHandler();	
			startRMI(bsHandler, CS_HANDLER, MAIN_CLASS, args);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

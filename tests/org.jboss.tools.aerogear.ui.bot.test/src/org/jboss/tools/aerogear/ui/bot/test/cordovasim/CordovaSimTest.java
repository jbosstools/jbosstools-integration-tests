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
package org.jboss.tools.aerogear.ui.bot.test.cordovasim;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.aerogear.reddeer.cordovasim.CordovaSimController;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.cordovasim.rmi.ICordovasimHandler;
import org.junit.AfterClass;
import org.junit.Test;

public class CordovaSimTest extends AerogearBotTest {
	
	protected static ICordovasimHandler csHandler;
	private static CordovaSimController csController = new CordovaSimController();
	public static final String APP_TEXT="Apache Cordova application powered by Eclipse Thym";
	
	@AfterClass
	public void closeCordova(){
		csController.stopCordovasim();
	}
	
	@Test
	public void cordovaSimAppIsLoaded() throws RemoteException{
		createHTMLHybridMobileApplication(AerogearBotTest.CORDOVA_PROJECT_NAME, AerogearBotTest.CORDOVA_APP_NAME,
				"org.jboss.example.cordova", "cordova-android@4.1.0");
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(AerogearBotTest.CORDOVA_PROJECT_NAME).select();
		
		ContextMenu m = new ContextMenu(new WithTextMatcher("Run As"), new RegexMatcher("(\\d+)( Run w/CordovaSim)"));
		csHandler = csController.launchCordovaSim(m);
		String appText = csHandler.getBrowserText();
		assertTrue("Cordova app text is browser "+appText, csHandler.getBrowserText().contains(APP_TEXT));
	}

}

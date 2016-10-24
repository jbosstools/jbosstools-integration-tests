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
package org.jboss.tools.browsersim.ui.bot.test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.List;

import org.jboss.tools.browsersim.reddeer.Skin;
import org.junit.BeforeClass;
import org.junit.Test;

public class BrowsersimTest extends BrowsersimBaseTest{
	
	@BeforeClass
	public static void prepareBrowsersim(){
		launchBrowsersim(null);
	}
	
	@Test
	public void checkSkinsInMenu() throws RemoteException{
		List<String> skins = bsHandler.getSkinsMenuItems();
		assertEquals(Skin.values().length, skins.size());
		for(Skin s: Skin.values()){
			assertTrue("Browsersim does not have skin "+s.getName(),checkSkin(skins, s));
		}
	}
	
	private boolean checkSkin(List<String> skins, Skin skin){
		for(String s: skins){
			if (s.equals(skin.getName())){
				return true;
			}
		} return false;
	}
	
	@Test
	public void testBrowserBackAndForward() throws RemoteException{
		bsHandler.openURL("www.redhat.com");
		assertTrue(bsHandler.getURL().contains("redhat"));
		bsHandler.openURL("www.google.com");
		assertTrue(bsHandler.getURL().contains("google"));
		bsHandler.browserBack();
		assertTrue(bsHandler.getURL().contains("redhat"));
		bsHandler.browserForward();
		assertTrue(bsHandler.getURL().contains("google"));
		
	}

}

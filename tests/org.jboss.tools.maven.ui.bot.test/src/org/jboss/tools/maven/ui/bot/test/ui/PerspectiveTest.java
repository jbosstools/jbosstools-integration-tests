/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.maven.ui.bot.test.ui;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.central.reddeer.perspective.JBossPerspective;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;

/**
 * @author Alexey Kazakov
 * @author Fred Bricon
 * @author Rastislav Wagner
 * 
 */
public class PerspectiveTest extends AbstractMavenSWTBotTest {

	/**
	 * Tests JBoss perspective has Maven stuff
	 * See https://issues.jboss.org/browse/JBIDE-10146
	 */
	@Test
	public void testJBossPerspective() {
		new JBossPerspective().open();
		try{
			new ShellMenu("File","New","Maven Project");
		} catch (SWTLayerException ex){
			fail("Maven project menu not found in JBoss perspective");
		}
	}
	
	@Test
	public void testJ2EEPerspective(){
		new JavaEEPerspective().open(); 
		try{
			new ShellMenu("File","New","Maven Project");
		} catch (SWTLayerException ex){
			fail("Maven project menu not found in Java EE perspective");
		}
	}
}
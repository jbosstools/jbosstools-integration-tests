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

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.jboss.tools.common.reddeer.perspectives.JBossPerspective;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alexey Kazakov
 * @author Fred Bricon
 * @author Rastislav Wagner
 * 
 */
@RunWith(RedDeerSuite.class)
public class PerspectiveTest extends AbstractMavenSWTBotTest {

	/**
	 * Tests JBoss perspective has Maven stuff
	 * See https://issues.jboss.org/browse/JBIDE-10146
	 */
	@Test
	public void testJBossPerspective() {
		new JBossPerspective().open();
		try{
			new ShellMenuItem("File","New","Maven Project");
		} catch (SWTLayerException ex){
			fail("Maven project menu not found in JBoss perspective");
		}
	}
	
	@Test
	public void testJ2EEPerspective(){
		new JavaEEPerspective().open(); 
		try{
			new ShellMenuItem("File","New","Maven Project");
		} catch (SWTLayerException ex){
			fail("Maven project menu not found in Java EE perspective");
		}
	}
}
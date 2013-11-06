/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * 1. injecting JmxBroadcaster will not fail -> no validation error 2. try to
 * open on JmxBroadcaster -> BroadcasterProducer should be opened
 * 
 * @author jjankovi
 * 
 */
public class JmxBroadcaster extends DeltaspikeTestBase {

	private Regex validationProblemRegex = new Regex("No bean is eligible.*");

	@After
	public void closeAllEditors() {
		new SWTWorkbenchBot().closeAllEditors();
		projectExplorer.deleteAllProjects();
	}

	@Test
	public void testJmxBroadcaster() {

		String projectName = "jmxBroadcaster";
		importDeltaspikeProject(projectName);

		new WaitWhile(new SpecificProblemExists(validationProblemRegex),
				TimePeriod.NORMAL);
		openClass(projectName, "test", "Test.java");
		OpenOnHelper.checkOpenOnFileIsOpened(bot, "Test.java",
				"jmxBroadcaster", "Open @Inject Bean",
				"BroadcasterProducer.class");
		try{
			new DefaultShell("Found source jar for 'deltaspike-core-impl-0.4.jar'");
			new PushButton("No").click();
			new WaitWhile(new ShellWithTextIsAvailable("Found source jar for 'deltaskipe-core-impl-0.4.jar'"));
		} catch (SWTLayerException ex){
			//do nothing because maven source lookup is not installed
		}

	}

}

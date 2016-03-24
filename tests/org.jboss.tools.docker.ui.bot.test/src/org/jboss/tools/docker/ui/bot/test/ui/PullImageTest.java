/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.docker.ui.bot.test.ui;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class PullImageTest extends AbstractDockerBotTest {
	
	@Test
	public void pullImageTest() {
		String dockerServerURI = System.getProperty("dockerServerURI");
		String imageName = System.getProperty("imageName");
		ConsoleView cview = new ConsoleView();
		cview.open();
		try {
			cview.clearConsole();
		} catch (CoreLayerException ex) {
			// there's not clear console button, since nothing run before
		}
		DockerExplorer de = new DockerExplorer();
		de.open();
		de.getConnection(dockerServerURI);
		new ContextMenu("Pull...").select();
		new WaitUntil(new ShellWithTextIsActive("Pull Image"), TimePeriod.NORMAL);
		new LabeledText("Name:").setText(imageName);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
	}

}

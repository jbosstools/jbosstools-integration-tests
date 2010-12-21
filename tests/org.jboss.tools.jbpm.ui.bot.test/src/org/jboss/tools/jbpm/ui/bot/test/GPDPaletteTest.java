/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.swt.graphics.Rectangle;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.gef.SWTArranger;
import org.jboss.tools.ui.bot.ext.gef.SWTBotGefEditorExt;
import org.junit.Test;

@SWTBotTestRequires(perspective = "jBPM jPDL 3", clearProjects = false, clearWorkspace = false)
public class GPDPaletteTest extends JBPMTest {

	@Test
	public void insertNodes() {

		String[] entities = { "Select", "Start", "State", "End", "Fork",
				"Join", "Decision", "Node", "TaskNode", "MailNode",
				"ESB Service", "Process State", "Super State", "Transition" };

		SWTBotGefEditorExt editor = new SWTBotGefEditorExt("simple");
		editor.insertEntity("Node", 100, 100);

		Rectangle cr = editor.getCanvasBounds();		
		SWTArranger arranger = new SWTArranger();
		arranger.setOrigin(cr);
		
		bot.sleep(TIME_10S);
	}
}

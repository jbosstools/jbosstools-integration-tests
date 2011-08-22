/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jbpm.ui.bot.test;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.tools.jbpm.ui.bot.test.suite.JBPMTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.gef.SWTBotGefEditorExt;
import org.jboss.tools.ui.bot.ext.gef.SWTBotGefFigure;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotMultiPageEditor;
import org.junit.Test;

@Require(perspective = "jBPM jPDL 3", clearProjects = false, clearWorkspace = false)
public class GPDTest extends JBPMTest {

	String[] nodes = { "start", "first", "end" };

	@Test
	public void selectNodes() {
		SWTGefBot gefBot = new SWTGefBot();
		SWTBotGefEditor editor = gefBot.gefEditor("simple");
		SWTBotMultiPageEditor multi = new SWTBotMultiPageEditor(
				editor.getReference(), gefBot);
		multi.activatePage("Diagram");

		String[] nodes = { "start", "first", "end" };

		for (String node : nodes) {
			editor.getEditPart(node).select();
		}
	}

	@Test
	public void resizeNodes() {

		SWTGefBot gefBot = new SWTGefBot();
		SWTBotGefEditor editor = gefBot.gefEditor("simple");

		for (String node : nodes) {
			editor.getEditPart(node).select().focus();
			editor.getEditPart(node).resize(PositionConstants.SOUTH_EAST, 100,
					70);
		}
		editor.save();
	}

	@Test
	public void moveNodes() {

		SWTGefBot gefBot = new SWTGefBot();
		final SWTBotGefEditor editor = gefBot.gefEditor("simple");
		final Rectangle[] abounds = new Rectangle[1];

		for (final String node : nodes) {

			bot.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					abounds[0] = ((GraphicalEditPart) editor.getEditPart(node)
							.part()).getFigure().getBounds();
				}

			});
			Rectangle bounds = abounds[0];
			editor.drag(editor.getEditPart(node), bounds.x + 100,
					bounds.y + 100);
		}
		editor.save();
	}

	@Test
	public void renameNodes() {
		SWTBotGefEditorExt editor = new SWTBotGefEditorExt("simple");

		for (String node : nodes) {
			SWTBotGefFigure label = editor.labelFigure(node);
			editor.setLabelText(label, node + "_NEXT");
			editor.save();
			log.info("Label: \"" + label.getText() + "\"");
			// TODO Bug, label is reported as unchanged although it's not, needs investigation
			// assertTrue(label.getText().equals(node + "_NEXT"));
			
		}
		bot.sleep(TIME_5S);
	}
}

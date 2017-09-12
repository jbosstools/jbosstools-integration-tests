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
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import org.eclipse.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks batch job.xml file from within source view and validation of looping elements
 * @author odockal
 *
 */
public class ValidateSourceElementLoopingTest extends AbstractJobXMLTest {

	@Override
	@Before
	public void setUp() {
		setupJobXML();
		setupEditor();
	}
	
	@Override
	@After
	public void tearDown() {
		closeEditor();
		removeJobXML();
	}
	
	@Test
	public void testStepLooping() {
		checkLoopingElements("<step id=\"my-id\" next=\"my-loop-id\"></step>", 
				"<step id=\"my-loop-id\" next=\"my-id\"></step>",
				0, 2);
	}
	
	@Test
	public void testSplitLooping() {
		checkLoopingElements("<split id=\"my-id\" next=\"my-loop-id\"></split>", 
				"<split id=\"my-loop-id\" next=\"my-id\"></split>",
				0, 2);
	}
	
	@Test
	public void testFlowLooping() {
		checkLoopingElements("<flow id=\"my-id\" next=\"my-loop-id\"></flow>", 
				"<flow id=\"my-loop-id\" next=\"my-id\"></flow>",
				0, 2);
	}
	
	@Test
	public void testDecisionLooping() {
		checkLoopingElements("<decision id=\"my-id\" ref=\"decider\">\r\n"
				+ "\t<next on=\"foo\" to=\"my-loop-id\"></next>\r\n"
				+ "\t</decision>", 
				"<decision id=\"my-loop-id\" ref=\"decider\">\r\n"
						+ "\t<next on=\"foo\" to=\"my-id\"></next>\r\n"
						+ "\t</decision>",
						0, 2);
	}
	
	@Test
	public void testTransitiveElementLooping() {
		checkLoopingElements("<step id=\"my-id\" next=\"my-next-id\"></step>\r\n"
				+ "\t<flow id=\"my-next-id\" next=\"my-loop-id\"></flow>",
				"<split id=\"my-loop-id\" next=\"my-id\"></split>",
				0, 3);
	}
	
	private void checkLoopingElements(String firstElement, String secondElement, int error, int warning) {
		XMLSourcePage source = editor.getSourcePage();
		source.activate();
		source.insertText(source.getPositionOfText("</job>"), "\t" + firstElement + "\r\n");
		editor.save();
		source.insertText(source.getPositionOfText("</job>"), "\t" + secondElement + "\r\n");
		editor.save();
		assertNumberOfProblems(error, warning);
	}
}

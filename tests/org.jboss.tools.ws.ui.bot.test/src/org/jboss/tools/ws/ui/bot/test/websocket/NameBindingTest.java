/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.websocket;

import static org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper.*;
import static org.jboss.tools.ws.ui.bot.test.websocket.NameBindingTest.Constants.*;
import static org.jboss.tools.ws.ui.bot.test.websocket.WebSocketHelper.*;
import static org.junit.Assert.*;

import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.common.reddeer.requirements.JavaFoldingRequirement.JavaFolding;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Tests hyperlinks with custom NameBinding annotation.
 *
 * @author Jan Novak
 */
@JavaFolding(false)
@AutoBuilding(false)
public class NameBindingTest extends WSTestBase {

	private TextEditor appEditor;
	private TextEditor filterEditor;
	private TextEditor annotationEditor;

	@Before
	public void setup() {
		setWsProjectName(PROJECT_NAME);

		if (!projectExists(getWsProjectName()))
			createProject(getWsProjectName());
		else
			throw new IllegalStateException("There is an unexpected project " + getWsProjectName());

		//prepare classes
		annotationEditor = prepareCustomAnnotationEditor(getWsProjectName());
		appEditor = prepareAppEditor(getWsProjectName());
		filterEditor = prepareFilterEditor(getWsProjectName());

		enableJAXRSSupport(getWsProjectName());
	}

	@After
	public void tearDown() {
		deleteAllProjects();
	}

	@Test
	public void test() {
		cleanAllProjects();
		assertProposals(appEditor, PROPOSAL_FILTER, PROPOSAL_APP);
		assertProposals(filterEditor, PROPOSAL_APP, PROPOSAL_FILTER);
		assertProposals(annotationEditor, Arrays.asList(PROPOSAL_APP, PROPOSAL_FILTER), new ArrayList<String>());
	}

	@Test
	public void duplicatedFilterTest() {
		TextEditor duplicateFilterEditor =
				duplicateClass(filterEditor, CLASS_NAME_FILTER, CLASS_NAME_FILTER_DUPLICATED);

		cleanAllProjects();

		assertProposals(appEditor,
				Arrays.asList(PROPOSAL_FILTER, PROPOSAL_FILTER_DUPLICATED),
				Arrays.asList(PROPOSAL_APP));

		assertProposals(filterEditor,
				Arrays.asList(PROPOSAL_APP, PROPOSAL_FILTER_DUPLICATED),
				Arrays.asList(PROPOSAL_FILTER));

		assertProposals(duplicateFilterEditor,
				Arrays.asList(PROPOSAL_FILTER, PROPOSAL_APP),
				Arrays.asList(PROPOSAL_FILTER_DUPLICATED));

		assertProposals(annotationEditor,
				Arrays.asList(PROPOSAL_APP, PROPOSAL_FILTER, PROPOSAL_FILTER_DUPLICATED),
				new ArrayList<String>());
	}

	@Test
	public void annotationRemoveTest() {
		TextEditor duplicateFilterEditor =
				duplicateClass(filterEditor, CLASS_NAME_FILTER, CLASS_NAME_FILTER_DUPLICATED);

		//remove CUSTOM_NAME_BINDING_ANNOTATION from original filter
		filterEditor.activate();
		filterEditor.setText(filterEditor.getText().replaceAll(CUSTOM_NAME_BINDING_ANNOTATION, ""));
		filterEditor.save();

		cleanAllProjects();

		assertProposals(appEditor,
				Arrays.asList(PROPOSAL_FILTER_DUPLICATED),
				Arrays.asList(PROPOSAL_FILTER, PROPOSAL_APP));

		assertProposals(duplicateFilterEditor,
				Arrays.asList(PROPOSAL_APP),
				Arrays.asList(PROPOSAL_FILTER, PROPOSAL_FILTER_DUPLICATED));

		assertProposals(annotationEditor,
				Arrays.asList(PROPOSAL_APP, PROPOSAL_FILTER_DUPLICATED),
				Arrays.asList(PROPOSAL_FILTER));
	}

	private void assertProposals(TextEditor editor, String expected, String unexpected) {
		assertProposals(editor, Arrays.asList(expected), Arrays.asList(unexpected));
	}

	private void assertProposals(TextEditor editor, Collection<String> expected, Collection<String> unexpected) {
		List<String> proposals = getAnnotationsProposals(editor, CLASS_NAME_ANNOTATION);
		assertTrue("There are not all expected proposals!" + proposals + expected,
				proposals.containsAll(expected));
		assertTrue("There are unexpected proposals!" + proposals + unexpected,
				Collections.disjoint(proposals, unexpected));
	}

	private TextEditor duplicateClass(TextEditor editor, String originalClassName, String newClassName) {
		editor.activate();
		String originalSource = editor.getText();
		String replacedSource = originalSource.replaceAll(originalClassName, newClassName);

		TextEditor newClassEditor = createClass(getWsProjectName(), PROJECT_PACKAGE, newClassName);
		newClassEditor.activate();
		newClassEditor.setText(replacedSource);
		newClassEditor.save();

		return newClassEditor;
	}

	private List<String> getAnnotationsProposals(TextEditor editor, String annotation) {
		editor.activate();

		int y = editor.getLineOfText(annotation);
		int x = editor.getTextAtLine(y).indexOf(annotation) + annotation.length() / 2;
		editor.setCursorPosition(y, x);

		ContentAssistant assistant = editor.openOpenOnAssistant();
		List<String> proposals = assistant.getProposals();
		if (assistant != null)
			assistant.close();

		return proposals;
	}

	static class Constants {

		static final String PROJECT_NAME = "nameBindingTestProject";
		static final String PROJECT_PACKAGE = "org.websocket.test";

		static final String CLASS_NAME_ANNOTATION = "CustomNameBinding";
		static final String CLASS_NAME_APP = "RestApplication";
		static final String CLASS_NAME_FILTER = "CustomResponseFilterWithBinding";
		static final String CLASS_NAME_FILTER_DUPLICATED = CLASS_NAME_FILTER + "Duplicated";

		static final String PROPOSAL_APP = "Open " + PROJECT_PACKAGE + "." + CLASS_NAME_APP;
		static final String PROPOSAL_FILTER = "Open " + PROJECT_PACKAGE + "." + CLASS_NAME_FILTER;
		static final String PROPOSAL_FILTER_DUPLICATED = "Open " + PROJECT_PACKAGE + "." + CLASS_NAME_FILTER_DUPLICATED;

		static final String CUSTOM_NAME_BINDING_ANNOTATION = "@" + CLASS_NAME_ANNOTATION;

	}
}

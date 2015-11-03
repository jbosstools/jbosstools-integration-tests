/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.smoke;

import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigEditor;
import org.jboss.tools.jsf.reddeer.ui.editor.FacesConfigSourceEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.helper.ContentAssistHelper;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * * Test Code Completion functionality of faces-config.xml file
 * 
 * @author Vladimir Pakan
 *
 */
public class FacesConfigCodeCompletionTest extends JSFAutoTestCase {

	private static final String FACES_CONFIG_FILE_NAME = "faces-config.xml";
	private FacesConfigEditor facesConfigEditor;
	private FacesConfigSourceEditor facesConfigSourceEditor;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		EditorHandler.getInstance().closeAll(true);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(VPEAutoTestCase.JBT_TEST_PROJECT_NAME)
				.getProjectItem("WebContent", "WEB-INF", FacesConfigEditingTest.FACES_CONFIG_FILE_NAME).open();
		facesConfigEditor = new FacesConfigEditor(FacesConfigCodeCompletionTest.FACES_CONFIG_FILE_NAME);
		facesConfigEditor.activateSourceTab();
		facesConfigSourceEditor = facesConfigEditor.getFacesConfigSourceEditor();
	}

	@Override
	public void tearDown() throws Exception {
		if (facesConfigEditor != null) {
			facesConfigEditor.save();
			facesConfigEditor.close();
		}
		super.tearDown();
	}

	/**
	 * Test Code Completion functionality of faces-config.xml file
	 */
	@Test
	public void testCodeCompletionOfFacesConfig() {
		// check Content Assist inside <faces-config> node
		ContentAssistHelper.checkContentAssistContent(facesConfigSourceEditor, 4, 0,
				getInsideFacesConfigTagProposalList(), true);
		// check Content Assist inside <managed-bean> node
		ContentAssistHelper.checkContentAssistContent(facesConfigSourceEditor, 9, 0,
				getInsideManagedBeanTagProposalList(), true);
		// check Content Assist inside <managed-property> node
		ContentAssistHelper.checkContentAssistContent(facesConfigSourceEditor, 10, 0,
				getInsideManagedPropertyTagProposalList(), true);
		// check Content Assist inside <navigation-rule> node
		ContentAssistHelper.checkContentAssistContent(facesConfigSourceEditor, 16, 0,
				getInsideNavigationRuleTagProposalList(), true);
		// check Content Assist inside <navigation-case> node
		ContentAssistHelper.checkContentAssistContent(facesConfigSourceEditor, 18, 0,
				getInsideNavigationCaseTagProposalList(), true);
	}

	/**
	 * Returns list of expected proposals inside <faces-config> tag
	 * 
	 * @return
	 */
	private static List<String> getInsideFacesConfigTagProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("application");
		result.add("component");
		result.add("converter");
		result.add("faces-config-extension");
		result.add("factory");
		result.add("lifecycle");
		result.add("managed-bean");
		result.add("navigation-rule");
		result.add("referenced-bean");
		result.add("render-kit");
		result.add("validator");
		result.add("XSL processing instruction - XSL processing instruction");
		result.add("comment - xml comment");

		return result;
	}

	/**
	 * Returns list of expected proposals inside <managed-bean> tag
	 * 
	 * @return
	 */
	private static List<String> getInsideManagedBeanTagProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("managed-property");
		result.add("description");
		result.add("display-name");
		result.add("icon");
		result.add("list-entries");
		result.add("managed-bean-class");
		result.add("managed-bean-extension");
		result.add("managed-bean-name");
		result.add("managed-bean-scope");
		result.add("map-entries");
		result.add("XSL processing instruction - XSL processing instruction");
		result.add("comment - xml comment");
		return result;
	}

	/**
	 * Returns list of expected proposals inside <managed-property> tag
	 * 
	 * @return
	 */
	private static List<String> getInsideManagedPropertyTagProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("description");
		result.add("display-name");
		result.add("icon");
		result.add("list-entries");
		result.add("map-entries");
		result.add("null-value");
		result.add("property-class");
		result.add("property-name");
		result.add("value");
		result.add("XSL processing instruction - XSL processing instruction");
		result.add("comment - xml comment");
		return result;
	}

	/**
	 * Returns list of expected proposals inside <navigation-rule> tag
	 * 
	 * @return
	 */
	private static List<String> getInsideNavigationRuleTagProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("description");
		result.add("display-name");
		result.add("icon");
		result.add("from-view-id");
		result.add("navigation-case");
		result.add("navigation-rule-extension");
		result.add("XSL processing instruction - XSL processing instruction");
		result.add("comment - xml comment");
		return result;
	}

	/**
	 * Returns list of expected proposals inside <navigation-case> tag
	 * 
	 * @return
	 */
	private static List<String> getInsideNavigationCaseTagProposalList() {
		LinkedList<String> result = new LinkedList<String>();

		result.add("description");
		result.add("display-name");
		result.add("from-action");
		result.add("icon");
		result.add("from-outcome");
		result.add("redirect");
		result.add("to-view-id");
		result.add("XSL processing instruction - XSL processing instruction");
		result.add("comment - xml comment");
		return result;
	}

}
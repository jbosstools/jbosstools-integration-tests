/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.annotation;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.reddeer.ui.views.AnnotationPropertiesView;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.Asserts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Checks behaviour of AnnotationProperties view and its 
 * impact on JAX-RS explorer 
 * 
 * @author jjankovi
 *
 * Also improve tests (mainly testAbsenceOfAnnotation() and testPresenceOfAnnotation())
 * 
 */
@RunWith(RedDeerSuite.class)
public class AnnotationPropertiesTest extends RESTfulTestBase {

	@Override
	public String getWsProjectName() {
		return "restAdvanced";
	}

	@Override
	public void setup() {
		importWSTestProject(getWsProjectName());
		openJavaFile(getWsProjectName(), getWsPackage(), getWsName() + ".java");
	}

	/**
	 * 1 there are no incorrectly checked annotations
	 * 2 there are no incorrectly unchecked annotations 
	 * 
	 * @author Radoslav Rabara
	 */
	@Test
	public void testAbsenceOfAnnotation() {
		/** check params of annotation is synchronized **/
		navigateInActiveEditor(13, 0);

		AnnotationPropertiesView annotationsView = new AnnotationPropertiesView();
		annotationsView.open();
		
		List<TreeItem> allAnnotations = annotationsView.getAllAnnotations();
		List<TreeItem> deactiveAnnotations = annotationsView.getAllDeactiveAnnotation();

		assertThat(deactiveAnnotations.size(), Is.is(allAnnotations.size()-1));

		for(TreeItem item : deactiveAnnotations) {
			assertThat("Path annotation isn't deactivated", item.getText(), IsNot.not("javax.ws.rs.Path"));
		}
	}

	/**
	 * 1 there are correctly checked annotations
	 * 2 there are correctly unchecked annotations
	 */
	@Test
	public void testPresenceOfAnnotation() {
		/** check params of annotation is synchronized **/
		navigateInActiveEditor(13, 0);

		AnnotationPropertiesView annotationsView = new AnnotationPropertiesView();
		annotationsView.open();

		List<TreeItem> activeAnnotations = annotationsView.getAllActiveAnnotation();

		assertThat("Only one annotation should be active but following annotations are active:\n"
				+ Arrays.toString(activeAnnotations.toArray()), activeAnnotations.size(), Is.is(1));
		assertThat("Path annotation should be active but active is " + activeAnnotations.get(0).getText(),
				activeAnnotations.get(0).getText(), Is.is("javax.ws.rs.Path"));
	}
	
	/**
	 * 1 check equality of param values
	 * 2 changing param values is mirrored to class
	 */
	@Test
	public void testAnnotationParamValues() {
		/** check params of annotation is synchronized **/
		navigateInActiveEditor(13, 0);

		AnnotationPropertiesView annotationView = new AnnotationPropertiesView();
		annotationView.open();

		TreeItem pathAnnotation = annotationView.getAnnotation("javax.ws.rs.Path");
		List<TreeItem> values = annotationView.getAnnotationValues(pathAnnotation);

		assertThat(values.size(), Is.is(1));

		assertThat(values.get(0).getCell(1), Is.is("\"/rest\""));

		/** edit parameter values and check if it is still synchronized **/
		String parameter = values.get(0).getText();
		annotationView.changeAnnotationParamValue(pathAnnotation, parameter, "/edit");
		activeEditorContains("@Path(\"/edit\")");
		activeEditorDoesntContain("@Path(\"/rest\")");
	}

	/**
	 * 1 activating annotation through view is mirrored to class
	 */
	@Test
	public void testAnnotationActivating() {
		navigateInActiveEditor(13, 0);

		AnnotationPropertiesView annotationsView = new AnnotationPropertiesView();
		annotationsView.open();
		annotationsView.activateAnnotation(annotationsView.getAnnotation("javax.ws.rs.Encoded"));

		activeEditorContains("@Encoded");
	}

	/**
	 * 1 deactivating annotation through view is mirrored to class
	 */
	@Test
	public void testAnnotationDeactivating() {
		navigateInActiveEditor(13, 0);

		AnnotationPropertiesView annotationsView = new AnnotationPropertiesView();
		annotationsView.open();
		annotationsView.deactivateAnnotation(annotationsView.getAnnotation("javax.ws.rs.Path"));

		activeEditorDoesntContain("@Path(\"/rest\")");
	}

	@Test
	public void testJaxRSSupport() {
		/** edit JAX-RS annotation value **/
		navigateInActiveEditor(13, 0);

		AnnotationPropertiesView annotationsView = new AnnotationPropertiesView();
		annotationsView.open();

		TreeItem pathAnnotation = annotationsView.getAnnotation("javax.ws.rs.Path");

		annotationsView.changeAnnotationParamValue(
				pathAnnotation, 
				annotationsView.getAnnotationValues(pathAnnotation).get(0).getText(), 
				"/edit");
		new WaitUntil(new RestServicePathsHaveUpdated(getWsProjectName()), TimePeriod.getCustom(2), false);

		for (RESTfulWebService service : restfulServicesForProject(getWsProjectName())) {
			String path = service.getPath();
			Asserts.assertNotContain(path, "rest");
			Asserts.assertContain(path, "edit");
		}

		/** delete JAX-RS annotation **/
		navigateInActiveEditor(16, 0);

		annotationsView.open();

		TreeItem getAnnotation = annotationsView.getAnnotation("javax.ws.rs.GET");

		annotationsView.deactivateAnnotation(getAnnotation);

		// It gets some time till it's shown in explorer
		new WaitUntil(new RestServicePathsHaveUpdated(getWsProjectName()), TimePeriod.getCustom(2), false);
		
		assertThat("RESTful services", restfulServicesForProject(getWsProjectName()).size(), Is.is(3));
	}

	private void navigateInActiveEditor(int line, int column) {
		TextEditor editor = new TextEditor();
		editor.setCursorPosition(line, column);
	}

	private void activeEditorDoesntContain(String value) {
		activeValueIsContainedInActiveEditor(value, false);
	}

	private void activeEditorContains(String value) {
		activeValueIsContainedInActiveEditor(value, true);
	}

	private void activeValueIsContainedInActiveEditor(
			String value, boolean shouldContain) {
		String text = new TextEditor().getText();
		assertTrue("Editor should " + (shouldContain?"":"not ")
				+ "contain \""+ value +"\" but the content is:\n" + text,
				text.contains(value) == shouldContain);
	}
}

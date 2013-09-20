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

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.views.AnnotationPropertiesView;
import org.junit.Test;

/**
 * Checks behaviour of AnnotationProperties view and its 
 * impact on JAX-RS explorer 
 * 
 * @author jjankovi
 *
 * Also improve tests (mainly testAbsenceOfAnnotation() and testPresenceOfAnnotation())
 * 
 */
public class AnnotationPropertiesTest extends RESTfulTestBase {
	
	private PackageExplorer packageExplorerRD = 
			new PackageExplorer();
	private AnnotationPropertiesView annotationsView = 
			new AnnotationPropertiesView();
	
	@Override
	public String getWsProjectName() {
		return "restAdvanced";
	}
	
	@Override
	public void setup() {
		
		importRestWSProject(getWsProjectName());
		openClass(getWsProjectName(), getWsPackage(), getWsName() + ".java");
		
	}
	
	/**
	 * 1 there are no incorrectly checked annotations
	 * 2 there are no incorrectly unchecked annotations 
	 * 
	 * @author rrabara
	 */
	@Test
	public void testAbsenceOfAnnotation() {
		/** check params of annotation is synchronized **/
		navigateInActiveEditor(13, 0);
		
		annotationsView.show();
		
		List<TreeAnnotationItem> allAnnotations = annotationsView.getAllAnnotations();

		List<TreeAnnotationItem> deactiveAnnotations = annotationsView.getAllDeactiveAnnotation();
		assertThat(deactiveAnnotations.size(), Is.is(allAnnotations.size()-1));
		for(TreeAnnotationItem item : deactiveAnnotations) {
			assertThat("path annotation isn't deactivated", item.getText(), IsNot.not("javax.ws.rs.Path"));
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
		
		annotationsView.show();
				
		List<TreeAnnotationItem> activeAnnotations = annotationsView.getAllActiveAnnotation();
		assertThat("only one annotation should be active but is "+activeAnnotations.size(),activeAnnotations.size(), Is.is(1));
		assertThat("path annotation should be active but is "+activeAnnotations.get(0).getText(), activeAnnotations.get(0).getText(), Is.is("javax.ws.rs.Path"));
	}
	
	/**
	 * 1 check equality of param values
	 * 2 changing param values is mirrored to class
	 */
	@Test
	public void testAnnotationParamValues() {
		
		/** check params of annotation is synchronized **/
		navigateInActiveEditor(13, 0);
		
		annotationsView.show();
		
		TreeAnnotationItem pathAnnotation = annotationsView.
				getAnnotation("javax.ws.rs.Path");
		List<SWTBotTreeItem> values = annotationsView.
				getAnnotationValues(pathAnnotation);
		
		assertThat(values.size(), Is.is(1));
		String parameter = values.get(0).getText();
		assertThat(values.get(0).cell(1), Is.is("\"/rest\""));
		
		/** edit parameter values and check if it is still synchronized **/
		annotationsView.changeAnnotationParamValue(
				pathAnnotation, parameter, "/edit");
		activeEditorContains("@Path(\"/edit\")");
		
	}
	
	/**
	 * 1 activating annotation through view is mirrored to class
	 */
	@Test
	public void testAnnotationActivating() {
		
		navigateInActiveEditor(13, 0);
		
		annotationsView.show();
		annotationsView.activateAnnotation(
				annotationsView.getAnnotation("javax.ws.rs.Encoded"));
		
		activeEditorContains("@Encoded");
		
	}
	
	/**
	 * 1 deactivating annotation through view is mirrored to class
	 */
	@Test
	public void testAnnotationDeactivating() {
		
		navigateInActiveEditor(13, 0);
		
		annotationsView.show();
		annotationsView.deactivateAnnotation(
				annotationsView.getAnnotation("javax.ws.rs.Path"));
		
		activeEditorDoesntContain("@Path(\"/rest\")");
		
	}
	
	/**
	 * 1 editing annotation(ant its values) is reflected on JAX-RS explorer
	 * 
	 * Fails due to JBIDE-11766.
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-11766
	 */
	@Test
	public void testJaxRSSupport() {
		
		/** edit JAX-RS annotation value **/
		navigateInActiveEditor(13, 0);
		
		annotationsView.show();
		
		TreeAnnotationItem pathAnnotation = annotationsView.getAnnotation("javax.ws.rs.Path");
		
		annotationsView.changeAnnotationParamValue(
				pathAnnotation, 
				annotationsView.getAnnotationValues(pathAnnotation).get(0).getText(), 
				"/edit");
		
		for (SWTBotTreeItem service : restfulServicesForProject(getWsProjectName())) {
			String path = restfulWizard.getPathForRestFulService(service);
			assertDoesNotContain("rest", path);
			assertContains("edit", path);	
		}
		
		/** delelete JAX-RS annotation **/
		navigateInActiveEditor(16, 0);
		
		annotationsView.show();
		
		TreeAnnotationItem getAnnotation = annotationsView.
				getAnnotation("javax.ws.rs.GET");
		
		annotationsView.deactivateAnnotation(getAnnotation);
		
		assertThat(restfulServicesForProject(getWsProjectName()).length, Is.is(3));
		
		
	}
	
	private void openClass(String projectName, String packageName,
			String classFullName) {

		packageExplorerRD.open();
		packageExplorerRD.getProject(projectName)
				.getProjectItem("src", packageName, classFullName)
				.open();

	}
	
	private void navigateInActiveEditor(int line, int column) {
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		editor.navigateTo(line, column);
	}
	
	private void activeEditorDoesntContain(String value) {
		activeValueIsContainedInActiveEditor(value, false);
	}
	
	private void activeEditorContains(String value) {
		activeValueIsContainedInActiveEditor(value, true);
	}
	
	private void activeValueIsContainedInActiveEditor(
			String value, boolean shouldContain) {
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		assertThat(editor.toTextEditor().getText().contains(value), 
				Is.is(shouldContain));
	}
	
}

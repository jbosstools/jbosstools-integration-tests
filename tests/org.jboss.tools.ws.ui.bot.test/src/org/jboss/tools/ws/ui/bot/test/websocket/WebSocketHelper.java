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

import static org.jboss.tools.ws.ui.bot.test.utils.ClassHelper.addClassAnnotation;
import static org.jboss.tools.ws.ui.bot.test.utils.ClassHelper.addImport;
import static org.jboss.tools.ws.ui.bot.test.utils.ClassHelper.getClassDefinitionLine;
import static org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper.createClass;
import static org.jboss.tools.ws.ui.bot.test.websocket.NameBindingTest.Constants.CLASS_NAME_ANNOTATION;
import static org.jboss.tools.ws.ui.bot.test.websocket.NameBindingTest.Constants.CLASS_NAME_FILTER;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.CLIENT_CLASS_NAME;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.EXPECTED_PROPOSALS;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.PROJECT_NAME;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.PROJECT_PACKAGE;
import static org.jboss.tools.ws.ui.bot.test.websocket.StubMethodsTest.Constants.SERVER_CLASS_NAME;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.label.DefaultLabel;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.swt.SWT;

class WebSocketHelper {

	/**
	 * Shell with this label is used. Initialized in first usage (requires opened editor).
	 */
	private static String CORRECT_CONTENT_ASSISTANT_LABEL = null;

	static void callAllStubProposals(TextEditor editor) {
		Set<String> remainingProposals = new HashSet<>(EXPECTED_PROPOSALS);

		for (String proposal : EXPECTED_PROPOSALS) {
			//Call proposal
			ContentAssistant contentAssistant = openContentAssistant(editor);
			contentAssistant.chooseProposal(proposal);

			//Update tmp collection
			remainingProposals.remove(proposal);

			//Asserts
			assertNoMarkerError(editor.getMarkers());
			assertThereAreOnlySpecifiedStubProposals(editor, remainingProposals);
			assertOutlineViewContainsNewMethod(editor, proposal);
		}
	}

	//ASSERTS:
	private static void assertNoMarkerError(List<Marker> markers) {
		for (Marker marker : markers) {
			if (marker.getText().contains("error")) {
				fail("There is an error in the markers! " + marker.getText());
			}
		}
	}

	/**
	 * Stub proposal should be in current proposals if and only if
	 * collection shouldBe contains it.
	 *
	 * @param editor   
	 * @param shouldBe
	 */
	static void assertThereAreOnlySpecifiedStubProposals(
			TextEditor editor, Collection<String> shouldBe) {

		//Preparing collections
		List<String> proposals = new ArrayList<>(EXPECTED_PROPOSALS);
		proposals.removeAll(shouldBe);
		List<String> shouldNotBe = proposals;

		ContentAssistant contentAssistant = openContentAssistant(editor);
		List<String> actualProposals = contentAssistant.getProposals();
		contentAssistant.close();

		//check
		assertTrue("There are not all expected proposals! Expected: \r\n" 
				+ shouldBe.stream().collect(Collectors.joining(", "))
				+ "\r\nBut was: \r\n" 
				+ actualProposals.stream().collect(Collectors.joining(", ")) + "\r\n", 
				actualProposals.containsAll(shouldBe));
		boolean testPass = actualProposals.retainAll(shouldNotBe);
		assertTrue("There are proposals those should not be there!\r\n" 
				+ actualProposals.stream().collect(Collectors.joining(", ")) + "\r\n", 
				testPass);
	}

	private static void assertOutlineViewContainsNewMethod(TextEditor editor, String proposal) {
		String newMethodName = proposal.split("[(]")[0];

		for (TreeItem outlineItem : new ContentOutline().outlineElements()) {
			if (SERVER_CLASS_NAME.equals(outlineItem.getText()) || CLIENT_CLASS_NAME.equals(outlineItem.getText())) {
				for(TreeItem classItem : outlineItem.getItems()){
					if (classItem.getText().startsWith(newMethodName)) {
						//OK
						editor.activate();
						return;
					}
				}
				fail("New method not found in Class. Method: " + newMethodName);
			}
		}
		fail("Class not found in Outline View!");
	}

	//Open ContentAssistant methods:
	/**
	 * In first call methods finds ContentAssistant with StubMethodsTest.Constants.EXPECTED_PROPOSALS
	 * and remembers its label in CORRECT_CONTENT_ASSISTANT_LABEL.
	 *
	 * @return opened content assistant
	 */
	static ContentAssistant openContentAssistant(TextEditor editor) {

		//first call
		if (CORRECT_CONTENT_ASSISTANT_LABEL == null) {
			findCorrectContentAssistantLabel(editor);
		}

		//finding the correct content assistant
		ContentAssistant contentAssistant = editor.openContentAssistant();
		String labelValue = new DefaultLabel(contentAssistant).getText();
		while (!CORRECT_CONTENT_ASSISTANT_LABEL.equals(labelValue)) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SPACE);
			labelValue = new DefaultLabel(contentAssistant).getText();
		}
		return contentAssistant;
	}

	private static void findCorrectContentAssistantLabel(TextEditor editor) {
		ContentAssistant contentAssistant = editor.openContentAssistant();

		//studio behavior fix
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
		contentAssistant = editor.openContentAssistant();

		String labelValue = new DefaultLabel(contentAssistant).getText();
		while (!contentAssistant.getProposals().containsAll(EXPECTED_PROPOSALS)) {
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SPACE);
			labelValue = new DefaultLabel(contentAssistant).getText();
		}
		CORRECT_CONTENT_ASSISTANT_LABEL = labelValue;
		contentAssistant.close();
	}

	//Prepare class methods:
	static TextEditor prepareClass(String className) {
		switch (className) {
			case SERVER_CLASS_NAME:
				return prepareClass(SERVER_CLASS_NAME,
						"import javax.websocket.server.ServerEndpoint;",
						"@ServerEndpoint(\"/test/\")");
			case CLIENT_CLASS_NAME:
				return prepareClass(CLIENT_CLASS_NAME,
						"import javax.websocket.ClientEndpoint;",
						"@ClientEndpoint");
			default:
				throw new RuntimeException("Unknown class to prepare!");
		}
	}
	
	static void deleteClass(String className) {
		TextEditor editor = new TextEditor(className + ".java");
		if (editor.isDirty()) {
			editor.activate();
			editor.save();
		}
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		
		ProjectItem item = pe.getProject(PROJECT_NAME).getProjectItem("src/main/java", PROJECT_PACKAGE, className + ".java");
		item.delete();
	}

	private static TextEditor prepareClass(
			String className, String importCommand, String annotation) {

		TextEditor editor = createClass(PROJECT_NAME, PROJECT_PACKAGE, className);
		DefaultStyledText text = new DefaultStyledText();
		text.insertText(1, 0, System.lineSeparator());
		text.insertText(2, 0, importCommand);
		text.insertText(3, 0, System.lineSeparator());
		text.insertText(3, 0, annotation);

		//set cursor position at the first line in the class, column 0
		int firstLineInClassIndex = getClassDefinitionLine(editor) + 1;
		editor.setCursorPosition(firstLineInClassIndex, 0);

		return editor;
	}

	//Prefixes methods:
	static List<String> filterStubProposalsWithPrefix(String prefix) {
		List<String> matches = new ArrayList<>();
		for (String proposal : EXPECTED_PROPOSALS) {
			if (proposal.startsWith(prefix))
				matches.add(proposal);
		}
		return matches;
	}

	static void setPrefixIntoFirstClassLine(TextEditor editor, String prefix) {
		int firstLineInClassIndex = getClassDefinitionLine(editor) + 1;
		new DefaultStyledText().insertText(firstLineInClassIndex, 0, prefix);
		
		//jump to the end of the line
		editor.setCursorPosition(firstLineInClassIndex, editor.getTextAtLine(firstLineInClassIndex).length());
	}

	static void clearFirstClassLine(TextEditor editor) {
		delFirstClassLine(editor);
		int firstLineInClassIndex = getClassDefinitionLine(editor) + 1;
		new DefaultStyledText().insertText(firstLineInClassIndex, 0, System.lineSeparator());
	}

	static void delFirstClassLine(TextEditor editor) {
		int firstLineInClassIndex = getClassDefinitionLine(editor) + 1;
		editor.selectLine(firstLineInClassIndex);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.DEL);
	}

	static int countImports(TextEditor editor) {
		return StringUtils.countMatches(editor.getText(), "import ");
	}

	static void enableJAXRSSupport(String projectName) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(projectName);
		PropertyDialog dialog = new PropertyDialog(projectName);
		dialog.open();
		dialog.select("JAX-RS");
		new CheckBox().toggle(true);
		dialog.ok();
	}

	static TextEditor prepareCustomAnnotationEditor(String projectName) {
		TextEditor annotationEditor = createClass(projectName,
				NameBindingTest.Constants.PROJECT_PACKAGE, NameBindingTest.Constants.CLASS_NAME_ANNOTATION);

		//replace 'class' with '@interface'
		String replacedCode = new DefaultStyledText().getText().replaceAll("public class", "public @interface");
		new DefaultStyledText().setText(replacedCode);

		addClassAnnotation(annotationEditor, "@NameBinding", "import javax.ws.rs.NameBinding;");

		addClassAnnotation(annotationEditor, "@Target({ ElementType.TYPE, ElementType.METHOD })", "import java.lang.annotation.Target;");
		addImport(annotationEditor, "import java.lang.annotation.ElementType;");

		addClassAnnotation(annotationEditor, "@Retention(value = RetentionPolicy.RUNTIME)", "import java.lang.annotation.Retention;");
		addImport(annotationEditor, "import java.lang.annotation.RetentionPolicy;");

		annotationEditor.save();
		return annotationEditor;
	}

	static TextEditor prepareAppEditor(String projectName) {
		TextEditor appEditor = createClass(projectName,
				NameBindingTest.Constants.PROJECT_PACKAGE, NameBindingTest.Constants.CLASS_NAME_APP);

		//add 'extends' def part
		String replacedCode = new DefaultStyledText().getText()
				.replaceAll("public class " + NameBindingTest.Constants.CLASS_NAME_APP,
						"public class " + NameBindingTest.Constants.CLASS_NAME_APP + " extends Application");
		new DefaultStyledText().setText(replacedCode);
		addImport(appEditor, "import javax.ws.rs.core.Application;");

		addClassAnnotation(appEditor, "@ApplicationPath(\"/app\")", "import javax.ws.rs.ApplicationPath;");
		addClassAnnotation(appEditor, "@" + CLASS_NAME_ANNOTATION);

		appEditor.save();
		return appEditor;
	}

	static TextEditor prepareFilterEditor(String projectName) {
		TextEditor filterEditor = createClass(projectName,
				NameBindingTest.Constants.PROJECT_PACKAGE, NameBindingTest.Constants.CLASS_NAME_FILTER);

		//add 'implements' def part
		String replacedCode = new DefaultStyledText().getText()
				.replaceAll("public class " + CLASS_NAME_FILTER,
						"public class " + CLASS_NAME_FILTER + " implements ContainerResponseFilter");
		new DefaultStyledText().setText(replacedCode);
		addImport(filterEditor, "import javax.ws.rs.container.ContainerResponseFilter;");

		addClassAnnotation(filterEditor, "@Provider", "import javax.ws.rs.ext.Provider;");
		addClassAnnotation(filterEditor, "@" + CLASS_NAME_ANNOTATION);

		replacedCode = new DefaultStyledText().getText()
				.replaceAll("implements ContainerResponseFilter \\{",
						"implements ContainerResponseFilter	{\n" +
								"\t@Override\n" +
								"\tpublic void filter(ContainerRequestContext requestContext,\n" +
								"                     ContainerResponseContext responseContext)\n" +
								"\t\t\tthrows IOException {\n" +
								"\t\t// ...\n" +
								"\t}");
		new DefaultStyledText().setText(replacedCode);
		addImport(filterEditor, "import javax.ws.rs.container.ContainerRequestContext;");
		addImport(filterEditor, "import javax.ws.rs.container.ContainerResponseContext;");
		addImport(filterEditor, "import java.io.IOException;");

		filterEditor.save();
		return filterEditor;
	}

}

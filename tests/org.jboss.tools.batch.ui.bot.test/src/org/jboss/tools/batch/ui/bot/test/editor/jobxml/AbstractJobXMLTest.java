/*******************************************************************************
 * Copyright (c) 2017-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.core.lookup.WidgetLookup;
import org.eclipse.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;
import org.eclipse.reddeer.jface.text.contentassist.ContentAssistant;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.jboss.tools.batch.ui.bot.test.Activator;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignFlowElementsTestTemplate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Abstract classwith set of features for test cases that manipulates with source view tab
 * @author odockal
 *
 */
public abstract class AbstractJobXMLTest extends DesignFlowElementsTestTemplate {
	
	private static int DELAY = 100;
	
	public static final String BATCHLET_REF = "batchlet";
	
	public static final String STEP_LISTENER_REF = "stepListener";
	
	public static final String READER_REF = "reader";
	
	public static final String WRITER_REF = "writer";
	
	public static final String JOB_LISTENER_REF = "jobListener";
	
	public static final String CUSTOM_LISTENER_REF = "customClassListener";
	
	public static final String CUSTOM_QUALIFIED_LISTENER_REF = "customListener";
	
	public static final String PROCESSOR_REF = "processor";
	
	public static final String CHECK_REF = "checkpointAlgorithm";
	
	public static final String DECIDER_REF = "decider";
	
	public static final String MAPPER_REF = "mapper";
	
	public static final String REDUCER_REF = "reducer";
	
	public static final String ANALYZER_REF = "analyzer";
	
	public static final String COLLECTOR_REF = "collector";
	
	private static final Logger log = Logger.getLogger(AbstractJobXMLTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() {
		String suffix = JAVA_VERSION > 1.8 ? "2-11.zip" : "2.zip";
		initTestResources(log, "projects/" + getProjectName() + suffix);
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		removeProject(log, getProjectName() + "2");
	}
	
	@Override
	@Before
	public void setUp() {
		setupEditor();
	}
	
	@Override
	@After
	public void tearDown() {
		closeEditor();
	}
	
	@Override
	protected String getPackage() {
		return "batch.test.editor.source";
	}
	
	/**
	 * Asserts that given reference is valid and
	 * its obfuscation is correctly marked as warning in Problems View
	 * @param referenceID id of referencing artifact 
	 * @param index index of the occurence of the id in the text editor
	 * @param referencingParam says whether "ref" or "class" attribute 
	 */
	protected void referenceCheck(String referenceID, int index, String referencingParam) {
		String textToFind = referencingParam + referenceID;
		int offset = editor.getSourcePage().getPositionOfText(textToFind, index);
		if (offset != -1) {
			editor.getSourcePage().setCursorPosition(offset + textToFind.length());
			pressButton('x');
			performSave(editor.getEditorPart());
			assertNumberOfProblems(0, 1);
			editor.getSourcePage().setCursorPosition(editor.getSourcePage().getPositionOfText(textToFind + "x") + (textToFind).length() + 1);
			selectText(1);
			pressButton(SWT.BS);
			performSave(editor.getEditorPart());
			assertNoProblems();
		} else {
			Assert.fail("Searched reference " + referenceID + " was not found in text editor");
		}
	}
	
	protected void emptyReferenceCheck(String referenceID) {
		emptyReferenceCheck(referenceID, 0, "ref=\"");
	}
	
	/**
	 * Tests validation of empty referenceParam attribute
	 * @param referenceID id of referencing artifact 
	 * @param index index of the occurence of the id in the text editor
	 * @param referencingParam says whether "ref" or "class" attribute
	 */
	protected void emptyReferenceCheck(String referenceID, int index, String referenceParam) {
		String textToFind = referenceParam + referenceID;
		int offset = editor.getSourcePage().getPositionOfText(textToFind, index);
		if (offset != -1) {
			editor.activate();
			editor.getSourcePage().selectText(textToFind + "\"", index);
			pressButton(SWT.BS);
			editor.getSourcePage().insertText(offset, referenceParam + "\"");
			performSave(editor.getEditorPart());
			assertNumberOfProblems(0, 1);
			editor.getSourcePage().selectText(referenceParam + "\"");
			pressButton(SWT.BS);
			editor.getSourcePage().insertText(offset, referenceParam + referenceID + "\"");
			performSave(editor.getEditorPart());
		} else {
			Assert.fail("Searched reference " + referenceID + " was not found in text editor");
		}
	}
	
	/**
	 * Changes first letter of given parameter to capital
	 * @param artifact artifact name to be manipulated with
	 * @return string artifact with first capital letter 
	 */
	protected String getMyArtifact(String artifact) {
		char art [] = artifact.toCharArray();
		art[0] = Character.toUpperCase(art[0]);
		return new String(art);
	}
	
	/**
	 * 
	 * @param className class name
	 * @return fully qualified class name
	 */
	protected String getFullyQualifiedClassName(String className) {
		return getPackage() + "." + className;
	}
	
	/**
	 * Replaces existing job.xml file content with content of the file passed as parameter
	 * using instance member editor that represents xml file
	 * @param filePath path of the file within this plugin
	 */
	protected void setJobXMLContentFromFile(String filePath, boolean assertNoProblems) {
		String absolutePath = getResourceAbsolutePath(Activator.PLUGIN_ID , filePath);
		log.info("Setting Job XML file content from file " + absolutePath);
		String jobcontent = "";
		try {
			jobcontent = readTextFileToString(absolutePath);
			log.info("Content to be inserted:\n\r" + jobcontent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		editor.activate();
		editor.getSourcePage().setText(jobcontent);
		performSave(editor.getEditorPart());
		if (assertNoProblems) {
			assertNoProblems();
		}
	}
	
	protected void setJobXMLContentFromFile(String filePath) {
		setJobXMLContentFromFile(filePath, true);
	}
	
	/**
	 * @param element text to insert in form of element
	 * @param insertBefore searched string to insert before
	 */
	protected void insertElementBefore(String element, String insertBefore) {
		XMLSourcePage source = editor.getSourcePage();
		source.insertLine(source.getLineOfText(insertBefore) - 1, element);
		performSave(editor.getEditorPart());
	}
	
	protected void verifyElementContentAssistOffering(String element, String... items) {
		List<String> proposals = getProposalsFromCursor(element);
		assertThat(proposals, hasItems(items));
	}
	
	protected List<String> getProposalsFromCursor(String element) {
		TextEditor source = (TextEditor) getSourcePage();
		int position = source.getPositionOfText(element);
		source.setCursorPosition(position + element.length());
		Point startPoint = source.getCursorPosition();
		return invokeContentAssistantAndGetProposals(source, startPoint);
	}
	
	protected List<String> getProposalsFromSelectedText(String cursorToSetAfterText, String textToSelect) {
		TextEditor source = (TextEditor) getSourcePage();
		int position = source.getPositionOfText(cursorToSetAfterText);
		source.setCursorPosition(position + cursorToSetAfterText.length());
		Point startPoint = source.getCursorPosition();
		selectText(textToSelect.length());
		return invokeContentAssistantAndGetProposals(source, startPoint);
	}
	
	protected List<String> invokeContentAssistantAndGetProposals(TextEditor source, Point cursorPoint) {
		List<String> proposals = new ArrayList<String>();
		try {
			ContentAssistant cs = source.openContentAssistant();
			proposals = cs.getProposals();
			cs.close();
		} catch (WaitTimeoutExpiredException exc) {
			// content assist just invoked the only available option and inserted it into the attribute
			// lets check the value
			performSave(editor.getEditorPart());
			Point endPoint = source.getCursorPosition();
			log.info("Selecting text on line " + cursorPoint.x + " from column " + endPoint.y + " to column " + cursorPoint.y);
			String result = source.getTextAtLine(endPoint.x).substring(cursorPoint.y, endPoint.y);
			log.info("Suggested text by content assistant: " + result);
			proposals.add(result);
		}
		return proposals;
	}
	
	/**
	 * Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=537025
	 */
	public void selectText(int numberOfSteps) {
		editor.activate();
		int mask = SWT.SHIFT;
		int spaceKey = SWT.ARROW_LEFT;
		final Widget w = WidgetLookup.getInstance().getFocusControl();
		Event press = keyEvent(spaceKey, SWT.KeyDown, w, mask);
		Event release = keyEvent(spaceKey, SWT.KeyUp, w, mask);
		for (int i = 0; i < numberOfSteps; i++) {
			type(press);
			type(release);
		}
	}
	
	public void pressButton(int key) {
		editor.activate();
		final Widget w = WidgetLookup.getInstance().getFocusControl();
		Event press = keyEvent(key, SWT.KeyDown, w);
		Event release = keyEvent(key, SWT.KeyUp, w);
		type(press);
		type(release);
	}
	
	private void type(Event e) {
		sync();
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				Display.getDisplay().post(e);
			}
		});
		sync();
	}

	private Event keyEvent(int key, int eventType, Widget w, int mask) {
		Event e = keyEvent(key, eventType, w);
		e.stateMask = mask;
		return e;
	}
	
	private Event keyEvent(int key, int eventType, Widget w) {
		Event e = new Event();
		e.keyCode = key;
		e.character = (char) key;
		e.type = eventType;
		e.widget = w;
		return e;
	}
	
	private void sync() {
		delay(DELAY);
		emptySync();		
	}

	private void emptySync() {
		Display.syncExec(new Runnable() {
			
			@Override
			public void run() {				
			}
		});
		
	}
	
	private void delay(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

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
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import java.io.IOException;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.swt.SWT;
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
	
	private static final Logger log = Logger.getLogger(AbstractJobXMLTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() {
		initTestResources(log, "projects/" + getProjectName() + "2.zip");
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		removeProject(log);
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
			editor.activate();
			editor.getSourcePage().setCursorPosition(offset + textToFind.length());
			KeyboardFactory.getKeyboard().type("x");
			editor.save();
			assertNumberOfProblems(0, 1);
			editor.getSourcePage().setCursorPosition(editor.getSourcePage().getPositionOfText(textToFind + "x") + (textToFind).length());
			KeyboardFactory.getKeyboard().type(SWT.DEL);
			editor.save();
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
			editor.getSourcePage().selectText(textToFind + "\"", index);
			KeyboardFactory.getKeyboard().type(SWT.DEL);
			editor.getSourcePage().insertText(offset, referenceParam + "\"");
			editor.save();
			assertNumberOfProblems(0, 1);
			editor.getSourcePage().selectText(referenceParam + "\"");
			KeyboardFactory.getKeyboard().type(SWT.DEL);
			editor.getSourcePage().insertText(offset, referenceParam + referenceID + "\"");
			editor.save();
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
	protected void setJobXMLContentFromFile(String filePath) {
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
		editor.save();
		assertNoProblems();
	}
	
	/**
	 * @param element text to insert in form of element
	 * @param insertBefore searched string to insert before
	 */
	protected void insertElementBefore(String element, String insertBefore) {
		XMLSourcePage source = editor.getSourcePage();
		source.insertLine(source.getLineOfText(insertBefore) - 1, element);
		source.save();
	}
	
}

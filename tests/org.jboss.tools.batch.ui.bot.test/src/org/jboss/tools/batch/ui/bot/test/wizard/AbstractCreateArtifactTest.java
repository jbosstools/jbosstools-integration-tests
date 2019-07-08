/*******************************************************************************
 * Copyright (c) 2016-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.wizard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.wst.xml.ui.tabletree.XMLMultiPageEditorPart;
import org.eclipse.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.batch.reddeer.editor.JavaClassEditor;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractCreateArtifactTest extends AbstractBatchTest {

	protected static final String NAMED_ANNOTATION = "Named";
	
	protected abstract String getClassName();

	protected abstract BatchArtifacts getArtifact();

	protected abstract void createArtifactHook(NewBatchArtifactWizardPage page);
	
	private static Logger log = Logger.getLogger(AbstractCreateArtifactTest.class);
	
	@Override
	protected String getPackage() {
		return "batch.test.wizard.artifact";
	}
	
	@BeforeClass
	public static void setUpBeforeClass() {
		initTestResources(log, "projects/" + getProjectName() + (JAVA_VERSION > 1.8 ? "-11.zip" : ".zip"));
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		removeProject(log, getProjectName());
	}

	@After
	public void tearDown(){
		try {
			new DefaultEditor(getClassName() + ".java").close();
		} catch (CoreLayerException e) {
			// nothing to do
		}
	}

	protected void createArtifact() {
		NewBatchArtifactWizardDialog dialog = new NewBatchArtifactWizardDialog();
		dialog.open();

		NewBatchArtifactWizardPage page = new NewBatchArtifactWizardPage(dialog);
		page.setPackage(getPackage());
		page.setName(getClassName());
		page.setArtifact(getArtifact());
		createArtifactHook(page);
		dialog.finish();
	}
	
	protected void assertEditorIsActivated() {
		assertTrue(getEditor().isActive());
	}

	protected void assertClassName() {
		assertThat(getEditor().getClassName(), is(getClassName()));
	}

	protected JavaClassEditor getEditor(){
		return new JavaClassEditor(getClassName() + ".java");
	}
	
	protected String getClassByID(String id) {
		if (!getProject().containsResource(BATCH_XML_FILE_FULL_PATH)){
			// the batch.xml file has not been created yet and the <ref value has not been inserted -> this is OK
			return "";
		}
		getProject().getProjectItem(BATCH_XML_FILE_FULL_PATH).open();
		
		XMLMultiPageEditorPart editor = new XMLMultiPageEditorPart(BATCH_XML_FILE);
		editor.activate();
		
		XMLSourcePage sourceTab = editor.getSourcePage();
		
		String xPath = "/:batch-artifacts/:ref[@id=\"" + id + "\"]/@class";
		return sourceTab.getAssociatedFile().xpath(xPath);
	}
	
	protected String getClass(String clazz) {
		if (!getProject().containsResource(BATCH_XML_FILE_FULL_PATH)){
			// the batch.xml file has not been created yet and the <ref value has not been inserted -> this is OK
			return "";
		}
		getProject().getProjectItem(BATCH_XML_FILE_FULL_PATH).open();
		
		XMLMultiPageEditorPart editor = new XMLMultiPageEditorPart(BATCH_XML_FILE);
		String xPath = "/:batch-artifacts/:ref[@class=\"" + clazz + "\"]";
		return editor.getSourcePage().getAssociatedFile().xpath(xPath);
	}
}

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
package org.jboss.tools.batch.ui.bot.test.editor.features;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignFlowElementsTestTemplate;
import org.junit.After;
import org.junit.Before;
import org.jboss.reddeer.eclipse.ui.search.SearchResult;
import org.jboss.reddeer.eclipse.ui.search.SearchView;
/**
 * Abstract template class for implementing tests of features as
 * references, validation, code assist, etc.
 * 
 * @author odockal
 *
 */
public abstract class AbstractFeatureBaseTest extends DesignFlowElementsTestTemplate {
	
	protected final static String BATCHLET_ID = "BatchletArtifact";
	
	protected final static String BATCHLET_JAVA_CLASS = "BatchletArtifact.java";
	
	protected final static String EXCEPTION_ID = "ExceptionArtifact";
	
	protected final static String EXCEPTION_JAVA_CLASS = "ExceptionArtifact.java";
	
	protected final static String BATCHLET_PROPERTY_ID = "BatchletPropertyArtifact";
	
	protected final static String BATCHLET_PROPERTY_JAVA_CLASS = "BatchletPropertyArtifact.java";
	
	protected final static String PROPERTY_NAME = "testProperty";
	
	public String getFullFileName(String name, String type) {
		return name + "." + type;
	}

	@Before
	@Override
	public void setupEditor() {
		if (!getProject().containsItem(JOB_XML_FILE_FULL_PATH)) {
			getProject().select();
			createJobXMLFile(JOB_ID); 
		}
		super.setupEditor();
	}
	
	@After
	@Override
	public void closeEditor() {
		if (getProject().containsItem(JOB_XML_FILE_FULL_PATH)) {
			getProject().getProjectItem(JOB_XML_FILE_FULL_PATH).delete();
		}
		super.closeEditor();
	}
	
	/**
	 * Checks whether given text was found in results of Search View
	 * @param text text to be found in Search View
	 * @param path path to the item in project explorer where context menu will be displayed
	 * @return true if text occurs in search results, false otherwise
	 */
	protected boolean searchForClassReference(String text, String... path) {
		boolean result = false;
		ProjectItem project = getProject().getProjectItem(path);
		if( project != null) {
			project.select();
			new ContextMenu("References", "Project").select();
			new WaitWhile(new JobIsRunning());
			try {
				SearchView search = new SearchView();
				search.open();
				for (SearchResult item : search.getSearchResults()) {
					if (item.getSearchResultText().toLowerCase().contains(text.toLowerCase())) {
						result = true;
						break;
					}
				}
				search.close();
			} catch (EclipseLayerException e) {
				throw new AssertionError("Could not open Seach View or results were not found.");
			}
		}
		return result;
	}	
	
	/**
	 * Checks whether given text was found in results of Search View
	 * @param text text to be found in Search View
	 * @param propertyName name of property to search for
	 * @param path path to the item in project explorer where context menu will be displayed
	 * @return true if text occurs in search results, false otherwise
	 */
	protected boolean searchForPropertyInFile(String text, String propertyName, String... path) {
		boolean result = false;
		ProjectItem project = getProject().getProjectItem(path);
		if( project != null) {
			project.open();
			TextEditor fileEditor = new TextEditor();
			fileEditor.selectText(propertyName);
			new ContextMenu("References", "Project").select();
			new WaitWhile(new JobIsRunning());
			try {
				SearchView search = new SearchView();
				search.open();
				for (SearchResult item : search.getSearchResults()) {
					if (item.getSearchResultText().toLowerCase().contains(text.toLowerCase())) {
						result = true;
						break;
					}
				}
			} catch (EclipseLayerException e) {
				throw new AssertionError("Could not open Seach View or results were not found.");
			}

		}
		return result;
	}
	
	protected boolean createExceptionClass(String exceptionID) {
		NewJavaClassWizardDialog dialog = new NewJavaClassWizardDialog();
		dialog.open();
		
		NewJavaClassWizardPage page = new NewJavaClassWizardPage();
		page.setSourceFolder(PROJECT_NAME + "/" + JAVA_FOLDER);
		page.setPackage(getPackage());
		page.setName(exceptionID);
		new LabeledText("Superclass:").setText("java.lang.Exception");
		return dialogFinished(dialog);
	}	
	
	protected boolean createBatchArtifactWithProperty(BatchArtifacts artifact, 
			String name, String propertyName) {
		NewBatchArtifactWizardDialog dialog = new NewBatchArtifactWizardDialog();
		dialog.open();

		NewBatchArtifactWizardPage page = new NewBatchArtifactWizardPage();
		page.setSourceFolder(PROJECT_NAME + "/" + JAVA_FOLDER);
		page.setPackage(getPackage());
		page.setName(name);
		page.addProperty(propertyName);
		page.setArtifact(artifact);
		return dialogFinished(dialog);	
	}
	
	private boolean dialogFinished(NewWizardDialog dialog) {
		if( dialog.isFinishEnabled()) {
			dialog.finish();
			return true;
		} else {
			dialog.cancel();
			return false;
		}
	}
}

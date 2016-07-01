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
package org.jboss.tools.batch.ui.bot.test.editor.design;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.ID;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.JOB;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.PROCESSOR;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.READER;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.STEP;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.WRITER;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.BATCHLET;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.CHECKPOINT;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.CHUNK;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.DECISION;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.FLOW;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.REF;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.SPLIT;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditor;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorDesignPage;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public abstract class DesignFlowElementsTestTemplate extends AbstractBatchTest {
	
	protected JobXMLEditor editor;
	
	@Override
	protected String getPackage(){
		return "batch.test.editor.design";
	}
	
	@Before
	public void setupEditor(){
		getProject().getProjectItem(JOB_XML_FILE_FULL_PATH).open();
		editor = new JobXMLEditor(JOB_XML_FILE);
		editor.activate();
	}
	
	@After
	public void closeEditor(){
		if (editor != null){
			editor.close();
		}
	}
	
	/**
	 * Lowers first letter of batch artifact
	 * @param className input string parameter, name of the class
	 * @return returns input string with first letter in lower case
	 */
	protected static String getBatchArtifactID(String className) {
		char c[] = className.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	} 
	
	protected JobXMLEditorDesignPage getDesignPage(){
		editor.activate();
		return editor.getDesignPage();
	}
	
	protected JobXMLEditorSourcePage getSourcePage(){
		editor.activate();
		return editor.getSourcePage();
	}
	
	protected void createBatchArtifact(BatchArtifacts artifact, String name) {
		NewBatchArtifactWizardDialog dialog = new NewBatchArtifactWizardDialog();
		dialog.open();

		NewBatchArtifactWizardPage page = new NewBatchArtifactWizardPage();
		page.setSourceFolder(PROJECT_NAME + "/" + JAVA_FOLDER);
		page.setPackage(getPackage());
		page.setName(name);
		page.setArtifact(artifact);
		dialog.finish();
	}
	
	protected String appendIDSelector(String xPathElement, String id){
		return xPathElement + "[" + JobXMLEditorSourcePage.ID + "='" + id + "']";
	}
	
	/**
	 * Add new step with given Id into flow elements
	 * @param stepID the Id of new step to be added
	 */
	protected void addStep(String stepID) {
		getDesignPage().addStep(stepID);
		
		String step = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, stepID), ID);
		assertThat(step, is(stepID));
		
		editor.save();
		assertNoProblems();
	}

	/**
	 * Add new flow element into open editor of job.xml file via design tab view,
	 * also tests if adding of element was successful and was created in source tab view.
	 * @param stepID the Id of step to add
	 * @param batchletID the Id of batchlet to add
	 */
	protected void addBatchlet(String stepID, String batchletID) {
		getDesignPage().addBatchlet(stepID, batchletID);

		String batchlet = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, stepID), BATCHLET, REF);
		assertThat(batchlet, is(batchletID));

		editor.save();
		assertNoProblems();
	}
	
	/**
	 * Add new chunk without identifier under existing step node
	 * @param stepID the Id of existing step where new chunk will be added
	 */
	protected void addChunk(String stepID) {
		getDesignPage().addChunk(stepID);
		
		editor.save();
		assertNumberOfProblems(1, 0);
	}
	
	/**
	 * Insert new reader reference into chunk of existing step,
	 * checks existence of added artifact reference
	 * @param stepID the Id of step
	 * @param readerID the reference to be added into reader's tag
	 */
	protected void setReaderRef(String stepID, String readerID) {
		getDesignPage().setReaderRef(stepID, readerID);
		
		String readerRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, stepID), CHUNK, READER, REF);
		assertThat(readerRef, is(readerID));
		
		editor.save();
		assertNumberOfProblems(1, 0);
	}

	/**
	 * Insert new writer reference into chunk of existing step,
	 * checks existence of added artifact reference
	 * @param stepID the Id of step
	 * @param writerID the reference to be added into writer's tag
	 */
	protected void setWriterRef(String stepID, String writerID) {
		getDesignPage().setWriterRef(stepID, writerID);
		
		String writerRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, stepID), CHUNK, WRITER, REF);
		assertThat(writerRef, is(writerID));
		
		editor.save();
		assertNoProblems();
	}
	
	/**
	 * Insert new processor reference into chunk of existing step,
	 * checks existence of added artifact reference
	 * @param stepID the Id of step
	 * @param processorID the reference to be added into processor's tag
	 */
	protected void setProcessor(String stepID, String processorID) {
		getDesignPage().addProcessor(stepID, processorID);
		
		String processorRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, stepID), CHUNK, PROCESSOR, REF);
		assertThat(processorRef, is(processorID));
		
		editor.save();
		assertNoProblems();
	}

	/**
	 * Insert new checkpoint reference into chunk of existing step,
	 * checks existence of added artifact reference
	 * @param stepID the Id of step
	 * @param checkpointID the reference to be added into checkpoint's tag
	 */
	protected void setCheckpoint(String stepID, String checkpointID) {
		getDesignPage().addCheckpointAlgorithm(stepID, checkpointID);
		
		String checkpointRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, stepID), CHUNK, CHECKPOINT, REF);
		assertThat(checkpointRef, is(checkpointID));
		
		editor.save();
		assertNoProblems();
	}
	
	/**
	 * Add new decision element with given id into existing job
	 * @param decisionID the id of the decision
	 */
	protected void addDecision(String decisionID) {
		getDesignPage().addDecision(decisionID);
		
		String decision = getSourcePage().evaluateXPath(JOB, appendIDSelector(DECISION, decisionID), ID);
		assertThat(decision, is(decisionID));
		
		editor.save();
		assertNumberOfProblems(1, 0);
	}

	/**
	 * Set decision reference
	 * @param decisionID decision id
	 * @param deciderID reference to be set to given decision
	 */
	protected void setDeciderRef(String decisionID, String deciderID) {
		getDesignPage().setDecisionRef(decisionID, deciderID);
		
		String decisionRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(DECISION, decisionID), REF);
		assertThat(decisionRef, is(deciderID));
		
		editor.save();
		assertNoProblems();
	}
	
	/**
	 * Add new split element into existing job, use design view
	 * @param splitID the id of the new split element
	 */
	protected void addSplit(String splitID) {
		getDesignPage().addSplit(splitID);
		
		String split = getSourcePage().evaluateXPath(JOB, appendIDSelector(SPLIT, splitID), ID);
		assertThat(split, is(splitID));
		
		editor.save();
		assertNoProblems();
	}

	/**
	 * Add new flow with the id into existing split element
	 * @param splitID id of the split where flow will be insterted
	 * @param flowID id of the new flow
	 */
	protected void addFlowIntoSplit(String splitID, String flowID) {
		getDesignPage().addSplitFlow(splitID, flowID);
		
		String splitFlow = getSourcePage().evaluateXPath(JOB, appendIDSelector(SPLIT, splitID), FLOW, ID);
		assertThat(splitFlow, is(flowID));
		
		editor.save();
		assertNoProblems();
	}
	
	/**
	 * Add new flow with id into existing job
	 * @param flowID the id of the new flow element
	 */
	protected void addFlow(String flowID){
		getDesignPage().addFlow(flowID);
		
		String flow = getSourcePage().evaluateXPath(JOB, appendIDSelector(FLOW, flowID), ID);
		assertThat(flow, is(flowID));
		
		editor.save();
		assertNoProblems();
	}
}

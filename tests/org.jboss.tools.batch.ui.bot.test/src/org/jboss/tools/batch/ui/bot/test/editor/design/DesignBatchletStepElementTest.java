package org.jboss.tools.batch.ui.bot.test.editor.design;

import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.BATCHLET;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.ID;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.JOB;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.REF;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.STEP;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DesignBatchletStepElementTest extends DesignFlowElementsTestTemplate {

	private static final String STEP_ID = "My-step-batchlet";
	
	private static final String BATCHLET_CLASS = "DesignBatchlet";
	
	private static final String BATCHLET_ID = getBatchArtifactID(BATCHLET_CLASS);
	
	@Before
	public void createBatchArtifact(){
		createBatchArtifact(BatchArtifacts.BATCHLET, BATCHLET_CLASS);
	}

	@Test
	public void createStep(){
		addStep();
		addBatchlet();
	}

	private void addStep() {
		getDesignPage().addStep(STEP_ID);

		String stepID = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), ID);
		assertThat(stepID, is(STEP_ID));
		
		editor.save();
		assertNoProblems();
	}

	private void addBatchlet() {
		getDesignPage().addBatchlet(STEP_ID, BATCHLET_ID);

		String batchletID = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), BATCHLET, REF);
		assertThat(batchletID, is(BATCHLET_ID));

		editor.save();
		assertNoProblems();
	}
}

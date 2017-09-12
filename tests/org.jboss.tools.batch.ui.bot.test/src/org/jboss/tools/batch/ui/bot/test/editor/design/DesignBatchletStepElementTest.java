package org.jboss.tools.batch.ui.bot.test.editor.design;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Before;
import org.junit.Test;

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
		addStep(STEP_ID);
		addBatchlet(STEP_ID, BATCHLET_ID);
	}
}

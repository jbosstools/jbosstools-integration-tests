package org.jboss.tools.batch.ui.bot.test.editor.design;

import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.DECISION;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.ID;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.JOB;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.REF;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DesignDecisionElementTest extends DesignFlowElementsTestTemplate {

	private static final String DECISION_ID = "My-decision";
	
	private static final String DECIDER_CLASS = "DesignDecider";
	
	private static final String DECIDER_ID = getBatchArtifactID(DECIDER_CLASS);
	
	@Before
	public void createDecisionClass(){
		createBatchArtifact(BatchArtifacts.DECIDER, DECIDER_CLASS);
	}
	
	@Test
	public void createDecision(){
		addDecision();
		setDeciderRef();
	}

	private void addDecision() {
		getDesignPage().addDecision(DECISION_ID);
		
		String decisionID = getSourcePage().evaluateXPath(JOB, appendIDSelector(DECISION, DECISION_ID), ID);
		assertThat(decisionID, is(DECISION_ID));
		
		editor.save();
		assertNumberOfProblems(1, 0);
	}

	private void setDeciderRef() {
		getDesignPage().setDecisionRef(DECISION_ID, DECIDER_ID);
		
		String decisionRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(DECISION, DECISION_ID), REF);
		assertThat(decisionRef, is(DECIDER_ID));
		
		editor.save();
		assertNoProblems();
	}
}

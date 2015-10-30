package org.jboss.tools.batch.ui.bot.test.editor.design;

import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.FLOW;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.ID;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.JOB;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.SPLIT;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DesignSplitElementTest extends DesignFlowElementsTestTemplate {

	private static final String SPLIT_ID = "My-split";
	
	private static final String FLOW_ID = "My-split-flow";
	
	@Test
	public void createSplit(){
		addSplit();
		addFlow();
	}

	private void addSplit() {
		getDesignPage().addSplit(SPLIT_ID);
		
		String splitID = getSourcePage().evaluateXPath(JOB, appendIDSelector(SPLIT, SPLIT_ID), ID);
		assertThat(splitID, is(SPLIT_ID));
		
		editor.save();
		assertNoProblems();
	}

	private void addFlow() {
		getDesignPage().addSplitFlow(SPLIT_ID, FLOW_ID);
		String splitFlowID = getSourcePage().evaluateXPath(JOB, appendIDSelector(SPLIT, SPLIT_ID), FLOW, ID);
		assertThat(splitFlowID, is(FLOW_ID));
		
		editor.save();
		assertNoProblems();
	}
}

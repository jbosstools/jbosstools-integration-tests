package org.jboss.tools.batch.ui.bot.test.editor.design;

import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.FLOW;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.ID;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.JOB;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DesignFlowElementTest extends DesignFlowElementsTestTemplate {

	private static final String FLOW_ID = "My-flow";
	
	@Test
	public void createFlow(){
		getDesignPage().addFlow(FLOW_ID);
		
		String flowID = getSourcePage().evaluateXPath(JOB, appendIDSelector(FLOW, FLOW_ID), ID);
		assertThat(flowID, is(FLOW_ID));
		
		editor.save();
		assertNoProblems();
	}
}

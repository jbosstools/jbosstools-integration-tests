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

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Before;
import org.junit.Test;

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
		addDecision(DECISION_ID);
		setDeciderRef(DECISION_ID, DECIDER_ID);
	}
}

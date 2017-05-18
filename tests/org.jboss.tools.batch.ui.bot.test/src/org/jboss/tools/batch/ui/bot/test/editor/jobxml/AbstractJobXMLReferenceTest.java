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

import org.junit.Before;

public class AbstractJobXMLReferenceTest extends AbstractJobXMLTest {

	protected final String BATCHLET_REF = "batchlet";
	
	protected final String STEP_LISTENER_REF = "stepListener";
	
	protected final String READER_REF = "reader";
	
	protected final String WRITER_REF = "writer";
	
	protected final String JOB_LISTENER_REF = "jobListener";
	
	protected final String CUSTOM_LISTENER_REF = "customClassListener";
	
	protected final String CUSTOM_QUALIFIED_LISTENER_REF = "customListener";
	
	protected final String PROCESSOR_REF = "processor";
	
	protected final String CHECK_REF = "checkpointAlgorithm";
	
	protected final String DECIDER_REF = "decider";
	
	protected final String MAPPER_REF = "mapper";
	
	protected final String REDUCER_REF = "reducer";
	
	protected final String ANALYZER_REF = "analyzer";
	
	protected final String COLLECTOR_REF = "collector";	
	
	protected final String BATCH_FILE = "job-ref.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE);
	}
	
}

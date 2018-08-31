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
package org.jboss.tools.batch.ui.bot.test.editor.design;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Before;
import org.junit.Test;

public class DesignChunkStepElementTest extends DesignFlowElementsTestTemplate {

	private static final String STEP_ID = "My-step-chunk";
	
	private static final String READER_CLASS = "DesignReader";
	
	private static final String READER_ID = getBatchArtifactID(READER_CLASS);
	
	private static final String WRITER_CLASS = "DesignWriter";
	
	private static final String WRITER_ID = getBatchArtifactID(WRITER_CLASS);
	
	private static final String CHECKPOINT_CLASS = "DesignCheckpoint";
	
	private static final String CHECKPOINT_ID = getBatchArtifactID(CHECKPOINT_CLASS);
	
	private static final String PROCESSOR_CLASS = "DesignProcessor";
	
	private static final String PROCESSOR_ID = getBatchArtifactID(PROCESSOR_CLASS);

	@Before
	public void createBatchArtifacts(){
		createBatchArtifact(BatchArtifacts.ITEM_READER, READER_CLASS);
		createBatchArtifact(BatchArtifacts.ITEM_WRITER, WRITER_CLASS);
		createBatchArtifact(BatchArtifacts.CHECKPOINT_ALGORITHM, CHECKPOINT_CLASS);
		createBatchArtifact(BatchArtifacts.ITEM_PROCESSOR, PROCESSOR_CLASS);
	}
	
	@Test
	public void createStep(){
		addStep(STEP_ID);
		addChunk(STEP_ID);
		setReaderRef(STEP_ID, READER_ID);
		setWriterRef(STEP_ID, WRITER_ID);
		setProcessor(STEP_ID, PROCESSOR_ID);
		setCheckpoint(STEP_ID, CHECKPOINT_ID);
	}
}

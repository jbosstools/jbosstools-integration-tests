package org.jboss.tools.batch.ui.bot.test.editor.design;

import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.CHECKPOINT;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.CHUNK;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.ID;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.JOB;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.PROCESSOR;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.READER;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.REF;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.STEP;
import static org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorSourcePage.WRITER;

import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
		addStep();
		addChunk();
		serReaderRef();
		setWriterRef();
		setProcessor();
		setCheckpoint();
	}

	private void addStep() {
		getDesignPage().addStep(STEP_ID);
		
		String stepID = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), ID);
		assertThat(stepID, is(STEP_ID));
		
		editor.save();
		assertNoProblems();
	}
	
	private void addChunk() {
		getDesignPage().addChunk(STEP_ID);
		
		editor.save();
		assertNumberOfProblems(1, 0);
	}
	
	private void serReaderRef() {
		getDesignPage().setReaderRef(STEP_ID, READER_ID);
		
		String readerRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), CHUNK, READER, REF);
		assertThat(readerRef, is(READER_ID));
		
		editor.save();
		assertNumberOfProblems(1, 0);
	}

	private void setWriterRef() {
		getDesignPage().setWriterRef(STEP_ID, WRITER_ID);
		
		String writerRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), CHUNK, WRITER, REF);
		assertThat(writerRef, is(WRITER_ID));
		
		editor.save();
		assertNoProblems();
	}
	private void setProcessor() {
		getDesignPage().addProcessor(STEP_ID, PROCESSOR_ID);
		
		String processorRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), CHUNK, PROCESSOR, REF);
		assertThat(processorRef, is(PROCESSOR_ID));
		
		editor.save();
		assertNoProblems();
	}

	private void setCheckpoint() {
		getDesignPage().addCheckpointAlgorithm(STEP_ID, CHECKPOINT_ID);
		
		String checkpointRef = getSourcePage().evaluateXPath(JOB, appendIDSelector(STEP, STEP_ID), CHUNK, CHECKPOINT, REF);
		assertThat(checkpointRef, is(CHECKPOINT_ID));
		
		editor.save();
		assertNoProblems();
	}
}

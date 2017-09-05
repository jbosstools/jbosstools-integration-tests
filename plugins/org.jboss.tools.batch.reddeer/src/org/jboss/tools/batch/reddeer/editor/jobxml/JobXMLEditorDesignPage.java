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
package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.LabeledCheckBox;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.batch.reddeer.editor.BatchExceptionType;
import org.jboss.tools.batch.reddeer.editor.design.BatchEditorTable;

/**
 * Represents the design tab of job xml editor.
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditorDesignPage {

	private static final String JOB = "Job";

	private static final String DECISION = "Decision";

	private static final String FLOW = "Flow";

	private static final String SPLIT = "Split";

	private static final String STEP = "Step";

	public void selectJob() {
		selectNode(JOB);
	}

	public String getJobID() {
		return new DefaultText(new DefaultSection(JOB), 0).getText();
	}

	public void addDecision(String id) {
		addFlowElement(DECISION, id);
	}

	public void addFlow(String id) {
		addFlowElement(FLOW, id);
	}

	public void addSplit(String id) {
		addFlowElement(SPLIT, id);
	}

	public void addStep(String id) {
		addFlowElement(STEP, id);
	}
	
	public void addJobListener(String ref) {
		selectNode(JOB, "Listeners");
		new ContextMenuItem("Add Job Listener").select();
		new DefaultText(new DefaultSection("<Listener>"), 0).setText(ref);
	}
	
	public void addFlowElementListener(String [] path, String ref) {
		selectNode(path);
		new ContextMenuItem("Add Step Listener").select();
		new DefaultText(new DefaultSection("<Listener>"), 0).setText(ref);
	}

	public void addBatchlet(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID);
		new ContextMenuItem("Add", "Batchlet").select();
		new DefaultText(new DefaultSection("Batchlet"), 0).setText(ref);
	}

	public void addChunk(String stepID) {
		selectNode(JOB, "Flow Elements", stepID);
		new ContextMenuItem("Add", "Chunk").select();
	}

	public void setReaderRef(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID, "Chunk", "Reader");
		new DefaultText(new DefaultSection("Reader"), 0).setText(ref);
	}

	public void setWriterRef(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID, "Chunk", "Writer");
		new DefaultText(new DefaultSection("Writer"), 0).setText(ref);
	}

	public void addCheckpointAlgorithm(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID, "Chunk");
		new ContextMenuItem("Add", "Checkpoint Algorithm").select();
		new DefaultText(new DefaultSection("Checkpoint Algorithm"), 0).setText(ref);
	}

	public void addProcessor(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID, "Chunk");
		new ContextMenuItem("Add", "Processor").select();
		new DefaultText(new DefaultSection("Processor"), 0).setText(ref);
	}

	public void addSplitFlow(String splitID, String id) {
		selectNode(JOB, "Flow Elements", splitID);
		new ContextMenuItem("Add Flow").select();
		new DefaultText(new DefaultSection(FLOW), 0).setText(id);
	}

	public void setDecisionRef(String id, String ref) {
		selectNode(JOB, "Flow Elements", id);
		new DefaultText(new DefaultSection(DECISION), 1).setText(ref);
	}

	public void addProperty(String stepID, String artifact, String propertyName, final Object value) {
		selectNode(JOB, "Flow Elements", stepID, artifact);
		new LabeledCheckBox("Properties").toggle(true);
		
		BatchEditorTable table = new BatchEditorTable("Properties");
		table.addItem(propertyName, value.toString());
	}
	
	public void addExceptionClass(String stepID, String artifact, String sectionName,
			String exceptionType, String exceptionClass, BatchExceptionType type)
	{
		selectNode(JOB, "Flow Elements", stepID, artifact);
		DefaultSection section = new DefaultSection(sectionName);
		new CheckBox(section, exceptionType).toggle(true);
		
		BatchEditorTable table = new BatchEditorTable(sectionName, type.getIndex());
		table.addItem(exceptionClass);
	}

	public void addExceptionClass(String stepID, String artifact, String sectionName,
			String exceptionType, String exceptionClass) {
		addExceptionClass(stepID, artifact, sectionName, exceptionType, exceptionClass, BatchExceptionType.INCLUDE);
	}

	private void addFlowElement(String name, String id) {
		selectNode(JOB, "Flow Elements");
		new ContextMenuItem("Add", name).select();
		new DefaultText(new DefaultSection(name), 0).setText(id);
	}

	public void selectNode(String... path) {
		new DefaultTreeItem(path).select();
	}
}

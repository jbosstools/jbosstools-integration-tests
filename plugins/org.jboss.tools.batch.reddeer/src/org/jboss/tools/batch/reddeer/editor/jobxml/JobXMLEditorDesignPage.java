package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

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
	
	public void selectJob(){
		selectNode(JOB);
	}
	
	public String getJobID(){
		return new DefaultText(new DefaultSection(JOB), 0).getText();
	}
	
	public void addDecision(String id){
		addFlowElement(DECISION, id);
	}
	
	public void addFlow(String id){
		addFlowElement(FLOW, id);
	}
	
	public void addSplit(String id){
		addFlowElement(SPLIT, id);
	}
	
	public void addStep(String id){
		addFlowElement(STEP, id);
	}
	
	public void addBatchlet(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID);
		new ContextMenu("Add", "Batchlet").select();
		new DefaultText(new DefaultSection("Batchlet"), 0).setText(ref);		
	}
	
	public void addChunk(String stepID) {
		selectNode(JOB, "Flow Elements", stepID);
		new ContextMenu("Add", "Chunk").select();
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
		new ContextMenu("Add", "Checkpoint Algorithm").select();
		new DefaultText(new DefaultSection("Checkpoint Algorithm"), 0).setText(ref);
	}
	
	public void addProcessor(String stepID, String ref) {
		selectNode(JOB, "Flow Elements", stepID, "Chunk");
		new ContextMenu("Add", "Processor").select();
		new DefaultText(new DefaultSection("Processor"), 0).setText(ref);
	}
	
	public void addSplitFlow(String splitID, String id){
		selectNode(JOB, "Flow Elements", splitID);
		new ContextMenu("Add Flow").select();
		new DefaultText(new DefaultSection(FLOW), 0).setText(id);
	}
	
	public void setDecisionRef(String id, String ref){
		selectNode(JOB, "Flow Elements", id);
		new DefaultText(new DefaultSection(DECISION), 1).setText(ref);
	}
	
	public void addProperty(String stepID, String artifact, String propertyName, Object value) {
		selectNode(JOB, "Flow Elements", stepID, artifact);
		new LabeledCheckBox("Properties").toggle(true);
		DefaultSection section = new DefaultSection("Properties");
		DefaultToolItem toolitem = new DefaultToolItem(section, new WithTooltipTextMatcher("Add Property"));
		toolitem.click();
		KeyboardFactory.getKeyboard().type(propertyName);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		KeyboardFactory.getKeyboard().type(value.toString());
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
	}
	
	public void addExceptionClass(String stepID, String artifact, String sectionName, 
			String exceptionType, String exceptionClass) {
		selectNode(JOB, "Flow Elements", stepID, artifact);
		DefaultSection section = new DefaultSection(sectionName);
		new CheckBox(section, exceptionType).click();
		DefaultToolItem toolitem = new DefaultToolItem(section, new WithTooltipTextMatcher("Add Class Element"));
		toolitem.click();
		KeyboardFactory.getKeyboard().type(exceptionClass);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
	}
	
	private void addFlowElement(String name, String id){
		selectNode(JOB, "Flow Elements");
		new ContextMenu("Add", name).select();
		new DefaultText(new DefaultSection(name), 0).setText(id);
	}
	
	private void selectNode(String... path){
		new DefaultTreeItem(path).select();
	}
}

package org.jboss.tools.batch.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard page for creating batch artifacts
 * 
 * @author Lucia Jelinkova
 *
 */
public class NewBatchArtifactWizardPage extends WizardPage {

	public String getSourceFolder(){
		return new LabeledText("Source folder:").getText();
	}

	public void setSourceFolder(String sourceFolder){
		new LabeledText("Source folder:").setText(sourceFolder);
	}
	
	public String getPackage(){
		return new LabeledText("Package:").getText();
	}
	
	public void setPackage(String packageName) {
		new LabeledText("Package:").setText(packageName);
	}
	
	public String getName(){
		return new LabeledText("Name:").getText();
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public BatchArtifacts getArtifact(){
		throw new UnsupportedOperationException();
	}
	
	public void setArtifact(BatchArtifacts artifact){
		new LabeledCombo("Artifact:").setSelection(artifact.getName());
	}
}

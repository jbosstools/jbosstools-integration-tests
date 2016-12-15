package org.jboss.tools.batch.reddeer.wizard;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
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
	
	public void selectImplementInterface(){
		new RadioButton("Implement interface").click();
	}
	
	public void selectExtendAbstractClass(){
		new RadioButton("Extend abstract class").click();
	}
	
	public void setNamed(){
		new RadioButton("@Named").click();
	}
	
	public void setBatchXML(){
		new RadioButton("batch.xml").click();
	}
	
	public void setClassloader(){
		new RadioButton("Class loader").click();
	}
	
	public void setArtifactName(String name){
		new LabeledText("Artifact name:").setText(name);
	}
	
	public void addProperty(String name){
		new PushButton("Add").click();
		new WaitUntil(new ShellWithTextIsAvailable("Add Property"));
		new DefaultShell("Add Property").setFocus();
		new LabeledText("Field name:").setText(name);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Add Property"));
	}
	
	public void removeProperty(String name){
		new DefaultList().select(name);
		new PushButton("Remove").click();
	}
	
	public List<String> getProperties(){
		return Arrays.asList(new DefaultList().getListItems());
	}
}

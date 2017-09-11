package org.jboss.tools.batch.reddeer.wizard;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard page for creating of job.xml file
 * 
 * @author Lucia Jelinkova
 *
 */
public class NewJobXMLFileWizardPage extends WizardPage {

	public NewJobXMLFileWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public String getFileName(){
		return new LabeledText("File name:").getText();
	}
	
	public void setFileName(String name){
		new LabeledText("File name:").setText(name);
	}
	
	public String getJobID(){
		return new LabeledText("Job ID:").getText();
	}
	
	public void setJobID(String id){
		new LabeledText("Job ID:").setText(id);
	}
}

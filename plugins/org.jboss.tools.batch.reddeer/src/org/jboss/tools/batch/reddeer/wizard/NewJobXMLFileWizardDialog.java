package org.jboss.tools.batch.reddeer.wizard;

import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;

/**
 * Wizard dialog for creating of job.xml files
 * 
 * @author Lucia Jelinkova, Ondrej Dockal
 *
 */
public class NewJobXMLFileWizardDialog extends NewMenuWizard {

	public NewJobXMLFileWizardDialog() {
		super("New Batch Job XML", "Batch", "Batch Job XML File");
	}
}

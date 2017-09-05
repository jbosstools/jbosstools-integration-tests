package org.jboss.tools.batch.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

/**
 * Wizard dialog for creating of job.xml files
 * 
 * @author Lucia Jelinkova
 *
 */
public class NewJobXMLFileWizardDialog extends NewMenuWizard {

	public NewJobXMLFileWizardDialog() {
		super("New Batch Job XML", "Batch", "Batch Job XML File");
	}
}

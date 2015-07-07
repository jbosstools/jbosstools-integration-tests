package org.jboss.tools.maven.reddeer.apt.ui.preferences;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.dialogs.ProjectPropertyPage;

public class AnnotationProcessingPropertyPage extends ProjectPropertyPage{
	
	public AnnotationProcessingPropertyPage(Project project) {
		super(project,"Maven","Annotation Processing");
	}

}

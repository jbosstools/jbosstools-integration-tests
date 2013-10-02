package org.jboss.tools.modeshape.reddeer.view;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.modeshape.reddeer.shell.PublishedLocations;
import org.jboss.tools.modeshape.reddeer.wizard.ModeshapePublishWizard;

/**
 * ModeShape Explorer is an imaginary explorer (doesn't exist) which can used for
 * publishing/unpublish to the repository.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeExplorer {

	public ModeshapePublishWizard publish(String project, String... item) {
		new ProjectExplorer().getProject(project).getProjectItem(item).select();
		new ContextMenu("ModeShape", "Publish").select();
		return new ModeshapePublishWizard();
	}

	public ModeshapePublishWizard unpublish(String project, String... item) {
		new ProjectExplorer().getProject(project).getProjectItem(item).select();
		new ContextMenu("ModeShape", "Unpublish").select();
		return new ModeshapePublishWizard();
	}

	public PublishedLocations showPublishedLocations(String project, String... item) {
		new ProjectExplorer().getProject(project).getProjectItem(item).select();
		new ContextMenu("ModeShape", "Show Published Locations").select();
		return new PublishedLocations();
	}
	
}

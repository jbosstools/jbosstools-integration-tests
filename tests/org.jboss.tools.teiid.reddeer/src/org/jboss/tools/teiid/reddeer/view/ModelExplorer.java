package org.jboss.tools.teiid.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.ShellTree;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;

/**
 * This class represents a model explorer
 * 
 * @author apodhrad
 *
 */
public class ModelExplorer extends WorkbenchView {

	public ModelExplorer() {
		super("Teiid Designer", "Model Explorer");
	}
	
	/**
	 * Creates a new model project
	 * 
	 * @param modelName
	 * @return
	 */
	public ModelProject createModelProject(String modelName) {
		ModelProjectWizard wizard = new ModelProjectWizard();
		wizard.open();
		wizard.getFirstPage().setProjectName(modelName);
		wizard.finish();
		return getModelProject(modelName);
	}

	/**
	 * Returns a model project with a given name
	 * 
	 * @param modelName
	 * @return
	 */
	public ModelProject getModelProject(String modelName) {
		for (ModelProject ModelProject : getModelProjects()) {
			if (ModelProject.getName().equals(modelName)) {
				return ModelProject;
			}
		}
		return null;
	}

	/**
	 * Returns all model projects
	 * 
	 * @return
	 */
	public List<ModelProject> getModelProjects() {
		List<ModelProject> ModelProjects = new ArrayList<ModelProject>();

		for (TreeItem item : getModelProjectExplorerTree().getItems()) {
			ModelProjects.add(new ModelProject(item));
		}
		return ModelProjects;
	}

	protected Tree getModelProjectExplorerTree() {
		return new ShellTree();
	}
}

package org.jboss.tools.ws.reddeer.jaxrs.core;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;

/**
 * Represents item in JAX-RS Explorer. Provides functions that parse
 * informations from the item.
 *
 * @author Radoslav Rabara
 */
public class RestService {

	private ProjectItem projectItem;

	/**
	 * Wraps given {@link ProjectItem} which has to represent a item in JAX-RS
	 * Explorer.
	 *
	 * @param restService item of JAX-RS Explorer
	 */
	public RestService(ProjectItem restService) {
		this.projectItem = restService;
	}

	/**
	 * Returns name of jax-rs service.
	 *
	 * @return name
	 */
	public String getName() {
		return projectItem.getText().split(" ")[0];
	}

	/**
	 * Returns path of jax-rs service.
	 *
	 * @return path
	 */
	public String getPath() {
		return projectItem.getText().split(" ")[1];
	}

	/**
	 * Returns additional informations about jax-rs service (consumes and
	 * produces value, and class name).
	 */
	protected List<ProjectItem> getInfo() {
		projectItem.open();
		new ProjectExplorer().activate();
		return projectItem.getChildren();
	}

	/**
	 * Returns consumes info.
	 */
	public String getConsumesInfo() {
		for (ProjectItem ti : getInfo()) {
			if (ti.getText().contains("consumes:")) {
				return ti.getText().split(" ")[1];
			}
		}
		return null;
	}

	/**
	 * Returns produces info.
	 */
	public String getProducesInfo() {
		for (ProjectItem ti : getInfo()) {
			if (ti.getText().contains("produces:")) {
				return ti.getText().split(" ")[1];
			}
		}
		return null;
	}

	/**
	 * Returns name of the class method.
	 */
	public String getClassMethodName() {
		for (ProjectItem ti : getInfo()) {
			if (!ti.getText().contains("produces:")
					&& !ti.getText().contains("consumes:")) {
				return ti.getText();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "RestService [name: " + getName() + ", path: " + getPath()
				+ ", produces: " + getProducesInfo() + ", consumes: "
				+ getConsumesInfo() + ", class method name: "
				+ getClassMethodName() + "]";
	}

	/**
	 * Returns wrapped {@link ProjectItem}.
	 * 
	 * @return project item that represents this rest service
	 */
	public ProjectItem getProjectItem() {
		return projectItem;
	}
}

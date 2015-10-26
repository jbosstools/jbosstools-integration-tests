package org.jboss.tools.openshift.reddeer.condition;

import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;

public class ResourceExists implements WaitCondition {

	private OpenShiftProject project;
	private Matcher resourceNameMatcher;
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	private ResourceState buildState;
	private Resource resource;
	
	public ResourceExists(String server, String username, String projectName, Resource resource, String resourceName) {
		this(server, username, projectName, resource, new WithTextMatcher(resourceName), ResourceState.UNSPECIFIED);
	}
	
	public ResourceExists(String server, String username, String projectName, Resource resource, String resourceName,
			ResourceState resourceState) {
		this(server, username, projectName, resource, new WithTextMatcher(resourceName), resourceState);
	}
	
	public ResourceExists(String server, String username, Resource resource, String projectName, Matcher nameMatcher) {
		this(server, username, projectName, resource, nameMatcher, ResourceState.UNSPECIFIED);
	}
		
	public ResourceExists(String server, String username, String projectName, Resource resource,
			Matcher nameMatcher, ResourceState buildState) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		project = explorer.getOpenShift3Connection(username, server).getProject(projectName);
		resourceNameMatcher = nameMatcher;
		this.buildState = buildState;
		this.resource = resource;
	}

	@Override
	public boolean test() {
		List<OpenShiftResource> resources = project.getOpenShiftResources(resource);
		if (resources.isEmpty()) {
			return false;
		}
		for (OpenShiftResource rsrc: resources) {			
			if (resourceNameMatcher.matches(treeViewerHandler.getNonStyledText(rsrc.getTreeItem()))) {
				if (!buildState.equals(ResourceState.UNSPECIFIED)) {
					return treeViewerHandler.getStyledTexts(rsrc.getTreeItem())[0].contains(buildState.toString());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		return "Waiting for resource " + resource.toString() + " with name matching matcher " 
					+ resourceNameMatcher.toString() + ".";  
	}
}

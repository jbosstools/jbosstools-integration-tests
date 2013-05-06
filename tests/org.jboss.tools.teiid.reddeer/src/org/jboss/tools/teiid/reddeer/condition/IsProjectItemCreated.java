package org.jboss.tools.teiid.reddeer.condition;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * Condition that specifies if a given project item was created
 * 
 * @author apodhrad
 * 
 */
public class IsProjectItemCreated implements WaitCondition {

	private String projectName;
	private String[] projectItem;
	private Logger log = Logger.getLogger(this.getClass());

	public IsProjectItemCreated(String projectName, String... projectItemPath) {
		this.projectItem = projectItemPath;
		this.projectName = projectName;
	}

	@Override
	public boolean test() {
		log.info("Waiting for " + projectName + "/" + toString(projectItem));
		boolean result = new ProjectExplorer().getProject(projectName).containsItem(projectItem);
		return result;
	}

	@Override
	public String description() {
		return "Project item '" + projectName + "/" + toString(projectItem) + "' wasn't created!";
	}

	private static String toString(String[] path) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < path.length; i++) {
			if (i > 0) {
				result.append("/");
			}
			result.append(path[i]);
		}
		return result.toString();
	}

}

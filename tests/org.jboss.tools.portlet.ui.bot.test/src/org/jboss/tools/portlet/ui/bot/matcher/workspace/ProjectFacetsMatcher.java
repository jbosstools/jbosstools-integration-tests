package org.jboss.tools.portlet.ui.bot.matcher.workspace;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;
import org.jboss.tools.portlet.ui.bot.task.dialog.property.ProjectPropertyDialogOpenTask;

/**
 * Checks if the project has the specified facets. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class ProjectFacetsMatcher extends JavaPerspectiveAbstractSWTMatcher<String> {

	private List<FacetDefinition> facets;
	
	public ProjectFacetsMatcher(FacetDefinition... facets) {
		this.facets = Arrays.asList(facets);
	}
	
	@Override
	protected boolean matchesSafelyInJavaPerspective(String project) {
		showPropertyDialog(project);
		boolean result = checkFacets();
		new PushButton("OK").click();
		return result;
	}

	private void showPropertyDialog(String project) {
		ProjectPropertyDialogOpenTask openTask = new ProjectPropertyDialogOpenTask();
		openTask.setProject(project);
		openTask.setPropertyPage("Project Facets");
		performInnerTask(openTask);
	}

	private boolean checkFacets() {
		boolean allChecked = true;
		List<TreeItem> items = new DefaultTree(1).getAllItems();
		for (FacetDefinition facet : facets){
			for(TreeItem item : items){
				if(item.getText().equals(facet.getName())){
					allChecked = item.isChecked();
					break;
				}
			}
		}
		
		return allChecked;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("contains facets ");
		description.appendValue(facets);
	}
	
}

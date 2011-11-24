package org.jboss.tools.portlet.ui.bot.test.matcher.workspace;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.task.dialog.ProjectPropertyDialogCloseTask;
import org.jboss.tools.portlet.ui.bot.task.dialog.ProjectPropertyDialogOpenTask;
import org.jboss.tools.portlet.ui.bot.task.facet.AbstractFacetTask;
import org.jboss.tools.portlet.ui.bot.test.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;

/**
 * Checks if the project has the specified facets. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ProjectFacetsMatcher extends AbstractSWTMatcher<String> {

	private List<FacetDefinition> facets;
	
	public ProjectFacetsMatcher(FacetDefinition... facets) {
		this.facets = Arrays.asList(facets);
	}
	
	@Override
	public boolean matchesSafely(String project) {
		showPropertyDialog(project);
		boolean result = checkFacets();
		performInnerTask(new ProjectPropertyDialogCloseTask());
		return result;
	}

	private void showPropertyDialog(String project) {
		ProjectPropertyDialogOpenTask openTask = new ProjectPropertyDialogOpenTask();
		openTask.setProject(project);
		openTask.setPropertyPage("Project Facets");
		performInnerTask(openTask);
	}

	private boolean checkFacets() {
		ValueCheckedTask task = new ValueCheckedTask();
		for (FacetDefinition facet : facets){
			task.addFacet(facet);			
		}
		performInnerTask(task);
		
		return task.allChecked;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendValue("file containing facet");
	}
	
	class ValueCheckedTask extends AbstractFacetTask {
		private boolean allChecked = true;
		
		@Override
		protected void processFacet(FacetDefinition facet, SWTBotTreeItem facetItem) {
			allChecked = allChecked && facetItem.isChecked();
		}
	}
}

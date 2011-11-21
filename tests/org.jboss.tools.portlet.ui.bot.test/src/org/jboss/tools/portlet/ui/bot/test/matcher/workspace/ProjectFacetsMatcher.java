package org.jboss.tools.portlet.ui.bot.test.matcher.workspace;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;
import org.jboss.tools.portlet.ui.bot.test.task.dialog.ProjectPropertyDialogCloseTask;
import org.jboss.tools.portlet.ui.bot.test.task.dialog.ProjectPropertyDialogOpenTask;
import org.jboss.tools.portlet.ui.bot.test.task.facet.AbstractFacetTask;
import org.jboss.tools.portlet.ui.bot.test.task.facet.FacetDefinition;

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
		return checkFacets();
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
		
		performInnerTask(new ProjectPropertyDialogCloseTask());
		return task.allChecked;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendValue("file containing facet");
	}
	
	class ValueCheckedTask extends AbstractFacetTask {
		private boolean allChecked = true;
		
		@Override
		protected void processFacet(SWTBotTreeItem facetItem) {
			allChecked = allChecked && facetItem.isChecked();
		}
	}
}

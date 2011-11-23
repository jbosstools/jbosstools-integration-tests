package org.jboss.tools.portlet.ui.bot.test.task.facet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;

/**
 * Common ancestor for facet handling tasks that can locate the facet node
 * in the tree (it can be simple facet of facet with category). 
 * 
 * It lets the subclasses to define the action upon the facet. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractFacetTask extends AbstractSWTTask {

	private List<FacetDefinition> facets;
	
	public AbstractFacetTask() {
		facets = new ArrayList<FacetDefinition>();
	}
	
	protected abstract void processFacet(FacetDefinition facet, SWTBotTreeItem facetTreewItem);
	
	@Override
	public void perform() {
		SWTBotTree tree = getTree();
		for (FacetDefinition facet : facets){
			processFacet(facet, getTreeItem(tree, facet));
		}
	}

	protected SWTBotTree getTree(){
		Tree tree = getBot().widget(new ProjectFacetsTableMatcher());
		return new SWTBotTree(tree); 
	}
	
	/**
	 * 
	 * @param tree
	 * @param facet
	 * @return The facet node (single or located within category)
	 */
	protected SWTBotTreeItem getTreeItem(SWTBotTree tree, FacetDefinition facet){
		if (facet.getCategory() == null){
			return tree.getTreeItem(facet.getName());
		} else {
			tree.expandNode(facet.getCategory());
			return tree.getTreeItem(facet.getCategory()).getNode(facet.getName());
		}
	}
	
	public void addFacet(FacetDefinition facet){
		facets.add(facet);
	}
	
	public void addAllFacets(List<FacetDefinition> f){
		facets.addAll(f);
	}
	
	/**
	 * Matcher for locating the facet selection tree on the screen. 
	 * 
	 * @author Lucia Jelinkova
	 *
	 */
	private static class ProjectFacetsTableMatcher extends BaseMatcher<Tree> {
		
		@Override
		public boolean matches(Object item) {
			if (!(item instanceof Tree)){
				return false;
			}
			Tree tree = (Tree) item;
			
			return tree.getColumnCount() > 0 && "Project Facet".equals(tree.getColumn(0).getText());
		}
		
		@Override
		public void describeTo(Description description) {
		}
	}
}

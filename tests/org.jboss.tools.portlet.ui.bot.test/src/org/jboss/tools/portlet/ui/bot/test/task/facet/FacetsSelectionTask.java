package org.jboss.tools.portlet.ui.bot.test.task.facet;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * Selects the given facets in the Project facetss tree. 
 * 
 * @author ljelinko
 *
 */
public class FacetsSelectionTask extends AbstractFacetTask {

	@Override
	protected void processFacet(SWTBotTreeItem facetItem) {
		facetItem.check();
	}
}

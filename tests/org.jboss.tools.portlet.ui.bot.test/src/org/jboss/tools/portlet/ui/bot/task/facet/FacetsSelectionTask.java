package org.jboss.tools.portlet.ui.bot.task.facet;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.portlet.ui.bot.test.entity.FacetDefinition;

/**
 * Selects the given facets in the Project facetss tree and optionaly also its version. 
 * 
 * @author ljelinko
 *
 */
public class FacetsSelectionTask extends AbstractFacetTask {

	@Override
	protected void processFacet(FacetDefinition facet, SWTBotTreeItem facetTreeItem) {
		facetTreeItem.check();
		
		if (facet.getVersion() != null){
			SWTBotMenu menu = facetTreeItem.contextMenu("Change Version...");
			menu.click();
			
			getBot().shell("Change Version").activate();
			getBot().comboBoxWithLabel("Version:").setSelection(facet.getVersion());
			getBot().button("OK").click();
		}
	}
}

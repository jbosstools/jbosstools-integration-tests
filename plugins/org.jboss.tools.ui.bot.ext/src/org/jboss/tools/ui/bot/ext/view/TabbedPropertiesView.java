package org.jboss.tools.ui.bot.ext.view;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList;
import org.hamcrest.Matcher;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GeneralProperties;
import org.jboss.tools.ui.bot.ext.widgets.SWTBotTabbedPropertyList;

/**
 * This class represents Tabbed properties view.
 * 
 * @author apodhrad
 * 
 */
public class TabbedPropertiesView extends ViewBase {

	private SWTBotTabbedPropertyList propertyList;

	/**
	 * Creates a new instance of PropertiesView
	 * 
	 * @param bot
	 */
	public TabbedPropertiesView() {
		viewObject = GeneralProperties.LABEL;
		Matcher matcher = allOf(widgetOfType(TabbedPropertyList.class));
		propertyList = new SWTBotTabbedPropertyList((TabbedPropertyList) bot().widget(matcher, 0));
	}

	/**
	 * Select a tab in the view located on index
	 * 
	 * @param index
	 */
	public void selectTab(int index) {
		propertyList.selectTab(index);
	}

	/**
	 * Select a tab in the view with the given mnemonic text
	 * 
	 * @param mnemonicText
	 */
	public void selectTab(String mnemonicText) {
		propertyList.selectTab(mnemonicText);
	}
}

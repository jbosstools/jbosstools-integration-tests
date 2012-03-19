package org.jboss.tools.portlet.ui.bot.test.core;

import static org.jboss.tools.portlet.ui.bot.entity.EntityFactory.portlet;

import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.portlet.ui.bot.test.template.LoadPortletInBrowserJBPortalTemplate;


public class LoadJavaPortletInBrowserJBPortal extends LoadPortletInBrowserJBPortalTemplate {

	@Override
	protected PortletDefinition getPortletDefinition() {
		return portlet(CreateJavaPortletGatein.CLASS_NAME);
	}
}

package org.jboss.tools.portlet.ui.bot.matcher.browser.portlet;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.entity.PortletDefinition;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.browser.PageSourceMatcher;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

/**
 * Check if the given portlet can be loaded in EPP 4.x runtime. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PortletLoadsInJBPortalMatcher extends AbstractSWTMatcher<PortletDefinition> {

	private static final String PORTAL_URL = "http://localhost:8080/portal/portal/default/";
	
	private PageSourceMatcher pageMatcher;
	
	private TaskDuration duration;
	
	public PortletLoadsInJBPortalMatcher(TaskDuration duration){
		this.duration = duration;
	}
	
	@Override
	public boolean matchesSafely(PortletDefinition portletTitle) {
		pageMatcher = new PageSourceMatcher(PORTAL_URL + portletTitle.getPage(), duration);
		pageMatcher.setBot(getBot());
		return pageMatcher.matchesSafely("<span class=\"portlet-titlebar-title\">" + portletTitle.getDisplayName() + "</span>");
	}

	@Override
	public void describeTo(Description description) {
		description.appendDescriptionOf(pageMatcher);
	}
}

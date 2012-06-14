package org.jboss.ide.eclipse.as.ui.bot.test.web;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

public class PageSourceMatcher extends TypeSafeMatcher<String> {

	private String pageText;

	public PageSourceMatcher() {
		super();
	}

	@Override
	public boolean matchesSafely(String item) {
		pageText = SWTBotFactory.getBot().browser().getText();
		return pageText.contains(item);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is on the page, but there was instead: ");
		description.appendValue(pageText);
	}
}

package org.jboss.ide.eclipse.as.ui.bot.test.web;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;

public class PageSourceMatcher extends TypeSafeMatcher<String> {

	private String pageText;

	private String url;
	
	public PageSourceMatcher() {
		super();
	}

	public PageSourceMatcher(String url) {
		this();
		this.url = url;
	}
	
	@Override
	public boolean matchesSafely(String item) {
		SWTBotBrowserExt browser = SWTBotFactory.getBot().browserExt();
		if (url != null){
			browser.loadUrlToBrowser(url, SWTBotFactory.getBot());
		}
		
		pageText = browser.getText();
		System.out.println("Page text: " + pageText);
		if ("".equals(item)){
			return pageText.equals(item);
		}
		return pageText.contains(item);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is on the page, but there was instead: ");
		description.appendValue(pageText);
	}
}

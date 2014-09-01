package org.jboss.tools.ws.ui.bot.test.utils;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;

public class Asserts {
	public static void assertContain(String actual, String expectedToContain) {
		assertThat(actual, StringContains.containsString(expectedToContain));
	}

	public static void assertNotContain(String actual, String expectedToContain) {
		assertThat(actual, IsNot.not(StringContains.containsString(expectedToContain)));
	}
}

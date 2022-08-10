package org.jboss.tools.usage.ui.bot.test;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.junit.Test;

public class DummyTest {
	
	@Test
	public void test() {
		DefaultShell dialog = new DefaultShell("JBoss Tools Usage");
		dialog.setFocus();
		assert(dialog.isVisible());
		new PushButton("No").click();
		assert(dialog.isDisposed());
	}
}

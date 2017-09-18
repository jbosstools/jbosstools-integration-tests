package org.jboss.tools.browsersim.rmi;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.condition.WidgetIsFound;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.ui.BrowserSim;

public class BrowsersimWidgetLookup {
	
	public static Shell getBrowsersimShell(){
		WidgetIsFound found = new WidgetIsFound(org.eclipse.swt.widgets.Shell.class, new WithTextMatcher("Device size will be truncated"));
		new WaitUntil(found, TimePeriod.DEFAULT, false);
		if(found.getResult() != null) {
			Shell s = new DefaultShell((org.eclipse.swt.widgets.Shell)found.getResult());
			new PushButton(s,"Truncate (recommended)").click();
			new WaitWhile(new ShellIsAvailable(s));
		}
		
		return new DefaultShell("BrowserSim");
	}
	
	public static IBrowser getBrowsersimBrowser(){
		getBrowsersimShell();
		return BrowserSim.getInstances().get(0).getBrowser();
		
	}

}
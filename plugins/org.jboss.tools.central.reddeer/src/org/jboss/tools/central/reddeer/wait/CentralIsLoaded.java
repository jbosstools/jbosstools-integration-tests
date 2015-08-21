package org.jboss.tools.central.reddeer.wait;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;

/**
 * Wait until JBoss Central is fully loaded (meaning jQuery is loaded).
 * 
 * @author rhopp
 *
 */

public class CentralIsLoaded implements WaitCondition {
	
	@Override
	public boolean test() {
		InternalBrowser internalBrowser = null;
		try {
			internalBrowser = new InternalBrowser();
		} catch (CoreLayerException ex) {
			return false;
		}
		Object jQuery = internalBrowser.evaluate("if (window.jQuery) return true; else return false;");
		if (jQuery instanceof Boolean) {
			return (Boolean) jQuery;
		}
		return false;
	}

	@Override
	public String description() {
		return "Waiting for Central to load";
	}

}

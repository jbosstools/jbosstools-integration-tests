package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

/**
 * This editor is displayed when user clicks on a specific server 
 * module and selects Show in -> Web Browser
 * 
 * Note: this should be replaced once WebPageEditor is implemented in RD
 * @author Lucia Jelinkova
 *
 */
public class ServerModuleWebPageEditor extends AbstractEditor {

	private InternalBrowser browser;
	
	public ServerModuleWebPageEditor(String moduleName) {
		super(new RegexMatcher(".*" + moduleName + ".*"));
	}
	
	public String getText(){
		activate();
		return getInternalBrowser().getText();
	}
	
	public void refresh(){
		getInternalBrowser().refresh();
	}

	private InternalBrowser getInternalBrowser(){
		if (browser == null){
			browser = new InternalBrowser();
		}
		return browser;
	}
}

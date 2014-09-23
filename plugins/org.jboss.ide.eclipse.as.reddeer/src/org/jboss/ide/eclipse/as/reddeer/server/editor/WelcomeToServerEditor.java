package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

/**
 * This editor is displayed when user clicks on a specific server and 
 * selects Show in -> Web Browser
 * 
 * Note: this should be replaced once WebPageEditor is implemented in RD
 * @author Lucia Jelinkova
 *
 */
public class WelcomeToServerEditor extends AbstractEditor {

	public WelcomeToServerEditor() {
		super(new RegexMatcher("Welcome to .*"));
	}
	
	public String getText(){
		activate();
		return new InternalBrowser().getText();
	}
}

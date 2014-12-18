package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.reddeer.swt.matcher.RegexMatcher;

/**
 * This editor is displayed when user clicks on a specific server and 
 * selects Show in -> Web Browser
 * 
 * Note: this should be replaced once WebPageEditor is implemented in RD
 * @author Lucia Jelinkova
 *
 */
public class WelcomeToServerEditor extends AbstractEditorWithBrowser {

	public WelcomeToServerEditor() {
		super(new RegexMatcher("(Welcome to .*|EAP 6)"));
	}
}

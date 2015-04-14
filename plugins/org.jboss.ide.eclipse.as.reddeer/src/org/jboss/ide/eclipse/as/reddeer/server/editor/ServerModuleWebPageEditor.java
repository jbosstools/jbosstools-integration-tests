package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.reddeer.common.matcher.RegexMatcher;

/**
 * This editor is displayed when user clicks on a specific server 
 * module and selects Show in -> Web Browser
 * 
 * Note: this should be replaced once WebPageEditor is implemented in RD
 * @author Lucia Jelinkova
 *
 */
public class ServerModuleWebPageEditor extends AbstractEditorWithBrowser {

	public ServerModuleWebPageEditor(String moduleName) {
		super(new RegexMatcher(".*" + moduleName + ".*"));
	}
}
